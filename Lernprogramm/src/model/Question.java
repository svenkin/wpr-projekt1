package model;

public class Question {
	private String question;
	private QuestionType type;
	private int id;

	public Question(String question) {
		this.question = question;
	}

	public Question(String question, int id) {
		this.question = question;
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public QuestionType getType() {
		return type;
	}

	public void setType(QuestionType type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
