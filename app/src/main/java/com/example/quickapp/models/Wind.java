package com.example.quickapp.models;

public class Wind{
	private int deg;
	private double speed;
	private double gust;

	public void setDeg(int deg){
		this.deg = deg;
	}

	public int getDeg(){
		return deg;
	}

	public void setSpeed(double speed){
		this.speed = speed;
	}

	public double getSpeed(){
		return speed;
	}

	public void setGust(double gust){
		this.gust = gust;
	}

	public double getGust(){
		return gust;
	}
}
