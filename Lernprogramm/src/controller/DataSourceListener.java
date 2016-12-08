package controller;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

@WebListener
public class DataSourceListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		//
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext app = sce.getServletContext();
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setServerName(app.getInitParameter("mysql.serverName"));
		dataSource.setUser(app.getInitParameter("mysql.user"));
		dataSource.setPassword(app.getInitParameter("mysql.password"));
		dataSource.setDatabaseName(app.getInitParameter("mysql.databaseName"));
		app.setAttribute("dataSource", dataSource);
	}
}
