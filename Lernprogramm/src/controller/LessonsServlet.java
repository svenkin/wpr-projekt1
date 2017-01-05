package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonStructure;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import model.Lesson;
import model.User;
import tools.AccessChecker;

@WebServlet("/lessons")
public class LessonsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Resource
	private DataSource dataSource;
	private final String lessonsSql = "SELECT title, text_content, image, image_description FROM lesson WHERE section_id=? ORDER BY `order`;";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String chapterId = request.getParameter("chapter-id");
		String sectionId = request.getParameter("section-id");
		User user = (User) request.getSession().getAttribute("user");
		if (chapterId != null && !chapterId.isEmpty() &&
			sectionId != null && !sectionId.isEmpty() &&
			AccessChecker.getInstance().checkAccess(chapterId, sectionId, user, this.dataSource)) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			Json.createWriter(response.getWriter()).write(this.getLessons(sectionId));
		} else {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			Json.createWriter(response.getWriter()).write(Json.createObjectBuilder()
					.add("error", "access denied")
					.build());
		}
	}
	
	private JsonStructure getLessons(String sectionId) {
		JsonStructure structure = null;
		List<Lesson> lessons = this.loadLessons(sectionId);
		if (lessons != null) {
			JsonArrayBuilder data = Json.createArrayBuilder();
			for (Lesson l : lessons) {
				JsonObjectBuilder lessonObject = Json.createObjectBuilder();
				lessonObject
						.add("title", l.getTitle())
						.add("textContent", l.getTextContent());
				
				if (l.getImage() != null) lessonObject.add("image", l.getImage());
				else lessonObject.add("image", JsonObject.NULL);
				
				if (l.getImage() != null) lessonObject.add("imageDescription", l.getImageDescription());
				else lessonObject.add("imageDescription", JsonObject.NULL);
				
				data.add(lessonObject);
			}
			structure = Json.createObjectBuilder().add("data", data).build();
		}
		return structure;
	}
	
	private List<Lesson> loadLessons(String sectionId) {
		List<Lesson> lessons = null;
		try (Connection con = this.dataSource.getConnection();
			 PreparedStatement statement = con.prepareStatement(this.lessonsSql)) {
			statement.setString(1, sectionId);
			try (ResultSet rs = statement.executeQuery()) {
				lessons = new ArrayList<>();
				while (rs.next()) {
					lessons.add(new Lesson(
							rs.getString("title"),
							rs.getString("text_content"),
							rs.getString("image"),
							rs.getString("image_description")
					));
				}
			}
		} catch (SQLException e) {
			for (Throwable t : e.getSuppressed()) t.printStackTrace();
			e.printStackTrace();
		}
		return lessons;
	}
}
