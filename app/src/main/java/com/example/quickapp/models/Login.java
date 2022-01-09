package com.example.quickapp.models;

public class Login{
	private String sha1;
	private String password;
	private String salt;
	private String sha256;
	private String uuid;
	private String username;
	private String md5;

	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public void setSha256(String sha256) {
		this.sha256 = sha256;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getSha1(){
		return sha1;
	}

	public String getPassword(){
		return password;
	}

	public String getSalt(){
		return salt;
	}

	public String getSha256(){
		return sha256;
	}

	public String getUuid(){
		return uuid;
	}

	public String getUsername(){
		return username;
	}

	public String getMd5(){
		return md5;
	}
}
