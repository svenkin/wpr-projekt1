package model;

import java.io.Serializable;

public class Chapter implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String title;
	private String banner;
	private String description;
	
	public Chapter() {
		//
	}
	
	public Chapter(String id, String title, String banner, String description) {
		this.id = id;
		this.title = title;
		this.banner = banner;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
