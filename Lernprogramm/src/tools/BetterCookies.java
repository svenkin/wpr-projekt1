package tools;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class BetterCookies {
	
	private HashMap<String, Cookie> betterCookies;
	
	public BetterCookies() {
		this(null);
	}
	
	public BetterCookies(Cookie[] badCookies) {
		if (badCookies != null) {
			this.betterCookies = new HashMap<>(badCookies.length);
			for (Cookie cookie : badCookies) {
				this.betterCookies.put(cookie.getName(), cookie);
			}
		} else {
			this.betterCookies = new HashMap<>();
		}
	}
	
	public void addCookie(Cookie cookie) {
		this.betterCookies.put(cookie.getName(), cookie);
	}
	
	public Cookie getCookie(String cookieName) {
		return this.betterCookies.get(cookieName);
	}
	
	public String getCookieValue(String cookieName) {
		Cookie cookie = this.betterCookies.get(cookieName);
		if (cookie != null) return cookie.getValue();
		else return null;
	}
	
	public Map<String, String> getCookieWithMultipleValues(String cookieName) {
		Cookie cookie = this.betterCookies.get(cookieName);
		if (cookie != null) {
			Map<String, String> cookieValues = new HashMap<>();
			for (String kvPairs : cookie.getValue().split("&")) {
				try {
					String[] kvPair = kvPairs.split("=");
					cookieValues.put(URLDecoder.decode(kvPair[0], "UTF-8"), URLDecoder.decode(kvPair[1], "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			return cookieValues;
		} else return null;
	}
	
	public void setCookieValue(String cookieName, String cookieValue) {
		Cookie cookie = this.betterCookies.get(cookieName);
		if (cookie != null) cookie.setValue(cookieValue);
	}
	
	public void setCookieWithMultipleValues(String cookieName, Map<String, String> cookieValues) {
		Cookie cookie = this.betterCookies.get(cookieName);
		if (cookie != null) {
			StringBuilder cookieValue = new StringBuilder();
			Iterator<Entry<String, String>> it = cookieValues.entrySet().iterator();
			Entry<String, String> entry = null;
			while (it.hasNext()) {
				try {
					entry = it.next();
					cookieValue
						.append(URLEncoder.encode(entry.getKey(), "UTF-8"))
						.append('=')
						.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
					if (it.hasNext()) cookieValue.append('&');
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			cookie.setValue(cookieValue.toString());
		}
	}
	
	public List<Cookie> getAllCookies() {
		return new ArrayList<>(this.betterCookies.values());
	}
	
	public void addAllCookiesToResponse(HttpServletResponse res) {
		for (Entry<String, Cookie> entry : this.betterCookies.entrySet()) {
			System.out.println("Set Cookie: "+entry.getValue());
			res.addCookie(entry.getValue());
		}
	}
}
