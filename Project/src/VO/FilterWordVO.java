package VO;

public class FilterWordVO {
	private String filter_seq;
	private String filterword;
	public FilterWordVO() {
		super();
	}
	public FilterWordVO(String filter_seq, String filterword) {
		super();
		this.filter_seq = filter_seq;
		this.filterword = filterword;
	}
	public String getFilter_seq() {
		return filter_seq;
	}
	public void setFilter_seq(String filter_seq) {
		this.filter_seq = filter_seq;
	}
	public String getFilterword() {
		return filterword;
	}
	public void setFilterword(String filterword) {
		this.filterword = filterword;
	}
	@Override
	public String toString() {
		return "FilteringVO [filter_seq=" + filter_seq + ", filterword=" + filterword + "]";
	}
	
	
}
