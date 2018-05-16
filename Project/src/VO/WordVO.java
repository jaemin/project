package VO;

import java.io.Serializable;

public class WordVO implements Serializable{
	private String word_seq;
	private String word;
	public WordVO() {
		super();
	}
	public WordVO(String word_seq, String word) {
		super();
		this.word_seq = word_seq;
		this.word = word;
	}
	public String getWord_seq() {
		return word_seq;
	}
	public void setWord_seq(String word_seq) {
		this.word_seq = word_seq;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	@Override
	public String toString() {
		return "WordListVO [word_seq=" + word_seq + ", word=" + word + "]";
	}
	
	
}
