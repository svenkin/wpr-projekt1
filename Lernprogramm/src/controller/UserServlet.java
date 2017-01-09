package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

import model.User;
import tools.SqlBooleanHelper;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/user")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource
	private DataSource dataSource;
	private String selectAnswersByNickName = "Select * from answered_questions Where user_nickname=?;";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			JsonArrayBuilder answeredQuestions = Json.createArrayBuilder();
			int correctAnswered = 0;
			try (Connection con = this.dataSource.getConnection();
					PreparedStatement statement = con.prepareStatement(this.selectAnswersByNickName)) {
				statement.setString(1, user.getNickName());
				try (ResultSet rs = statement.executeQuery()) {
					
					while(rs.next()){
						JsonObjectBuilder answeredQuestionObject = Json.createObjectBuilder();
						answeredQuestionObject.add("question", rs.getString("question"));
						boolean correct = SqlBooleanHelper.checkIfCorrect(rs.getInt("correct"));
						if(correct) correctAnswered++;
						answeredQuestionObject.add("correct", correct);
						answeredQuestions.add(answeredQuestionObject);
					}
				}
			} catch (SQLException e) {
				for (Throwable t : e.getSuppressed())
					t.printStackTrace();
				e.printStackTrace();
			}
			JsonObjectBuilder userObject = Json.createObjectBuilder();
			userObject.add("firstName", user.getFirstName()).add("lastName", user.getLastName()).add("nickName",
					user.getNickName()).add("answeredQuestions", answeredQuestions).add("correctAnswered", correctAnswered);
			String gender = "";
			switch (user.getGender()) {
			case MALE:
				gender = "Männlich";
				break;
			case FEMALE:
				gender = "Weiblich";
				break;
			}
			userObject.add("gender", gender);
			JsonStructure structure = Json.createObjectBuilder().add("data", userObject).build();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			Json.createWriter(response.getWriter()).write(structure);
		} else {
			response.setStatus(400);
		}
	}
}
