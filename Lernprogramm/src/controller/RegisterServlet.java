package controller;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Gender;
import model.User;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		String firstName = req.getParameter("first-name");
		String lastName = req.getParameter("last-name");
		Gender gender = Gender.valueOf(req.getParameter("gender"));
		String nickName = req.getParameter("nick-name");
		String password = req.getParameter("password");
		
		// TODO: Validitäts-Check
		User newUser = new User(firstName, lastName, gender, nickName, password);
		boolean success = this.registerUser(newUser);
	}
	
	private boolean registerUser(User newUser) {
		// TODO: Permanente Speicherung
		Map<String, User> registeredUsers = this.getRegisteredUsers();
		return registeredUsers.putIfAbsent(newUser.getNickName(), newUser) == null;
	}
	
	private Map<String, User> getRegisteredUsers() {
		@SuppressWarnings("unchecked")
		Map<String, User> registeredUsers = (Map<String, User>) this.getServletContext().getAttribute("registeredUsers");
		if (registeredUsers == null) {
			registeredUsers = new Hashtable<>();
		}
		return registeredUsers;
	}
}
