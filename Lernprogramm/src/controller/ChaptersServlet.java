package controller;

import java.io.IOException;
import java.sql.Connection;
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
	private String chapterSql = "SELECT id, title, description FROM chapter;";

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");
		Json.createWriter(res.getWriter()).write(this.getChapters());
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
						.add("description", c.getDescription()));
			}
			structure = Json.createObjectBuilder().add("data", data).build();
		}
		return structure;
	}
	
	private List<Chapter> loadChapters() {
		List<Chapter> chapters = null;
		try (Connection con = this.dataSource.getConnection();
			 Statement statement = con.createStatement();
			 ResultSet rs = statement.executeQuery(this.chapterSql)) {
			chapters = new ArrayList<>();
			while (rs.next()) {
				chapters.add(new Chapter(rs.getString("id"), rs.getString("title"), rs.getString("description")));
			}
		} catch (SQLException e) {
			for (Throwable t : e.getSuppressed()) t.printStackTrace();
			e.printStackTrace();
		}
		return chapters;
	}
}
