package com.example.quickapp.models;

public class Name{
	private String last;
	private String title;
	private String first;

	public void setLast(String last) {
		this.last = last;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getLast(){
		return last;
	}

	public String getTitle(){
		return title;
	}

	public String getFirst(){
		return first;
	}
}
