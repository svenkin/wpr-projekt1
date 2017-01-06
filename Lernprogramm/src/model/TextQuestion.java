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
	}

	public boolean checkForKeywords(String words) {
		String[] cutWords = words.split(" ");
		System.out.println(words);
		int correct = 0;
		for (String string : cutWords) {
			System.out.println(string);
			if (keywords.contains(string))
				correct++;
		}
		return correct == cutWords.length;
	}

}
