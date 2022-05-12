package com.hyperconix.eventreg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hyperconix.eventreg.model.Attendee;
import com.hyperconix.eventreg.model.Event;
import com.hyperconix.eventreg.service.EventService;


/**
 * This class represents the Rest Event Controller
 * which will be used for the Event Registration
 * Service.
 * 
 * @author Luke S
 *
 */
@RestController
@CrossOrigin
public class EventController {
	
	/**
	 * This is responsible for storing the EventService interface, to be used by the controller.
	 */
	private EventService es;
	
	/**
	 * This is responsible for storing a representation of the Environment interface, 
	 * to be used for accessing property files. This is Autowired so it will
	 * be injected when the application is started.
	 */
	@Autowired
	private Environment env;
	
	/**
	 * This creates the state of the EventController which will be used
	 * for the Event Registration Service. The event service interface must be 
	 * provided to the constructor so it can be automatically configured
	 * and injected when the application is started.
	 * 
	 * @param es The event service
	 */
	public EventController(EventService es) {
		this.es = es;
	}
	
	/**
	 * A get request to the Event Registration Service which retrieves all of the events which are 
	 * currently stored in the system.
	 * 
	 * @return ResponseEntity A response entity with a okay status, containing all of the events
	 */
	@GetMapping("/eventreg/events/allEvents")
	public ResponseEntity<List<Event>> getAllEvents() {
		List<Event> allEvents = es.getAllEvents();
		
		System.out.println("GET /eventreg/events/allEvents => 200 OK");
		
		return ResponseEntity.ok(allEvents);
	}
	
