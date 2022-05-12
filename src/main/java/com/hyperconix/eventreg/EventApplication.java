package com.hyperconix.eventreg;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.hyperconix.eventreg.model.Event;
import com.hyperconix.eventreg.service.EventService;

/**
 * This class is the entry point for
 * the Event Registration Web Service. This will
 * run the program as Spring Boot Application.
 * 
 * @author Luke S
 *
 */
@SpringBootApplication
public class EventApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventApplication.class, args);
	}
	
	/**
	 * This bean will initiate the internal storage
	 * by creating some starting events that can be used
	 * when the service is started.
	 * 
	 * @param es The event service interface
	 * @return Args which populate the internal storage with some starting Events
	 */
    @Bean
    public CommandLineRunner initDB(EventService es) {
        return (args) -> {
        	es.addEvent(new Event(100, "CyberSecurity all around", "Pathfoot Building", "2022-04-01", "9am", 3, 40));
        	es.addEvent(new Event(102, "Wearables!", "Cortrell Building", "2022-02-03", "10am", 3, 2));
        	es.addEvent(new Event(103, "Data Security Awareness", "Cortrell Building", "2022-08-20", "10am", 3, 2));
        };
    }
}


