package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import model.Gender;
import model.User;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Resource
	private DataSource dataSource;
	private String registerUserSql = "INSERT INTO user (first_name, last_name, gender, nick_name, password, access_chapter, access_section) VALUES (?, ?, ?, ?, ?, ?, ?);";
	private String firstChapterSql = "SELECT id FROM chapter ORDER BY `order` LIMIT 1;";
	private String firstSectionSql = "SELECT id FROM section WHERE chapter_id = ? ORDER BY `order` LIMIT 1;";

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String firstName = request.getParameter("first-name");
		String lastName = request.getParameter("last-name");
		Gender gender = Gender.valueOf(request.getParameter("gender"));
		String nickName = request.getParameter("nick-name");
		String password = request.getParameter("password");

		// TODO: Validitäts-Check
		String firstChapterId = this.getFirstChapter();
		User newUser = new User(firstName, lastName, gender, nickName, password, firstChapterId, this.getFirstSection(firstChapterId));
		if (this.registerUser(newUser)) response.sendRedirect("tutorial.html");
		else request.getRequestDispatcher("error.html").forward(request, response);
	}
	
	private String getFirstChapter() {
		String firstChapterId = null;
		try (Connection con = this.dataSource.getConnection();
			 Statement statement = con.createStatement();
			 ResultSet rs = statement.executeQuery(this.firstChapterSql)) {
			if (rs.next()) firstChapterId = rs.getString("id");
		} catch (SQLException e) {
			for (Throwable t : e.getSuppressed()) t.printStackTrace();
			e.printStackTrace();
		}
		return firstChapterId;
	}
	
	private String getFirstSection(String chapterId) {
		String firstSectionId = null;
		try (Connection con = this.dataSource.getConnection();
			 PreparedStatement statement = con.prepareStatement(this.firstSectionSql)) {
			statement.setString(1, chapterId);
			try (ResultSet rs = statement.executeQuery()) {
				if (rs.next()) firstSectionId = rs.getString("id");
			}
		} catch (SQLException e) {
			for (Throwable t : e.getSuppressed()) t.printStackTrace();
			e.printStackTrace();
		}
		return firstSectionId;
	}

	private boolean registerUser(User newUser) {
		try (Connection con = this.dataSource.getConnection();
			 PreparedStatement statement = con.prepareStatement(this.registerUserSql)) {
			statement.setString(1, newUser.getFirstName());
			statement.setString(2, newUser.getLastName());
			statement.setString(3, newUser.getGender().toString());
			statement.setString(4, newUser.getNickName());
			statement.setString(5, newUser.getPassword());
			statement.setString(6, newUser.getAccessChapterId());
			statement.setString(7,  newUser.getAccessSectionId());
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			for (Throwable t : e.getSuppressed()) t.printStackTrace();
			e.printStackTrace();
			return false;
		}
	}
}
