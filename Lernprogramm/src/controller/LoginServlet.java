package controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String nickName = req.getParameter("nick-name");
		String password = req.getParameter("password");

		User loggedInUser = loginUser(nickName, password);
		if (loggedInUser != null) {
			HttpSession session = req.getSession();
			res.sendRedirect(res.encodeRedirectURL("index.html"));
		}
		else res.sendRedirect("login.prototype.html");
	}

	private User loginUser(String nickName, String password) {
		User loggedInUser = null;
		@SuppressWarnings("unchecked")
		Map<String, User> registeredUsers = (Map<String, User>) this.getServletContext().getAttribute("registeredUsers");
		if (registeredUsers != null) {
			User registeredUser = registeredUsers.get(nickName);
			if (registeredUser != null && registeredUser.getPassword().equals(password)) loggedInUser = registeredUser;
		}
		return loggedInUser;
}}