	/**
	 * A post request to the Event Registration Service, which will post an Event
	 * to the system. 
	 * 
	 * @param event The Event to be posted, which is bound to the Request Body
	 * 
	 * @return ResponseEntity A response entity with a created status
	 * 
	 * @throws ResponseStatusException(HttpStatus.UNAUTHORIZED) If the api key contained in the request body is incorrect
	 * @throws ResponseStatusException(HttpStatus.BAD_REQUEST) If the event already exists
	 */
	@PostMapping("/eventreg/events/addEvent")
	public ResponseEntity<String> postEvent(@RequestBody Event event) {
		
		if(!event.getApikey().equals(env.getProperty("apikey"))) {
			
			System.out.println("POST /eventreg/events/addEvent => 401 UNAUTHORIZED");
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		
		boolean eventExists = es.hasEvent(event.getEventID());
		
		if(eventExists) {
			System.out.println("POST /eventreg/events/addEvent => 400 BAD REQUEST: Event Already Exists");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		
		es.addEvent(event);
		
		System.out.println("POST /eventreg/events/addEvent => 201 CREATED");
		
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}
	
	/**
	 * A get request to the Event Registration Service which will
	 * get all registered events for the specified attendee using
	 * their ID.
	 * 
	 * @param attendeeID The ID of the attendee
	 * 
	 * @return ResponseEntity A ResponseEntity with an ok status and the registered events
	 */
	@GetMapping("/eventreg/events/registeredEvents/{attendeeID}")
	public ResponseEntity<List<Event>> getAllEventsForAttendee(@PathVariable String attendeeID) {
		
		System.out.printf("GET /eventreg/events/registeredEvents/%s => 200 OK%n", attendeeID);
		
		return ResponseEntity.ok(es.getAttendeesEvents(attendeeID));
	}
	
	/**
	 * A get request to the Event Registration Service which will
	 * get all registered attendees for the specified event.
	 * 
	 * @param eventID The ID of the event
	 * 
	 * @return ResponseStatus A ResponseStatus with an ok status and the registered attendees
	 * 
	 * @throws ResponseStatusException(HttpStatus.NOT_FOUND) If the event cannot be found
	 */
	@GetMapping("/eventreg/attendees/registeredAttendees/{eventID}")
	public ResponseEntity<List<Attendee>> getRegisteredAttendees(@PathVariable int eventID) {
	    Event event = es.getEvent(eventID);
		
		if(event == null) {
			System.out.printf("GET /eventreg/attendees/registeredAttendees/%d => 404 NOT FOUND: Event Not Found%n", eventID);
			
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		System.out.printf("GET /eventreg/attendees/registeredAttendees/%d => 200 OK%n", eventID);
		
		return ResponseEntity.ok(es.getRegisteredAttendees(eventID));
	}
	
	/**
	 * A delete request to the Event Registration Service which will
	 * delete the specified event.
	 * 
	 * @param eventID The ID of the event
	 * @param key The api key which is bound to the Request Body
	 * 
	 * @return ResponseStatus A ResponseStatus with a no content status
	 * 
	 * @throws ResponseStatusException(HttpStatus.UNAUTHORIZED) If the api key is incorrect
	 * @throws ResponseStatusException(HttpStatus.NOT_FOUND) If the event cannot be found
	 */
	@DeleteMapping("/eventreg/events/removeEvent/{eventID}")
	public ResponseEntity<?> deleteEvent(@PathVariable int eventID, @RequestBody String key) {
		
		if(!key.equals(env.getProperty("apikey"))) {
			System.out.printf("DELETE /eventreg/events/removeEvent/%d => 401 UNAUTHORIZED%n", eventID);
			
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		
		if(!es.hasEvent(eventID)) {
			System.out.printf("DELETE /eventreg/events/removeEvent/%d => 404 NOT FOUND: Event not Found%n", eventID);
			
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		es.removeEvent(eventID);
		
		System.out.printf("DELETE /eventreg/events/removeEvent/%d => 204 NO CONTENT: Event Removed%n", eventID);
		
		return ResponseEntity.noContent().build();
		
	}
	
	/**
	 * A post request to the Event Registration Service, which will
	 * post an attendee to the system. More specifically this is
	 * the request responsible for registering an attendee to an event.
	 * 
	 * @param attendee The Attendee to be posted which is bound to the Request Body
	 * @param eventID The ID of the event
	 * 
	 * @return ResponseEntity A ResponseEntity with a created status
	 * 
	 * @throws ResponseStatusException(HttpStatus.NOT_FOUND) If the event cannot be found
	 * @throws ResponseStatusException(HttpStatus.BAD_REQUEST) If the attendee is already registered
	 * @throws ResponseStatusException(HttpStatus.BAD_REQUEST) If the event does not have enough space
	 */
	@PostMapping("/eventreg/attendees/registerAttendee/{eventID}")
	public ResponseEntity<String> postAttendee(@RequestBody Attendee attendee, @PathVariable int eventID) {
		Event event = es.getEvent(eventID);
		
		if(event == null) {
			System.out.printf("POST /eventreg/attendees/registerAttendee/%d => 404 NOT FOUND: Event Not Found%n", eventID);
			
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			
		}
		
		boolean isRegisteredAlready = es.hasAttendeeRegistered(eventID, attendee.getUid());
		
		boolean capacityExceeded = es.capacityExceeded(eventID);
		
		if(isRegisteredAlready) {
			System.out.printf("POST /eventreg/attendees/registerAttendee/%d => 400 BAD REQUEST: Attendee Registered Already%n", eventID);
			
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}	
		else if(capacityExceeded) {
			System.out.printf("POST /eventreg/attendees/registerAttendee/%d => 400 BAD REQUEST: Event does not have enough space%n", eventID);
			
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		
		es.registerAttendee(eventID, attendee);
		
		System.out.printf("POST /eventreg/attendees/registerAttendee/%d => 201 CREATED%n", eventID);
		
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}
	
	/**
	 * A put request to the Event Registration Service
	 * which will update an attendee who is registered
	 * to the specified event.
	 * 
	 * @param attendee The attendee to be updated which is bound to the RequestBody
	 * @param eventID The ID of the event 
	 * 
	 * @return ResponseEntity A ResponseEntity with a created status
	 * 
	 * @throws ResponseStatusException(HttpStatus.NOT_FOUND) If the event cannot be found
	 */
	@PutMapping("/eventreg/attendees/updateAttendee/{eventID}")
	public ResponseEntity<String> putAttendee(@RequestBody Attendee attendee, @PathVariable int eventID) {
		Event event = es.getEvent(eventID);
		
		if(event == null) {
			System.out.printf("PUT /eventreg/attendees/updateAttendee/%d => 404 NOT FOUND: Event Not Found%n", eventID);
			
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		System.out.printf("PUT /eventreg/attendees/updateAttendee/%d => 201 CREATED%n", eventID);
		
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}
	
	/**
	 * A put request to the Event Registration Service
	 * which will update an event contained within the
	 * system.
	 * 
	 * @param event The Event which is bound to the RequestBody
	 * 
	 * @return ResponseEntity A ResponseEntity with a created status
	 * 
	 * @throws ResponseStatusException(HttpStatus.UNAUTHORIZED) If the api key is incorrect
	 */
	@PutMapping("/eventreg/events/updateEvent")
	public ResponseEntity<String> putEvent(@RequestBody Event event) {
		
		//Check if the caller provided the right api key
		if(!event.getApikey().equals(env.getProperty("apikey"))) {
			System.out.println("PUT /eventreg/events/updateEvent => 401 UNAUTHORIZED: Caller not authorized");
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		
		es.updateEvent(event);
		
		System.out.println("PUT /eventreg/events/updateEvent => 201 CREATED");
		
		return new ResponseEntity<String>(HttpStatus.CREATED);
		
	}
	
	/**
	 * A delete request to the Event Registration Service which
	 * will delete an attendee. More specifically this is the request
	 * responsible for cancelling or deregistering an attendee.
	 * 
	 * @param eventID The ID of the event
	 * @param attendeeID The ID of the attendee
	 * 
	 * @return ResponseEntity A ResponseEntity with a no content status
	 * 
	 * @throws ResponseStatusException(HttpStatus.NOT_FOUND) If the event cannot be found
	 * @throws ResponseStatusException(HttpStatus.BAD_REQUEST) If the attendee is not registered
	 */
	@DeleteMapping("/eventreg/attendees/deregisterAttendee/{eventID}/{attendeeID}")
	public ResponseEntity<?> deleteAttendee(@PathVariable int eventID, @PathVariable String attendeeID) {
		Event event = es.getEvent(eventID);
		
		//Check if there is a valid event to delete
		if(event == null) {
			System.out.printf("DELETE /eventreg/attendees/deregisterAttendee/%d/%s => 404 NOT FOUND: Event not Found%n", eventID, attendeeID);
			
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		if(es.hasAttendeeRegistered(eventID, attendeeID)) {
			//Cancel the attendee only if they are registered to the event
			es.cancelAttendee(eventID, attendeeID);
			
			System.out.printf("DELETE /eventreg/attendees/deregisterAttendee/%d/%s => 204 NO CONTENT: Event Cancelled%n", eventID, attendeeID);
			
			return ResponseEntity.noContent().build();
		}
		else {
			System.out.printf("DELETE /eventreg/attendees/deregisterAttendee/%d/%s => 400 BAD REQUEST: Attendee is not Registered%n", eventID, attendeeID);
			
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * A get request to the Event Service which retrieves details about a single event
	 * by its ID.
	 * 
	 * @param eventID The event id to be used in the path
	 * @return ResponseEntity A ResponseEntity with an ok status and the event.
	 * 
	 * @throws ResponseStatusException(HttpStatus.NOT_FOUND) If the event cannot be found
	 */
	@GetMapping("/eventreg/events/{eventID}")
	public ResponseEntity<Event> getEvent(@PathVariable int eventID) {
		Event event = es.getEvent(eventID);
		
		if(event == null) {
			System.out.printf("GET /eventreg/events/%d => 404 NOT FOUND%n", eventID);
			
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		System.out.printf("GET /eventreg/events/%d => 200 OKAY%n", eventID);
		
		return ResponseEntity.ok(event);
	}
	

}
