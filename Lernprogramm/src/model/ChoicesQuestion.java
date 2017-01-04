package model;

import java.util.Vector;

public class ChoicesQuestion extends Question {
	private Vector<Choice> choices;

	public ChoicesQuestion(String question) {
		super(question);
		choices = new Vector<>();
	}

	public Vector<Choice> getChoices() {
		return choices;
	}

	public void setChoices(Vector<Choice> choices) {
		this.choices = choices;
	}

	public boolean addChoice(Choice choice) {
		return choices.add(choice);
	}
}
