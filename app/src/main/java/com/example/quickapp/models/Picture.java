package com.example.quickapp.models;

public class Picture{
	private String thumbnail;
	private String large;
	private String medium;

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public void setLarge(String large) {
		this.large = large;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

	public String getThumbnail(){
		return thumbnail;
	}

	public String getLarge(){
		return large;
	}

	public String getMedium(){
		return medium;
	}
}
