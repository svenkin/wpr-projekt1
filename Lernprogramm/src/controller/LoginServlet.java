package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import model.Gender;
import model.User;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String loginUserSql = "SELECT first_name, last_name, gender, nick_name, password FROM user WHERE nick_name=?;";

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String nickName = req.getParameter("nick-name");
		String password = req.getParameter("password");

		User user = loginUser(nickName, password);
		if (user != null) {
			HttpSession session = req.getSession();
			session.setAttribute("user", user);
			res.sendRedirect(res.encodeRedirectURL("index.html"));
		} else res.sendRedirect("error.html");
	}

	private User loginUser(String nickName, String password) {
		User user = null;
		DataSource dataSource = (DataSource) this.getServletContext().getAttribute("dataSource");
		try (Connection con = dataSource.getConnection();
			 PreparedStatement statement = con.prepareStatement(this.loginUserSql)) {
			statement.setString(1, nickName);
			try (ResultSet rs = statement.executeQuery()) {
				if (rs.next()) user = new User(rs.getString(1), rs.getString(2), Gender.valueOf(rs.getString(3)), rs.getString(4), rs.getString(5));
			}
		} catch (SQLException e) {
			for (Throwable t : e.getSuppressed()) t.printStackTrace();
			e.printStackTrace();
		}
		return user;
	}
}
