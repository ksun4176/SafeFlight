package com.SafeFlight;

import org.json.simple.JSONObject;

public class UserAccount {
	private String email;
	private int rating;
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String state;
	private int zipcode;

	public UserAccount() {
		
	}
	
	public UserAccount(String email, int rating, String firstName, String lastName, String address, String city, String state, int zipcode) {
		this.email = email;
		this.rating = rating;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zipcode = zipcode;
	}
	
	public String getEmail() {
       return email;
    }
	
	public int getRating() {
		return rating;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getFullName() {
		return firstName + " " + lastName; 
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getCity() {
		return city;
	}
	
	public String getState() {
		return state;
	}
	
	public int getZipcode() {
		return zipcode;
	}
	
	public String getLongAddress() {
		return this.address + " " + this.city + " " + this.state + " " + this.zipcode;
	}

		
	 
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setState(String state) {
		this.state = state;
	}
	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}
	
	public String toString() {
		return String.format("Email: %s\nRating: %d\nName: %s\nAddress: %s", 
				this.email, this.rating, this.getFullName(), this.getLongAddress());
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("Email", email);
		json.put("Rating", rating);
		json.put("Name", this.getFullName());
		json.put("Address", this.getLongAddress());
		
		return json;
	}
}
