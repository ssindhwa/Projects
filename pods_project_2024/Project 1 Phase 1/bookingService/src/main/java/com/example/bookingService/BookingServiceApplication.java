package com.example.bookingService;

import com.example.bookingService.Services.ShowService;
import com.example.bookingService.Services.TheatreService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookingServiceApplication implements CommandLineRunner {
	@Autowired
    private TheatreService theatreService;
    @Autowired
    private ShowService showService;
	public static void main(String[] args) {
		SpringApplication.run(BookingServiceApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception{
	     theatreService.populateTheatreFromCSV();
        showService.populateShowFromCSV();
	}
}
