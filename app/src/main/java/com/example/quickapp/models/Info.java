package com.example.quickapp.models;

public class Info{
	private String seed;
	private int page;
	private int results;
	private String version;

	public String getSeed(){
		return seed;
	}

	public int getPage(){
		return page;
	}

	public int getResults(){
		return results;
	}

	public String getVersion(){
		return version;
	}
}
