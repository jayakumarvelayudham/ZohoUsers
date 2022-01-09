package com.example.quickapp.models;

public class Timezone{
	private String offset;
	private String description;

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOffset(){
		return offset;
	}

	public String getDescription(){
		return description;
	}
}
