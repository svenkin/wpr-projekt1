package controller;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonStructure;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Gender;
import model.User;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/user")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User)request.getSession().getAttribute("user");
		if(user != null){
			JsonObjectBuilder userObject = Json.createObjectBuilder();
			userObject.add("firstName", user.getFirstName())
			.add("lastName", user.getLastName())
			.add("nickName", user.getNickName());
			String gender = "";
			switch(user.getGender()){
			case MALE: gender = "Männlich";
				break;
			case FEMALE: gender = "Weiblich";
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
