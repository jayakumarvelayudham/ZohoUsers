package com.example.quickapp.models;

import java.io.Serializable;

public class ResultsItem implements Serializable {
	private String nat;
	private String gender;
	private String phone;
	private Dob dob;
	private Name name;
	private Registered registered;
	private Location location;
	private Id id;
	private Login login;
	private String cell;
	private String email;
	private Picture picture;

	public void setNat(String nat) {
		this.nat = nat;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setDob(Dob dob) {
		this.dob = dob;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public void setRegistered(Registered registered) {
		this.registered = registered;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
	}

	public String getNat(){
		return nat;
	}

	public String getGender(){
		return gender;
	}

	public String getPhone(){
		return phone;
	}

	public Dob getDob(){
		return dob;
	}

	public Name getName(){
		return name;
	}

	public Registered getRegistered(){
		return registered;
	}

	public Location getLocation(){
		return location;
	}

	public Id getId(){
		return id;
	}

	public Login getLogin(){
		return login;
	}

	public String getCell(){
		return cell;
	}

	public String getEmail(){
		return email;
	}

	public Picture getPicture(){
		return picture;
	}
}
