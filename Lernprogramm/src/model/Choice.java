package model;

public class Choice {
	private String text;
	private int id;
	private int questionId;
	private boolean correct;
	
	public Choice(int id, int questionId, boolean correct, String text) {
		this.id = id;
		this.questionId = questionId;
		this.correct = correct;
		this.text = text;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}
}
