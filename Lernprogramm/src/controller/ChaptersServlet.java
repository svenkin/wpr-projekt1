package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

import model.Chapter;

@WebServlet("/chapters")
public class ChaptersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Resource
	private DataSource dataSource;
	private String chaptersSql = "SELECT id, title, banner, description FROM chapter;";
	private String chapterSql = "SELECT id, title, banner, description FROM chapter WHERE id = ?;";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String chapterId = request.getParameter("chapter-id");
		JsonStructure body = null;
		if (chapterId != null && !chapterId.isEmpty()) {
			body = this.getChapter(chapterId);
		} else {
			body = this.getChapters();
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		Json.createWriter(response.getWriter()).write(body);
	}

	private JsonStructure getChapters() {
		JsonStructure structure = null;
		List<Chapter> chapters = this.loadChapters();
		if (chapters != null) {
			JsonArrayBuilder data = Json.createArrayBuilder();
			for (Chapter c : chapters) {
				data.add(Json.createObjectBuilder()
						.add("id", c.getId())
						.add("title", c.getTitle())
						.add("banner", c.getBanner())
						.add("description", c.getDescription()));
			}
			structure = Json.createObjectBuilder().add("data", data).build();
		}
		return structure;
	}
	
	private JsonStructure getChapter(String chapterId) {
		JsonStructure structure = null;
		Chapter chapter = this.loadChapter(chapterId);
		if (chapter != null) {
			structure = Json.createObjectBuilder()
					.add("data", Json.createObjectBuilder()
							.add("id", chapter.getId())
							.add("title", chapter.getTitle())
							.add("banner", chapter.getBanner())
							.add("description", chapter.getDescription()))
					.build();
		}
		return structure;
	}
	
	private List<Chapter> loadChapters() {
		List<Chapter> chapters = null;
		try (Connection con = this.dataSource.getConnection();
			 Statement statement = con.createStatement();
			 ResultSet rs = statement.executeQuery(this.chaptersSql)) {
			chapters = new ArrayList<>();
			while (rs.next()) {
				chapters.add(new Chapter(
						rs.getString("id"),
						rs.getString("title"),
						rs.getString("banner"),
						rs.getString("description")
				));
			}
		} catch (SQLException e) {
			for (Throwable t : e.getSuppressed()) t.printStackTrace();
			e.printStackTrace();
		}
		return chapters;
	}
	
	private Chapter loadChapter(String chapterId) {
		Chapter chapter = null;
		try (Connection con = this.dataSource.getConnection();
			 PreparedStatement statement = con.prepareStatement(this.chapterSql)) {
			statement.setString(1, chapterId);
			try (ResultSet rs = statement.executeQuery()) {
				if (rs.next()) {
					chapter = new Chapter(
							rs.getString("id"),
							rs.getString("title"),
							rs.getString("banner"),
							rs.getString("description"));
				}
			}
		} catch (SQLException e) {
			for (Throwable t : e.getSuppressed()) t.printStackTrace();
			e.printStackTrace();
		}
		return chapter;
	}
}
