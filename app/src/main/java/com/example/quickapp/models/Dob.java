package com.example.quickapp.models;

public class Dob{
	private String date;
	private int age;

	public void setDate(String date) {
		this.date = date;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getDate(){
		return date;
	}

	public int getAge(){
		return age;
	}
}
