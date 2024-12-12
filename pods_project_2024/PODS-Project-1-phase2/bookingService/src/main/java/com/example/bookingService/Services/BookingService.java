package com.example.bookingService.Services;

import com.example.bookingService.Entity.*;
import com.example.bookingService.Repository.BookingRepository;
import com.example.bookingService.Repository.ShowRepository;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Transactional(isolation = Isolation.SERIALIZABLE)
@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private  ShowService showService;

    @Autowired
    private ShowRepository showRepository;

        //get bookings by user id
    public List<BookingDTO> getBookingByUserId(Integer user_id){
        List<BookingDTO> bookings =new ArrayList<>();
        HashMap<String, Integer> params = new HashMap<>();
        params.put("userId", user_id);
        try {
            //request to user to check if user exists else 404
            ResponseEntity<ServiceUser> response
                    = restTemplate.getForEntity(
                    "http://anushka-userservice:8080/users/{userId}",
                    ServiceUser.class, params);

            if (response.getBody()!=null) {
                //if user exists check bookings if not 404
                ServiceUser mappedUser = response.getBody();
                Integer mappedId = mappedUser.getId();
                List<Booking> bookings1= bookingRepository.findByUserId(mappedId);
                return bookings1.stream()
                        .map(booking -> new BookingDTO(mappedId,booking.getSeatsBooked(),booking.getUserId()))
                        .toList();

            }
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return bookings;
    }
    @Transactional
    //create new booking
    public ResponseEntity<?> createBooking(BookingDTO booking) {
        Integer showId = booking.getShow_id();
        Integer userID = booking.getUser_id();
        //get show from repo
        Show shows1= showRepository.findById(showId).orElse(null);
        if(shows1==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        HashMap<String, Integer> params = new HashMap<>();
        params.put("userId", userID);
        try {
            //check is user is valid else 404
            ResponseEntity<ServiceUser> response
                    = restTemplate.getForEntity(
                    "http://anushka-userservice:8080/users/{userId}",
                    ServiceUser.class, params);
            if (response.hasBody() && shows1 != null) {
                ShowDTO show = new ShowDTO(shows1.getId(),shows1.getTheatreId(),shows1.getTitle(),shows1.getPrice(),shows1.getSeatsAvailable());
                //if user and show both are valid then check seats
                Integer seatsBooked = booking.getSeats_booked();
                Integer availSeats = show.getSeats_available();
                int remainSeats = availSeats-seatsBooked;
                if(remainSeats>0) {
                    try{
                        //if sufficient seats are there then check for wallet
                        Integer showPrice=show.getPrice();
                        int finalPrice = showPrice*seatsBooked;
                        String debitUrl = "http://sakshi-walletservice:8082/wallets/{userID}";
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        String requestBody = "{\"action\":\"" + "debit" + "\",\"amount\":" + finalPrice + "}";
                        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
                        ResponseEntity<String> response1 = restTemplate.exchange(
                                debitUrl,
                                HttpMethod.PUT,
                                requestEntity,
                                String.class,
                                userID
                        );
                        //if wallet has money then book seats
                        show.setSeats_available(remainSeats);
                        showRepository.updateShowSeats(remainSeats,showId);
                        Booking booking1 = new Booking(booking.getSeats_booked(), booking.getUser_id(),
                                new Show(show.getId(),show.getTheatre_id(),show.getTitle(),show.getPrice(),show.getSeats_available()));
                        bookingRepository.save(booking1);
                        return ResponseEntity.status(HttpStatus.OK)
                                .body(new BookingDTO(booking1.getShowId(),booking1.getSeatsBooked(),booking1.getUserId()));
                    }
                    catch (Exception ex){
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet" + ex+
                                " not found");

                    }
                }
            }
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entity not found" + ex);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @Transactional
    //delete booking of a user for a show
    public ResponseEntity<?> deleteBookingUserShow(Integer user_id,Integer show_id){
        ShowDTO show = showService.getShow(show_id);

        HashMap<String, Integer> params = new HashMap<>();
        params.put("userId", user_id);
        try {
            ResponseEntity<ServiceUser> response
                    = restTemplate.getForEntity(
                    "http://anushka-userservice:8080/users/{userId}",
                    ServiceUser.class, params);
            List<Booking> bookingList =bookingRepository.getBookingByShowUser(user_id,show_id);
            if (response.hasBody() && show != null && !bookingList.isEmpty()) {
            //if user,show and booking exits then revert money to wallet and refill seats

                try{
                    int finalPrice=0;
                    Integer numSeats=0;
                    for(Booking it:bookingList){
                        numSeats+=it.getSeatsBooked();
                    }
                    Integer showPrice=show.getPrice();
                    finalPrice+= showPrice * numSeats;
                    int finalSeats = numSeats + show.getSeats_available();
                    String debitUrl = "http://sakshi-walletservice:8082/wallets/{user_id}";
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    String requestBody = "{\"action\":\"" + "credit" + "\",\"amount\":" + finalPrice + "}";
                    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
                    ResponseEntity<String> response1 = restTemplate.exchange(
                            debitUrl,
                            HttpMethod.PUT,
                            requestEntity,
                            String.class,
                            user_id
                    );
                    bookingRepository.deleteBookingByShowUser(user_id,show_id);
                    showRepository.updateShowSeats(finalSeats,show_id);
                    return ResponseEntity.status(HttpStatus.OK).build();
                }
                catch (Exception ex){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet" +
                            " not found");
                }
            }
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @Transactional
    //delete all bookings of a user
    public ResponseEntity<?> deleteUserBookings(Integer user_id) {
        HashMap<String, Integer> params = new HashMap<>();
        params.put("userId", user_id);
        try {
            //first check if user exists
            ResponseEntity<ServiceUser> response
                    = restTemplate.getForEntity(
                    "http://anushka-userservice:8080/users/{userId}",
                    ServiceUser.class, params);
            if (response.getBody()!=null) {
                ServiceUser mappedUser = response.getBody();
                Integer mappedId = mappedUser.getId();
                List<Booking> bookings =bookingRepository.findByUserId(mappedId);
                if(!bookings.isEmpty()) {
                    //map shows by their price paid by user in shows prices for user
                    //map shows their seats booked in shows_for_user
                    bookingRepository.deleteShowByUserId(mappedId);
                         try{
                                List<Booking> mybookings= bookings;
                                Integer final_price=0;
                                HashMap<Integer,Integer> shows_for_user=new HashMap<>();
                                HashMap<Integer,Integer> showsprices_for_user=new HashMap<>();
                                for(Booking it:mybookings){
                                    if(shows_for_user.containsKey(it.getShowId())){
                                        Integer temp=it.getSeatsBooked()+shows_for_user.get(it.getShowId());
                                        shows_for_user.put(it.getShowId(),temp);
                                    }
                                    else{
                                        shows_for_user.put(it.getShowId(),it.getSeatsBooked());
                                        ShowDTO show = showService.getShow(it.getShowId());
                                        Integer showprice=show.getPrice();
                                        showsprices_for_user.put(it.getShowId(),showprice);
                                    }
                                }
                                //map show with amount paid by the user in result
                                Map<Integer, Integer> result = new HashMap<>();
                                for (Map.Entry<Integer, Integer> entry : shows_for_user.entrySet()) {
                                    Integer key = entry.getKey();
                                    Integer value1 = entry.getValue();
                                    if (showsprices_for_user.containsKey(key)) {
                                        Integer value2 = showsprices_for_user.get(key);
                                        Integer multipliedValue = value1 * value2;
                                        result.put(key, multipliedValue);
                                    }
                                    //refill the seats
                                    ShowDTO show = showService.getShow(key);
                                    int newSeats = value1+show.getSeats_available();
                                    show.setSeats_available(newSeats);
                                    showRepository.updateShowSeats(newSeats,key);
                                }
                
                                for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
                                    final_price+= entry.getValue();
                                }
                                //revert the amount
                                String debitUrl = "http://sakshi-walletservice:8082/wallets/{user_id}";
                                HttpHeaders headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                String requestBody = "{\"action\":\"" + "credit" + "\",\"amount\":" + final_price + "}";
                                HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
                                ResponseEntity<String> myresponse = restTemplate.exchange(
                                        debitUrl,
                                        HttpMethod.PUT,
                                        requestEntity,
                                        String.class,
                                        user_id
                                );

                                return ResponseEntity.status(HttpStatus.OK).build();
                            }
                            catch (Exception ex){
                                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet" +
                                        " not found");
                            }
                }
                else{
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
            }
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return null;
    }
    @Transactional
    //do the same process as above but now run for every user
   public ResponseEntity<?> deleteBookings() {
        //implement wallet logic
        List<Booking> mybookings =bookingRepository.findAll();
        Map<Integer,List<Booking>> userbooking=new HashMap<>();
        
        //HashMap<Integer,Integer> showsprices_for_user=new HashMap<>();
        for(Booking it:mybookings){
            if(userbooking.containsKey(it.getUserId()))  continue;
            else{
                List<Booking> mylist=bookingRepository.findByUserId(it.getUserId());
                userbooking.put(it.getUserId(),mylist);
            }
        }
        for (Map.Entry<Integer, List<Booking>> entry : userbooking.entrySet()) {
            Integer key = entry.getKey();
            List<Booking> value = entry.getValue();

            // System.out.println("Key: " + key);
            HashMap<Integer, Integer> shows_for_user = new HashMap<>();
            HashMap<Integer, Integer> showsprices_for_user = new HashMap<>();
            // Iterate over each element of the List
            for (Booking it : value) {
                if (shows_for_user.containsKey(it.getShowId())) {
                    Integer temp = it.getSeatsBooked() + shows_for_user.get(it.getShowId());
                    shows_for_user.put(it.getShowId(), temp);

                } else {
                    shows_for_user.put(it.getShowId(), it.getSeatsBooked());
                    ShowDTO show = showService.getShow(it.getShowId());
                    Integer showprice = show.getPrice();
                    showsprices_for_user.put(it.getShowId(), showprice);
                }
            }
            Map<Integer, Integer> result = new HashMap<>();
            for (Map.Entry<Integer, Integer> entry1 : shows_for_user.entrySet()) {
                Integer key1 = entry1.getKey();
                Integer value1 = entry1.getValue();
                if (showsprices_for_user.containsKey(key1)) {
                    Integer value2 = showsprices_for_user.get(key1);
                    Integer multipliedValue = value1 * value2;
                    result.put(key1, multipliedValue);
                }

            }
            int final_price = 0;
            for (Map.Entry<Integer, Integer> entry2 : result.entrySet()) {
                final_price += entry2.getValue();
            }
            try {
                String debitUrl = "http://sakshi-walletservice:8082/wallets/{key}";// + "/" + userID + "/debit";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                String requestBody = "{\"action\":\"" + "credit" + "\",\"amount\":" + final_price + "}";
                HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
                ResponseEntity<String> myresponse = restTemplate.exchange(
                        debitUrl, // URL endpoint
                        HttpMethod.PUT, // HTTP method
                        requestEntity, // Request entity
                        String.class,
                        key// Response entity type
                );
            }catch (Exception ex){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet" +
                        " not found");
            }

        }
        showService.populateShowFromCSV();
        bookingRepository.deleteAll();
        return ResponseEntity.ok().build();
    }


}
