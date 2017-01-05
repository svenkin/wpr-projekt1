package model;

import java.io.Serializable;

public class Lesson implements Serializable {
	private static final long serialVersionUID = 1L;

	private String title;
	private String textContent;
	private String image;
	private String imageDescription;
	
	public Lesson() {
		//
	}

	public Lesson(String title, String textContent, String image, String imageDescription) {
		this.title = title;
		this.textContent = textContent;
		this.image = image;
		this.imageDescription = imageDescription;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImageDescription() {
		return imageDescription;
	}

	public void setImageDescription(String imageDescription) {
		this.imageDescription = imageDescription;
	}
}
