package VO;

public class MemberVO {
	private String member_seq;
	private String id;
	private String password;
	private String playnum;
	private String correctnum;
	private String winningRate;
	private String totalplaytime;
	private String indate;
	private String memo;
	public MemberVO() {
		super();
	}
	public String getUser_seq() {
		return member_seq;
	}
	public void setUser_seq(String user_seq) {
		this.member_seq = user_seq;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPlaynum() {
		return playnum;
	}
	public void setPlaynum(String playnum) {
		this.playnum = playnum;
	}
	public String getCorrectnum() {
		return correctnum;
	}
	public void setCorrectnum(String correctnum) {
		this.correctnum = correctnum;
	}
	public String getWinningRate() {
		return winningRate;
	}
	public void setWinningRate(String winningRate) {
		this.winningRate = winningRate;
	}
	public String getTotalplaytime() {
		return totalplaytime;
	}
	public void setTotalplaytime(String totalplaytime) {
		this.totalplaytime = totalplaytime;
	}
	public String getIndate() {
		return indate;
	}
	public void setIndate(String indate) {
		this.indate = indate;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	@Override
	public String toString() {
		return "개인정보는 ひ.み.つ !";
	}
	
	
}
