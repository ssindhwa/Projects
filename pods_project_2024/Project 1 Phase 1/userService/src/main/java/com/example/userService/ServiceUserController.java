package com.example.userService;
import com.example.userService.ServiceUser;
import com.example.userService.ServiceUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class ServiceUserController{
    @Autowired
    private ServiceUserService serviceUserService;

    //get all users
    @GetMapping("/users")
    public ResponseEntity<List<ServiceUser>> getServiceUsers(){
       List<ServiceUser> serviceUsers = serviceUserService.getServiceUsers();

       return  ResponseEntity.status(HttpStatus.OK).body(serviceUsers);
    }

    //create new user
    @PostMapping("/users")
    public ResponseEntity<?> createServiceUser(@RequestBody ServiceUser serviceUser){
        String email = serviceUser.getEmail();
        ServiceUser mappedUser = serviceUserService.getUserByEmail(email);
        //check for duplicate emails
        if(mappedUser!= null){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        else{
            serviceUserService.saveServiceUser(serviceUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(serviceUser);
        }

    }

    //get user by id
    @GetMapping("/users/{id}")
    public ResponseEntity<ServiceUser> getServiceUser(@PathVariable Integer id){
       ServiceUser mappedUser = serviceUserService.getServiceUser(id);
       return ResponseEntity.status(HttpStatus.OK).body(mappedUser);
    }

    //delete user by id
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteServiceUserById(@PathVariable Integer id) {
        //also delete all booking
        ResponseEntity<?> responseEntity2= serviceUserService.deleteAllBookingsUser(id);
        //also delete all wallets
        ResponseEntity<?> responseEntity3= serviceUserService.deleteAllWalletsUsers(id);
        ResponseEntity<?> responseEntity1 =serviceUserService.deleteServiceUser(id);
        //check if all is deleted correctly
             if(responseEntity1.getStatusCode()==HttpStatusCode.valueOf(200)
                     && responseEntity2.getStatusCode()==HttpStatusCode.valueOf(200)
                && responseEntity3.getStatusCode()==HttpStatusCode.valueOf(200))
                return ResponseEntity.status(HttpStatus.OK).body(null);
            else{
                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
             }
    }

    //delete all users
    @DeleteMapping("/users")
    public ResponseEntity<?> deleteServiceUsers(){
        //also delete all booking
        ResponseEntity<?> responseEntity2= serviceUserService.deleteBookings();
        //also delete all wallets
        ResponseEntity<?> responseEntity3= serviceUserService.deleteWallets();
        ResponseEntity<?> responseEntity1 =serviceUserService.deleteAllServiceUsers();
        //check if all is deleted correctly
        if(responseEntity1.getStatusCode()==HttpStatusCode.valueOf(200)
                && responseEntity2.getStatusCode()==HttpStatusCode.valueOf(200)
                && responseEntity3.getStatusCode()==HttpStatusCode.valueOf(200))
            return ResponseEntity.status(HttpStatus.OK).body(null);
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
