package com.example.quickapp.models;

public class Coordinates{
	private String latitude;
	private String longitude;

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude(){
		return latitude;
	}

	public String getLongitude(){
		return longitude;
	}
}
