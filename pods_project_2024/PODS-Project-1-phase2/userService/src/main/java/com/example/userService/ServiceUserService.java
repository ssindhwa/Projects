package com.example.userService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ServiceUserService {
    @Autowired
    private ServiceUserRepository serviceUserRepository;

    @Autowired
    private RestTemplate restTemplate;

    //get all service users throw 404 if not found
    public List<ServiceUser> getServiceUsers(){
        return serviceUserRepository.findAll();
    }
    public ServiceUser getServiceUser(Integer id){
        return serviceUserRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));

    }

    //save user in db
    public void saveServiceUser(ServiceUser serviceUser){
        serviceUserRepository.save(serviceUser);
    }

    //get user by email
    public ServiceUser getUserByEmail(String email){
        return serviceUserRepository.findByEmail(email);
    }

    //delete user by id if no user then 404
    public ResponseEntity<?> deleteServiceUser(Integer id){
        ServiceUser serviceUser = serviceUserRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
        Integer userId = serviceUser.getId();
            serviceUserRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).build();

    }

    //delete user from db
    public ResponseEntity<?> deleteAllServiceUsers(){
        serviceUserRepository.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).build();

    }


    //send request to booking service to delete bookings by user id
    public ResponseEntity<?> deleteAllBookingsUser(Integer id){
        String url = "http://sakshi-bookingservice:8081/bookings/users/{id}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        if(serviceUserRepository.findById(id).isPresent()) {
            try {
                ResponseEntity<?> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String.class, id);
                HttpStatus status = (HttpStatus) responseEntity.getStatusCode();
                if (status == HttpStatus.OK) {
                    return ResponseEntity.status(HttpStatus.OK).build();
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found");
            }
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }
    }

    //send request to wallet to delete wallet by user id
    public ResponseEntity<?> deleteAllWalletsUsers(Integer id){
        String url = "http://sakshi-walletservice:8082/wallets/{id}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        if(serviceUserRepository.findById(id).isPresent()) {
            try {
                ResponseEntity<?> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String.class, id);
                HttpStatus status = (HttpStatus) responseEntity.getStatusCode();
                if (status == HttpStatus.OK) {
                    return ResponseEntity.status(HttpStatus.OK).build();
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found");
            }
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }
    }

    //send request to booking to delete all bookings
    public ResponseEntity<?> deleteBookings() {
        String url = "http://sakshi-bookingservice:8081/bookings";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<?> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String.class);
            HttpStatus status = (HttpStatus) responseEntity.getStatusCode();
            if (status == HttpStatus.OK) {
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found");
        }
    }

    //send request to wallet to delete all wallets

        public ResponseEntity<?> deleteWallets(){
            String url = "http://sakshi-walletservice:8082/wallets";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            try{
                ResponseEntity<?> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE,requestEntity,String.class);
                HttpStatus status = (HttpStatus) responseEntity.getStatusCode();
                if(status==HttpStatus.OK){
                    return ResponseEntity.status(HttpStatus.OK).build();
                }else{
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

            }catch(Exception e){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found");
        }
    }
}

