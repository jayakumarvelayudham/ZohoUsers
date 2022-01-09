package com.example.quickapp.models;

public class Location{
	private String country;
	private String city;
	private Street street;
	private Timezone timezone;
	private String postcode;
	private Coordinates coordinates;
	private String state;

	public void setCountry(String country) {
		this.country = country;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setStreet(Street street) {
		this.street = street;
	}

	public void setTimezone(Timezone timezone) {
		this.timezone = timezone;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry(){
		return country;
	}

	public String getCity(){
		return city;
	}

	public Street getStreet(){
		return street;
	}

	public Timezone getTimezone(){
		return timezone;
	}

	public String getPostcode(){
		return postcode;
	}

	public Coordinates getCoordinates(){
		return coordinates;
	}

	public String getState(){
		return state;
	}
}
