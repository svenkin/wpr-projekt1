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
	
	public boolean checkIfChoiceIsCorrect(int choiceId){
		boolean correct = false;
		for (Choice choice : choices) {
			if(choice.isCorrect() && choiceId == choice.getId()) correct = true;
		}
		return correct;
	}
	
	public boolean checkIfChoicesAreCorrect(int[] choiceId){
		int correctCount = 0;
		for (Choice choice : choices) {
			for (int i = 0; i < choiceId.length; i++) {
				if(choice.isCorrect() && choiceId[i] == choice.getId()) correctCount++;
			}
		}
		return correctCount==choiceId.length;
	}
}
