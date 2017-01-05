package model;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class Exam implements Serializable {

	private static final long serialVersionUID = 1L;

	private String description;
	private int sectionId;
	private Vector<Question> questions;
	private Hashtable<Integer, Question> premappedQuestions;
	
	public Exam(){}
	
	public Exam(String description, int sectionId) {
		this.description = description;
		this.sectionId = sectionId;
	}

	public Vector<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(Vector<Question> questions) {
		this.questions = questions;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getSectionId() {
		return sectionId;
	}

	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}

	public Hashtable<Integer, Question> getPremappedQuestions() {
		return premappedQuestions;
	}

	public void setPremappedQuestions(Hashtable<Integer, Question> premappedQuestions) {
		this.premappedQuestions = premappedQuestions;
	}
	
	
}
