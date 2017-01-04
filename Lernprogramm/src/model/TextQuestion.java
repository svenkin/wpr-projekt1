package model;

public class TextQuestion extends Question {

	private String keywords;

	public TextQuestion(String question, String keywords, int id) {
		super(question, id);
		this.keywords = keywords;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public TextQuestion(String question) {
		super(question);
		// TODO Auto-generated constructor stub
	}

}
