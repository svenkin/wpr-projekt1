package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import model.Gender;
import model.User;
import tools.ContextUser;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Resource
	private DataSource dataSource;
	private String loginUserSql = "SELECT first_name, last_name, gender, nick_name, password, access_chapter, access_section FROM user WHERE nick_name=?;";

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nickName = request.getParameter("nick-name");
		String password = request.getParameter("password");
		User user = loginUser(nickName, password);
		//On Login use ServletContext
		ContextUser.use(getServletContext());
		if (user != null) {
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			response.sendRedirect(response.encodeRedirectURL("app.html"));
		} else response.sendRedirect("error.html");
	}

	private User loginUser(String nickName, String password) {
		User user = null;
		try (Connection con = this.dataSource.getConnection();
			 PreparedStatement statement = con.prepareStatement(this.loginUserSql)) {
			statement.setString(1, nickName);
			try (ResultSet rs = statement.executeQuery()) {
				if (rs.next()) user = new User(
						rs.getString("first_name"),
						rs.getString("last_name"),
						Gender.valueOf(rs.getString("gender")),
						rs.getString("nick_name"),
						rs.getString("password"),
						rs.getString("access_chapter"),
						rs.getString("access_section"));
			}
		} catch (SQLException e) {
			for (Throwable t : e.getSuppressed()) t.printStackTrace();
			e.printStackTrace();
		}
		return user;
	}
}
