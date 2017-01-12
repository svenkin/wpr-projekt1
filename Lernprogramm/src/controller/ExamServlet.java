package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import model.Choice;
import model.ChoicesQuestion;
import model.Exam;
import model.ExamResult;
import model.Question;
import model.QuestionType;
import model.TextQuestion;
import model.User;
import tools.BetterCookies;
import tools.SqlBooleanHelper;

/**
 * Servlet implementation class ExamServlet
 */
@WebServlet("/exam")
public class ExamServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource
	private DataSource dataSource;
	private String selectExamBySectionId = "SELECT exam.description, question.question, question.type, answer.text, answer.id as answerId, answer.correct, question.id as questionId"
			+ " FROM exam JOIN question ON exam.id = question.exam_id"
			+ " JOIN answer ON question.id = answer.question_id WHERE exam.section_id = ?;";
	private String selectNextSection = "Select section.order, section.id from section where section.order > (Select section.order from section Where section.id = ?) and section.chapter_id = ? limit 1;";
	private String updateUserAccessSection = "UPDATE user set access_section = ? Where nick_name = ?;";
	private String selectNextChapter = "Select chapter.order, chapter.id from chapter where chapter.id > ? limit 1;";
	private String updateUserAccessChapter = "UPDATE user set access_chapter = ? Where nick_name = ?;";
	private String selectFirstSection = "Select * from section where chapter_id = ? Order by section.order limit 1;";
	private String insertAnsweredQuestion = "INSERT INTO answered_questions (question, user_nickname, correct) VALUES (?, ?, ?);";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String sectionId = request.getParameter("section-id");

		if (sectionId != null && !sectionId.isEmpty()) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			Json.createWriter(response.getWriter()).write(this.getExam(sectionId));
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String sectionId = request.getParameter("section-id");
		String chapterId = request.getParameter("chapter-id");
		User currentUser = (User) request.getSession().getAttribute("user");
		if (sectionId != null && !sectionId.isEmpty() && chapterId != null && !chapterId.isEmpty()
				&& currentUser != null) {
			User updatedUser = this.updateUserAccess(sectionId, chapterId, currentUser);
			BetterCookies bc = new BetterCookies();
			Cookie chapterCookie = new Cookie("continue-chapter-id", updatedUser.getAccessChapterId());
			chapterCookie.setMaxAge(86400);
			bc.addCookie(chapterCookie);
			Cookie sectionCookie = new Cookie("continue-section-id", updatedUser.getAccessSectionId());
			sectionCookie.setMaxAge(86400);
			bc.addCookie(sectionCookie);
			bc.addAllCookiesToResponse(response);
			request.getSession().setAttribute("user", updatedUser);
			BufferedReader body = request.getReader();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			Json.createWriter(response.getWriter()).write(this.getExamResults(sectionId, body, currentUser));
		}

	}

	private Vector<ExamResult> checkExamResult(String sectionId, BufferedReader body, User currentUser) {
		Exam exam = loadExam(sectionId);
		Vector<ExamResult> results = new Vector<>();
		if (exam != null) {
			try (Connection con = this.dataSource.getConnection();
					PreparedStatement stmnt = con.prepareStatement(this.insertAnsweredQuestion)) {
				Hashtable<Integer, Question> examQuestions = exam.getPremappedQuestions();
				JsonReader reader = Json.createReader(body);
				JsonObject obj = reader.readObject();
				JsonValue val = obj.get("answers");
				JsonArray answers = (JsonArray) val;
				// For each answer in the answers array
				for (JsonValue jsonValue : answers) {
					boolean correct = false;
					JsonObject objc = (JsonObject) jsonValue;
					int questionId = objc.getInt("questionId");
					Question q = examQuestions.get(questionId);
					// Depending on question type cast the question and check if
					// correctly answered
					switch (q.getType().toString()) {
					case "SINGLE":
						ChoicesQuestion cq = (ChoicesQuestion) q;
						correct = cq.checkIfChoiceIsCorrect(objc.getInt("answer"));
						break;
					case "MULTIPLE":
						ChoicesQuestion cq2 = (ChoicesQuestion) q;
						JsonArray ans = objc.getJsonArray("answer");
						int[] arr = toIntArray(ans);
						correct = cq2.checkIfChoicesAreCorrect(arr);
						break;
					case "TEXT":
						TextQuestion text = (TextQuestion) q;
						correct = text.checkForKeywords(objc.getString("answer"));
						break;
					}
					stmnt.setString(1, q.getQuestion());
					stmnt.setString(2, currentUser.getNickName());
					stmnt.setInt(3, SqlBooleanHelper.booleanToInt(correct));
					stmnt.executeUpdate();
					results.add(new ExamResult(q.getQuestion(), correct));
				}
			} catch (SQLException e) {
				for (Throwable t : e.getSuppressed())
					t.printStackTrace();
				e.printStackTrace();
			}
		}
		return results;
	}

	private JsonStructure getExamResults(String sectionId, BufferedReader body, User currentUser) {
		Vector<ExamResult> results = checkExamResult(sectionId, body, currentUser);
		JsonObjectBuilder build = Json.createObjectBuilder();
		JsonArrayBuilder array = Json.createArrayBuilder();
		for (ExamResult examResult : results) {
			array.add(Json.createObjectBuilder().add("text", examResult.getText()).add("correct",
					examResult.isCorrect()));
		}
		build.add("data", array);
		return build.build();
	}

	private Exam loadExam(String sectionId) {
		Exam exam = null;
		try (Connection con = this.dataSource.getConnection();
				PreparedStatement statement = con.prepareStatement(this.selectExamBySectionId)) {
			statement.setString(1, sectionId);
			try (ResultSet rs = statement.executeQuery()) {
				Hashtable<Integer, Question> questions = new Hashtable<>();
				boolean hasRows = false;
				while (rs.next()) {
					hasRows = true;
					int questionId = rs.getInt("questionId");
					// If question doesnt exist yet, add question to hashtable
					// with id as key
					// If question already exist, only add the choice to the
					// existing question
					if (!questions.containsKey(questionId)) {
						String type = rs.getString("type");
						switch (type) {
						case "MULTIPLE":
							ChoicesQuestion multiple = fillChoicesQuestion(rs, QuestionType.MULTIPLE, null);
							questions.put(questionId, multiple);
							break;
						case "SINGLE":
							ChoicesQuestion single = fillChoicesQuestion(rs, QuestionType.SINGLE, null);
							questions.put(questionId, single);
							break;
						case "TEXT":
							TextQuestion text = new TextQuestion(rs.getString("question"), rs.getString("text"),
									questionId);
							text.setType(QuestionType.TEXT);
							questions.put(questionId, text);
							break;
						}
					} else {
						Question question = questions.get(questionId);
						String type = rs.getString("type");
						switch (type) {
						case "MULTIPLE":
							ChoicesQuestion multiple = (ChoicesQuestion) question;
							String text = rs.getString("text");
							if (!text.isEmpty()) {
								Choice choice = new Choice(rs.getInt("answerId"), questionId,
										SqlBooleanHelper.checkIfCorrect(rs.getInt("correct")), text);
								multiple.addChoice(choice);
							}
							questions.put(questionId, multiple);
							break;
						case "SINGLE":
							ChoicesQuestion single = (ChoicesQuestion) question;
							String singleText = rs.getString("text");
							if (!singleText.isEmpty()) {
								Choice choice = new Choice(rs.getInt("answerId"), questionId,
										SqlBooleanHelper.checkIfCorrect(rs.getInt("correct")), singleText);
								single.addChoice(choice);
							}
							questions.put(questionId, single);
							break;
						}
					}
				}
				// Hashtable only used for easier check if question already
				// exists, map it to vector
				Iterator<Map.Entry<Integer, Question>> it = questions.entrySet().iterator();
				Vector<Question> questionsAsList = new Vector<>();
				while (it.hasNext()) {
					Map.Entry<Integer, Question> entry = it.next();
					questionsAsList.add(entry.getValue());
				}

				// Move to first row to fill exam data
				if (hasRows) {
					rs.first();
					exam = new Exam(rs.getString("description"), Integer.parseInt(sectionId));
					exam.setPremappedQuestions(questions);
					exam.setQuestions(questionsAsList);
				}

				return exam;
			}
		} catch (SQLException e) {
			for (Throwable t : e.getSuppressed())
				t.printStackTrace();
			e.printStackTrace();
		}
		return null;
	}

	private JsonStructure getExam(String sectionId) {
		JsonStructure structure = null;
		Exam exam = this.loadExam(sectionId);
		if (exam != null) {
			Vector<Question> questions = exam.getQuestions();
			JsonObjectBuilder examObject = Json.createObjectBuilder().add("description", exam.getDescription());
			JsonArrayBuilder qs = Json.createArrayBuilder();
			for (Question question : questions) {
				if (question.getType().equals(QuestionType.MULTIPLE)
						|| question.getType().equals(QuestionType.SINGLE)) {
					ChoicesQuestion choicesQuestion = (ChoicesQuestion) question;
					JsonArrayBuilder choices = Json.createArrayBuilder();
					for (Choice choice : choicesQuestion.getChoices()) {
						choices.add(Json.createObjectBuilder().add("id", choice.getId()).add("text", choice.getText()));
					}
					qs.add(Json.createObjectBuilder().add("type", question.getType().toString())
							.add("text", question.getQuestion()).add("choices", choices)
							.add("questionId", choicesQuestion.getId()));

				} else if (question.getType().equals(QuestionType.TEXT)) {
					TextQuestion text = (TextQuestion) question;
					qs.add(Json.createObjectBuilder().add("text", text.getQuestion())
							.add("type", text.getType().toString()).add("questionId", text.getId()));
				}
			}
			examObject.add("questions", qs);
			structure = Json.createObjectBuilder().add("data", examObject).build();
		} else {
			structure = Json.createObjectBuilder().add("data", Json.createObjectBuilder()).build();
		}
		return structure;
	}

	private int[] toIntArray(JsonArray array) {
		int[] resArray = new int[array.size()];
		if (array != null) {
			int len = array.size();
			for (int i = 0; i < len; i++) {
				resArray[i] = array.getInt(i);
			}
		}
		return resArray;
	}

	private ChoicesQuestion fillChoicesQuestion(ResultSet rs, QuestionType type, Question quest) throws SQLException {
		ChoicesQuestion choicesQuestion = null;
		if (quest != null) {
			choicesQuestion = (ChoicesQuestion) quest;
		} else {
			choicesQuestion = new ChoicesQuestion(rs.getString("question"));
		}
		choicesQuestion.setType(type);
		choicesQuestion.setId(rs.getInt("questionId"));
		String text = rs.getString("text");
		if (!text.isEmpty()) {
			Choice choice = new Choice(rs.getInt("answerId"), rs.getInt("questionId"),
					SqlBooleanHelper.checkIfCorrect(rs.getInt("correct")), text);
			choicesQuestion.addChoice(choice);
		}
		return choicesQuestion;
	}

	private User updateUserAccess(String sectionId, String chapterId, User user) {
		try (Connection con = this.dataSource.getConnection();
				PreparedStatement statement = con.prepareStatement(this.selectNextSection)) {
			statement.setString(1, sectionId);
			statement.setString(2, chapterId);
			try (ResultSet rs = statement.executeQuery()) {
				if (!rs.next()) {
					System.out.println("Keine nächste Section!");
					// Fetch next chapter id
					if (user.getAccessChapterId().equals(chapterId)) {
						PreparedStatement stmnt = con.prepareStatement(this.selectNextChapter);
						stmnt.setString(1, chapterId);
						try (ResultSet chaptRes = stmnt.executeQuery()) {
							if (!chaptRes.next()) {
								System.out.println("Kein nächstes Chapter!");
							} else {
								int nextOrder = chaptRes.getInt("id");
								String nickname = user.getNickName();
								PreparedStatement upd = con.prepareStatement(this.updateUserAccessChapter);
								upd.setInt(1, nextOrder);
								upd.setString(2, nickname);
								upd.executeUpdate();
								user.setAccessChapterId(String.valueOf(nextOrder));
								System.out.println(
										"Access für Chapter: " + chapterId + " wurde auf " + nextOrder + " geupdated!");
								// Set acces_section auf erste Section im
								// Chapter
								PreparedStatement firstSectionStmnt = con.prepareStatement(this.selectFirstSection);
								firstSectionStmnt.setString(1, chapterId);
								try (ResultSet sectRes = stmnt.executeQuery()) {
									if (!sectRes.next()) {
										System.out.println("Neues Chapter hat keine sections!");
									} else {
										PreparedStatement updSect = con.prepareStatement(this.updateUserAccessSection);
										updSect.setInt(1, sectRes.getInt("id"));
										updSect.setString(2, nickname);
										updSect.executeUpdate();
										user.setAccessSectionId(sectRes.getString("id"));
										System.out.println("Access für Section: " + sectionId + " wurde auf "
												+ sectRes.getString("id") + " geupdated!");
									}
								}
							}
						}
					}

				} else {
					int nextOrder = rs.getInt("id");
					// Be sure users access ids are the same as the answered
					// exam (dont update access if user answers an old exam)
					if (user.getAccessSectionId().equals(sectionId) && user.getAccessChapterId().equals(chapterId)) {
						String nickname = user.getNickName();
						PreparedStatement upd = con.prepareStatement(this.updateUserAccessSection);
						upd.setInt(1, nextOrder);
						upd.setString(2, nickname);
						upd.executeUpdate();
						user.setAccessSectionId(String.valueOf(nextOrder));
						System.out.println(
								"Access für Section: " + sectionId + " wurde auf " + nextOrder + " geupdated!");

					} else {
						System.out.println("Alter Test wurde beantwortet");
					}
				}
				return user;
			}
		} catch (SQLException e) {
			for (Throwable t : e.getSuppressed())
				t.printStackTrace();
			e.printStackTrace();
		}
		return user;
	}
}
