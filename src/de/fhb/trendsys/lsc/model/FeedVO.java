package de.fhb.trendsys.lsc.model;

public class FeedVO {

	private String url;
	private String title;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public FeedVO(String url, String title) {
		super();
		this.url = url;
		this.title = title;
	}
	
	
}
