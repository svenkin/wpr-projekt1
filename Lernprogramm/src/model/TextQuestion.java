package model;

public class TextQuestion extends Question {

	private String keywords;

	public TextQuestion(String question, String keywords, int id) {
		super(question, id);
		this.keywords = keywords.toLowerCase();
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords.toLowerCase();
	}

	public TextQuestion(String question) {
		super(question);
	}

	public boolean checkForKeywords(String words) {
		String[] cutWords = words.toLowerCase().split(" ");
		int correct = 0;
		for (String string : cutWords) {
			if (keywords.contains(string))
				correct++;
		}
		return correct == cutWords.length;
	}

}
