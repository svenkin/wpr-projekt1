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
import javax.json.JsonStructure;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import model.Section;
import model.User;
import tools.AccessChecker;

@WebServlet("/sections")
public class SectionsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Resource
	private DataSource dataSource;
	private final String sectionsSql = "SELECT id, title, description FROM section WHERE chapter_id = ? ORDER BY `order`;";
	private final String sectionSql = "SELECT id, title, description FROM section WHERE chapter_id = ? AND id = ?;";
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String chapterId = request.getParameter("chapter-id");
		String sectionId = request.getParameter("section-id");
		User user = (User) request.getSession().getAttribute("user");
		if (chapterId != null && !chapterId.isEmpty() && AccessChecker.getInstance().checkAccess(chapterId, user, this.dataSource)) {
			JsonStructure body = null;
			if (sectionId != null && !sectionId.isEmpty()) {
				body = this.getSection(chapterId, sectionId);
			} else {
				body = this.getSections(chapterId, user);
			}
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			Json.createWriter(response.getWriter()).write(body);
		} else {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			Json.createWriter(response.getWriter()).write(Json.createObjectBuilder()
					.add("error", "access denied")
					.build());
		}
	}
	
	private JsonStructure getSections(String chapterId, User user) {
		JsonStructure structure = null;
		List<Section> sections = this.loadSections(chapterId);
		if (sections != null) {
			JsonArrayBuilder data = Json.createArrayBuilder();
			boolean access = true;
			for (Section s : sections) {
				data.add(Json.createObjectBuilder()
						.add("id", s.getId())
						.add("title", s.getTitle())
						.add("description", s.getDescription())
						.add("access", access));
				if (user.getAccessSectionId().equals(s.getId())) access = false;
			}
			structure = Json.createObjectBuilder().add("data", data).build();
		}
		return structure;
	}
	
	private JsonStructure getSection(String chapterId, String sectionId) {
		JsonStructure structure = null;
		Section section = this.loadSection(chapterId, sectionId);
		if (section != null) {
			structure = Json.createObjectBuilder()
					.add("data", Json.createObjectBuilder()
							.add("id", section.getId())
							.add("title", section.getTitle())
							.add("description", section.getDescription()))
					.build();
		}
		return structure;
	}
	
	private List<Section> loadSections(String chapterId) {
		List<Section> sections = null;
		try (Connection con = this.dataSource.getConnection();
			 PreparedStatement statement = con.prepareStatement(this.sectionsSql)) {
			statement.setString(1, chapterId);
			try (ResultSet rs = statement.executeQuery()) {
				sections = new ArrayList<>();
				while (rs.next()) {
					sections.add(new Section(
							rs.getString("id"),
							rs.getString("title"),
							rs.getString("description")
					));
				}
			}
		} catch (SQLException e) {
			for (Throwable t : e.getSuppressed()) t.printStackTrace();
			e.printStackTrace();
		}
		return sections;
	}
	
	private Section loadSection(String chapterId, String sectionId) {
		Section section = null;
		try (Connection con = this.dataSource.getConnection();
			 PreparedStatement statement = con.prepareStatement(this.sectionSql)) {
			statement.setString(1, chapterId);
			statement.setString(2, sectionId);
			try (ResultSet rs = statement.executeQuery()) {
				if (rs.next()) {
					section = new Section(
							rs.getString("id"),
							rs.getString("title"),
							rs.getString("description"));
				}
			}
		} catch (SQLException e) {
			for (Throwable t : e.getSuppressed()) t.printStackTrace();
			e.printStackTrace();
		}
		return section;
	}
}
