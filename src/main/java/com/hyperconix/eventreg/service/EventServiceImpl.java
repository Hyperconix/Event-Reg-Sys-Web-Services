package com.hyperconix.eventreg.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.hyperconix.eventreg.model.Attendee;
import com.hyperconix.eventreg.model.Event;

/**
 * This class is responsible for encapsulating
 * all the implementation logic for the Event 
 * Registration Service. This is tagged
 * as a Component so that it can be automatically
 * detected when the service starts.
 * 
 * @author Luke S
 *
 */
@Component
public class EventServiceImpl implements EventService {
	
	/**
	 * Map from integer to an Event, this is responsible for storing
	 */
	private Map<Integer, Event> db;
	
	/**
	 * This creates the state of the Event Service Implementation 
	 * and assigns memory for the internal storage.
	 */
	public EventServiceImpl() {
		db = new HashMap<>();
	}
	
	public void addEvent(Event event) {
		if(event != null) {
			event = new Event(event);
			
			db.put(event.getEventID(), event);
		}
	}
	
	public void updateEvent(Event event) {
		
		//In the case where we have an event, we want to hold onto the attendees
		if(hasEvent(event.getEventID())) {
			Event e = getEvent(event.getEventID());
			
			event.setAttendees(e.getAttendees());
		}
		
		db.put(event.getEventID(), event);
	}
	
	public void removeEvent(int eventID) {
		db.remove(eventID);
	}
	
	public Event getEvent(int eventID) {
		Event event = db.get(eventID);
		
		if(event == null) {
			return null;
		}
		
		event = new Event(event);
		
		return event;
	}

	public List<Event> getAllEvents() {
		ArrayList<Event> allEvents = new ArrayList<>();
		
		db.values().forEach(event -> { 
			   allEvents.add(new Event(event));
			});
		
		return allEvents;
	}
	
	public List<Event> getAttendeesEvents(String userID) {
		
		List<Event> userEvents = new ArrayList<>();
		
		db.values().forEach(event -> {
			event.getAttendees().forEach(attendee -> {
				if(attendee.getUid().equals(userID)) {
					userEvents.add(event);
				}
			});
		});
		
		return userEvents;
	}
	
	public boolean capacityExceeded(int eventID) {
		Event event = db.get(eventID);
		
		if(event == null) {
			return false;
		}
		
		return event.getCapacity() < event.getAttending() + 1;
	}
	
	public boolean hasAttendeeRegistered(int eventID, String userID) {		
		Event event = db.get(eventID);
		
		if(event == null) return false;
		
		List<Attendee> attendees = new ArrayList<>(List.copyOf(event.getAttendees()));
		
		for(Attendee attendee : attendees) {
			String currentUserID = attendee.getUid();
			
			if(userID.equals(currentUserID)) {
				return true;
			}
		}
		
		return false;
		
	}

	public void registerAttendee(int eventID, Attendee attendee) {
		Event event = db.get(eventID);
		
		if(event != null) {
			List<Attendee> attendees = new ArrayList<>(List.copyOf(event.getAttendees()));
			
			attendees.add(attendee);
			
			event.setAttendees(attendees);
		}
		
		
	}

	public void cancelAttendee(int eventID, String userID) {
		Event event = db.get(eventID);
		
		if(event != null) {
			List<Attendee> attendees = new ArrayList<>(List.copyOf(event.getAttendees()));
			
			boolean attendeeRemoved = attendees.removeIf(attendee -> attendee.getUid().equals(userID));
			
			if(attendeeRemoved) {
				event.setAttendees(attendees);
			}
			
		}
	}
	
	public void updateAttendee(int eventID, Attendee attendee) {
		Event event = db.get(eventID);
		
		if(event == null) {
			return;
		}
		
		List<Attendee> attendees = new ArrayList<>(List.copyOf(event.getAttendees()));
		
		for(int i = 0; i < attendees.size(); i++) {
			Attendee currentAttendee = attendees.get(i);
			
			if(currentAttendee.getUid().equals(attendee.getUid())) {
				attendees.set(i, currentAttendee);
				break;
			}
		}
		
	}

	public List<Attendee> getRegisteredAttendees(int eventID) {
		Event event = db.get(eventID);
		
		if(event == null) {
			return new ArrayList<Attendee>();
		}
		
		return event.getAttendees();
	}

}
