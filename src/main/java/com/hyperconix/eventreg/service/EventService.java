package com.hyperconix.eventreg.service;

import java.util.List;

import com.hyperconix.eventreg.model.Attendee;
import com.hyperconix.eventreg.model.Event;

/**
 * 
 * This interface represents the contract
 * that the Event Registration Service will use.
 * This will be included in the Event Registrations controller
 * via constructor injection to allow for modularity.
 * 
 * @author Luke S
 *
 */
public interface EventService {
	
	/**
	 * This method is responsible for adding an Event
	 * to the Event Registration Services Internal Storage
	 * by using the provided Event object.
	 * 
	 * @param event The event to be added
	 */
	void addEvent(Event event);
	
	/**
	 * This method is responsible for performing an update
	 * operation to an Event by using the provided Event
	 * object. This will either create an entirely new 
	 * Event or update an existing one, if one exists.
	 * 
	 * @param event The event to be updated
	 */
	void updateEvent(Event event);
	
	/**
	 * This method is responsible for removing an event
	 * using the specified ID. If the event does not
	 * exist then no changes will be made.
	 * 
	 * @param eventID The ID of the event to be removed
	 */
	void removeEvent(int eventID);
	
	
	/**
	 * This method is responsible for determining
	 * whether an event with a specified ID exists
	 * in the Event Registration Services internal storage. 
	 * This will return true if an event can be found matching 
	 * the ID or false otherwise.
	 * 
	 * @param eventID The ID of the event
	 * @return {@code true} If the event exists, {@code false} otherwise
	 */
	default boolean hasEvent(int eventID) {
		return getEvent(eventID) != null;
	}
	
	/**
	 * This method is responsible for querying an Event
	 * by using the event ID. If this event exists then it 
	 * will be returned, otherwise null will be returned instead.
	 * 
	 * @param eventID The ID of the event to query
	 * 
	 * @return An Event object representing the event when matched the id number, 
	 *         {@code null} if the event does not exist
	 */
	Event getEvent(int eventID);
	
	/**
	 * This method is responsible for querying all
	 * Events which are currently stored and then
	 * returning those events as a list. If there
	 * are no Events currently stored, then this will
	 * return an empty list.
	 * 
	 * @return A list of all events which currently exist
	 */
	List<Event> getAllEvents();
	
	/**
	 * This method is responsible for querying all
	 * Events which an attendee is currently registered
	 * to. If the attendee is not registered to any events, or
	 * the attendee cannot be found this will return an empty
	 * list.
	 * 
	 * @param attendeeID The ID of the attendee
	 * @return
	 */
	List<Event> getAttendeesEvents(String attendeeID);
	
	/**
	 * This method is responsible for determining
	 * if the specified event by its ID has exceed
	 * its capacity for attendees. This will return
	 * true if the capacity has been exceeded, and
	 * false if there is still space in the event.
	 * 
	 * @param eventID The ID of the event
	 * 
	 * @return {@code true} If the capacity has been exceeded, {@code false} if there is still space
	 */
	boolean capacityExceeded(int eventID);
	
	
	/**
	 * This method is responsible for determining if
	 * an attendee with their specified ID is registered 
	 * for an event using the specified event ID. 
	 * This will return true if the attendee is registered 
	 * to the specified event, and false if the 
	 * attendee is not registered.
	 * 
	 * @param eventID The ID of the event
	 * @param attendeeID The attendee ID to check for registration
	 * 
	 * @return {@code true} If the attendee is already registered, 
	 *         {@code false} if there is no event or if the attendee is not registered
	 */
	boolean hasAttendeeRegistered(int eventID, String attendeeID);
	
	/**
	 * This method is responsible for registering an
	 * attendee to a specified event using its ID
	 * and the provided attendee object.
	 * 
	 * @param eventID The ID of the event
	 * @param attendee The attendee being registered to the event
	 */
	void registerAttendee(int eventID, Attendee attendee);
	
	/**
	 * This method is responsible for cancelling
	 * an attendees registration for an event using
	 * the IDs specified. If the attendee was
	 * not registered to the event, then no
	 * changes will be made.
	 * 
	 * @param eventID The ID of the event to cancel for
	 * @param attendeeID The ID of the attendee to cancel
	 */
	void cancelAttendee(int eventID, String attendeeID);
	
	/**
	 * This method is responsible for performing an update
	 * operation for an attendee who is registered to the
	 * specified using its ID.
	 * 
	 * @param eventID The ID of the event
	 * @param attendee The attendee to update
	 */
	void updateAttendee(int eventID, Attendee attendee);
	
	/**
	 * This method is responsible for querying
	 * all attendees who are currently registered
	 * to the specified event using its ID and
	 * returning those as a list. If there are no
	 * attendees registered to the event, this will
	 * return an empty list.
	 * 
	 * @param eventID The ID of the event
	 * 
	 * @return A list of registered attendees
	 */
	List<Attendee> getRegisteredAttendees(int eventID);
	
	
	
	
	
}
