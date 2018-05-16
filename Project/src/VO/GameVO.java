package VO;

public class GameVO {
	private String game_seq;
	private String id;
	private String usertype;
	private String word;
	private String starttime;
	private String endtime;
	private String playtime;
	public GameVO() {
		super();
	}
	public GameVO(String game_seq, String id, String usertype, String word, String starttime, String endtime,
			String playtime) {
		super();
		this.game_seq = game_seq;
		this.id = id;
		this.usertype = usertype;
		this.word = word;
		this.starttime = starttime;
		this.endtime = endtime;
		this.playtime = playtime;
	}
	public String getGame_seq() {
		return game_seq;
	}
	public void setGame_seq(String game_seq) {
		this.game_seq = game_seq;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getPlaytime() {
		return playtime;
	}
	public void setPlaytime(String playtime) {
		this.playtime = playtime;
	}
	@Override
	public String toString() {
		return "GameVO [game_seq=" + game_seq + ", id=" + id + ", usertype=" + usertype + ", word=" + word
				+ ", starttime=" + starttime + ", endtime=" + endtime + ", playtime=" + playtime + "]";
	}
	
}
