package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tools.BetterCookies;

@WebServlet("/continue")
public class ContinueServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BetterCookies cookies = new BetterCookies(request.getCookies());
		String continueChapterId = cookies.getCookieValue("continue-chapter");
		String continueSectionId = cookies.getCookieValue("continue-section");
		if (continueChapterId == null || continueSectionId == null) response.sendRedirect("app.html#/");
		else response.sendRedirect("app.html#/" + continueChapterId + '/' + continueSectionId);
	}
}
