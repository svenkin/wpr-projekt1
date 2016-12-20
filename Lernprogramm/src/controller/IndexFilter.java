package controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/")
public class IndexFilter implements Filter {

	public void destroy() {
		//
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (((HttpServletRequest) request).getSession(false) != null) ((HttpServletResponse) response).sendRedirect("app.html");
		else chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		//
	}
}
