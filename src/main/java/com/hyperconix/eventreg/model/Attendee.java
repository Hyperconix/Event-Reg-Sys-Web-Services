package com.hyperconix.eventreg.model;

public class Attendee {
	private String uid;
	
	private String name;
	
	private String[] interests;

	public Attendee(String uid, String name, String[] interests) {
		this.uid = uid;
		this.name = name;
		this.interests = interests;
	}
	
	public String getUid() {
		return uid;
	}

	public String getName() {
		return name;
	}

	public String[] getInterests() {
		return interests;
	}
	
	
	

}
