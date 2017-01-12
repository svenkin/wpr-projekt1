package model;

import java.util.Vector;

public class ChoicesQuestion extends Question {
	private Vector<Choice> choices;
	private int correctCount = 0;

	public ChoicesQuestion(String question) {
		super(question);
		choices = new Vector<>();
	}

	public Vector<Choice> getChoices() {
		return choices;
	}

	public void setChoices(Vector<Choice> choices) {
		for (Choice choice : choices) {
			if (choice.isCorrect())
				correctCount++;
		}
		this.choices = choices;
	}

	public boolean addChoice(Choice choice) {
		if (choice.isCorrect())
			correctCount++;
		return choices.add(choice);
	}

	public boolean checkIfChoiceIsCorrect(int choiceId) {
		boolean correct = false;
		for (Choice choice : choices) {
			if (choice.isCorrect() && choiceId == choice.getId())
				correct = true;
		}
		return correct;
	}

	public boolean checkIfChoicesAreCorrect(int[] choiceId) {
		int correctAnswered = 0;
		boolean falseAnswer = false;
		for (Choice choice : choices) {
			for (int i = 0; i < choiceId.length; i++) {
				if (choiceId[i] == choice.getId() && choice.isCorrect()) {
					correctAnswered++;
				} else if (choiceId[i] == choice.getId()) {
					falseAnswer = true;
				}
			}
		}
		return correctAnswered == this.correctCount && !falseAnswer;
	}
}
