package com.hyperconix.eventreg.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

/**
 * This class models an Event in the Event Registration Web Service.
 * 
 * An event contains details about its location, description, duration and
 * the number of attendees who are registered to an event.
 * 
 * @author Luke S
 *
 */
public class Event {
	
	/**
	 * Responsible for storing the id of the event
	 */
   private int eventID;
   
   /**
    * Responsible for storing the description of the event 
    */
   private String description;
   
   /**
    * Responsible for storing the location of the event 
    */
   private String location;
   
   /**
    * Responsible for storing the date of the event 
    */
   private String date;
   
   /**
    * Responsible for storing the time of the event 
    */
   private String time;
   
   /**
    * Responsible for storing the duration of the event 
    */
   private int duration;
   
   /**
    * Responsible for storing the capacity of the event 
    */
   private int capacity;
   
   /**
    * Responsible for storing the list of registered attendees 
    */
   @JsonProperty(access = Access.WRITE_ONLY)
   private List<Attendee> attendees;
   
   @JsonProperty(access = Access.WRITE_ONLY)
   private String apikey;
   

   /**
    * Creates the state of an Event in the system.
    * 
    * @param eventID The event id
    * @param description The description of the event
    * @param location The location of the event
    * @param date The date of the event
    * @param time The time of the event
    * @param duration The duration of the event
    * @param capacity The capacity of the event
    */
   public Event(int eventID, String description, String location, String date, String time, int duration,
		int capacity) {
		this.eventID = eventID;
		this.description = description;
		this.location = location;
		this.date = date;		
		this.time = time;
		this.duration = duration;
		this.capacity = capacity;
		
        attendees = new ArrayList<>();
   }
   
   /**
    * Copy Constructor 
    * @param that
    */
   public Event(Event that) {
	   this.eventID = that.eventID;
		this.description = that.description;
		this.location = that.location;
		this.date = that.date;		
		this.time = that.time;
		this.duration = that.duration;
		this.capacity = that.capacity;
		
		attendees = that.attendees;
   }

	public int getEventID() {
		return eventID;
	}

	public String getDescription() {
		return description;
	}
	
	public String getLocation() {
		return location;
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}

	public int getDuration() {
		return duration;
	}

	public int getCapacity() {
		return capacity;
	}
	
	public int getAttending() {
		return attendees.size();
	}
	
	public List<Attendee> getAttendees() {
		return attendees;
	}
	
	public String getApikey() {
		return apikey;
	}

	public void setAttendees(List<Attendee> attendees) {
		this.attendees = attendees;
	}
	

	@Override
	public String toString() {
		return "Event{" +
	            "eventID=\"" + eventID + "\"," +
	            "description=\"" + description + "\"}";
	}

   
   
   
   
}
