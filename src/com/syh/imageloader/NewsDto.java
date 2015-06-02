package com.syh.imageloader;

public class NewsDto {

	private String imageUrl;
	private String title;
	private String content;

	public NewsDto() {

	}

	public NewsDto(String imageUrl, String title, String content) {
		super();
		this.imageUrl = imageUrl;
		this.title = title;
		this.content = content;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
