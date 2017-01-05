package tools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import model.User;

public class AccessChecker {
	
	private static AccessChecker instance;
	
	private final String chaptersSql = "SELECT id FROM chapter ORDER BY `order`;";
	private final String sectionsSql = "SELECT id FROM section WHERE chapter_id = ? ORDER BY `order`;";
	
	private AccessChecker() {
		//
	}
	
	public static AccessChecker getInstance() {
		if (AccessChecker.instance == null) AccessChecker.instance = new AccessChecker();
		return AccessChecker.instance;
	}
	
	public boolean checkAccess(String requestedChapterId, User user, DataSource dataSource) {
		boolean access = false;
		try (Connection con = dataSource.getConnection();
			 Statement statement = con.createStatement();
			 ResultSet rs = statement.executeQuery(this.chaptersSql)) {
			while (rs.next()) {
				String id = rs.getString("id");
				if (id.equals(requestedChapterId)) {
					access = true;
					break;
				} else if (id.equals(user.getAccessChapterId())) break;
			}
		} catch (SQLException e) {
			for (Throwable t : e.getSuppressed()) t.printStackTrace();
			e.printStackTrace();
		}
		return access;
	}
	
	public boolean checkAccess(String requestedChapterId, String requestedSectionId, User user, DataSource dataSource) {
		boolean access = false;
		boolean chapterAccess = this.checkAccess(requestedChapterId, user, dataSource);
		if (chapterAccess) {
			try (Connection con = dataSource.getConnection();
				 PreparedStatement statement = con.prepareStatement(this.sectionsSql)) {
				statement.setString(1, requestedChapterId);
				try (ResultSet rs = statement.executeQuery()) {
					while (rs.next()) {
						String id = rs.getString("id");
						if (id.equals(requestedSectionId)) {
							access = true;
							break;
						} else if (id.equals(user.getAccessSectionId())) break;
					}
				}
			} catch (SQLException e) {
				for (Throwable t : e.getSuppressed()) t.printStackTrace();
				e.printStackTrace();
			}
		}
		return access;
	}
}
