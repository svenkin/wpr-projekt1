package controller;

import java.io.IOException;

import javax.json.Json;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns={
		"/app.html",
		"/chapters",
		"/sections",
		"/lessons",
		"/exam",
		"/user",
		"/continue"
})
public class AuthorizationFilter implements Filter {

    public void destroy() {
		//
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		if (httpRequest.getSession(false) == null) {
			if (httpRequest.getRequestURL().toString().endsWith("/app.html")) ((HttpServletResponse) response).sendRedirect("");
			else {
				((HttpServletResponse) response).setStatus(HttpServletResponse.SC_FORBIDDEN);
				Json.createWriter(response.getWriter()).write(Json.createObjectBuilder()
						.add("error", "not authorized")
						.build());
			}
		}
		else chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		//
	}
}
