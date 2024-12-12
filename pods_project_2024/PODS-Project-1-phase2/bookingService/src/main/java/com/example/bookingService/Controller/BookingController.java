package com.example.bookingService.Controller;



import com.example.bookingService.Entity.BookingDTO;
import com.example.bookingService.Services.BookingService;

//import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Transactional
@RestController
@RequestMapping("/")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/bookings/users/{user_id}")
    public ResponseEntity<List<BookingDTO>> getBookings(@PathVariable Integer user_id){
        List<BookingDTO>bookings=  bookingService.getBookingByUserId(user_id);
        if(bookings==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(bookings);
    }

    @PostMapping("/bookings")
    public ResponseEntity<?> createBooking(@RequestBody BookingDTO booking)  {
        return bookingService.createBooking(booking);
    }



  @DeleteMapping("/bookings/users/{user_id}/shows/{show_id}")
  public ResponseEntity<?> deleteUserShowId(@PathVariable("user_id") Integer user_id,@PathVariable("show_id") Integer show_id) {
       return bookingService.deleteBookingUserShow(user_id,show_id);
    }

    @DeleteMapping("bookings/users/{user_id}")
    public ResponseEntity<?> deleteUserBookings(@PathVariable Integer user_id) {
      return bookingService.deleteUserBookings(user_id);
    }

    @DeleteMapping("/bookings")
    public ResponseEntity<?> deleteBookings() {
       return bookingService.deleteBookings();
    }
}
