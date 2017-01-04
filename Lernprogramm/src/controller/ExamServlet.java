package controller;

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
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonStructure;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import model.Choice;
import model.ChoicesQuestion;
import model.Exam;
import model.Question;
import model.QuestionType;
import model.TextQuestion;

/**
 * Servlet implementation class ExamServlet
 */
@WebServlet("/exam")
public class ExamServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource
	private DataSource dataSource;
	private String selectExamBySectionId = "SELECT lernprogramm.exam.description, lernprogramm.question.question, lernprogramm.question.type, lernprogramm.answer.text, lernprogramm.answer.id as answerId, lernprogramm.answer.correct, lernprogramm.question.id as questionId"
			+ " FROM lernprogramm.exam JOIN lernprogramm.question ON lernprogramm.exam.id = question.exam_id"
			+ " JOIN lernprogramm.answer ON lernprogramm.question.id = answer.question_id WHERE exam.section_id = ?;";

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
										checkIfCorrect(rs.getInt("correct")), text);
								multiple.addChoice(choice);
							}
							questions.put(questionId, multiple);
							break;
						case "SINGLE":
							ChoicesQuestion single = (ChoicesQuestion) question;
							String singleText = rs.getString("text");
							if (!singleText.isEmpty()) {
								Choice choice = new Choice(rs.getInt("answerId"), questionId,
										checkIfCorrect(rs.getInt("correct")), singleText);
								single.addChoice(choice);
							}
							questions.put(questionId, single);
							break;
						}
					}
				}
				Iterator<Map.Entry<Integer, Question>> it = questions.entrySet().iterator();
				Vector<Question> questionsAsList = new Vector<>();
				while (it.hasNext()) {
					Map.Entry<Integer, Question> entry = it.next();
					questionsAsList.add(entry.getValue());
				}
				
				// Move to first row to fill exam data	
				System.out.println(hasRows);
				if(hasRows){
					rs.first();
					exam = new Exam(rs.getString("description"), Integer.parseInt(sectionId));
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
							.add("text", question.getQuestion()).add("choices", choices).add("questionId", choicesQuestion.getId()));

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

	private boolean checkIfCorrect(int numb) {
		return numb > 0;
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
					checkIfCorrect(rs.getInt("correct")), text);
			choicesQuestion.addChoice(choice);
		}
		return choicesQuestion;
	}
}
