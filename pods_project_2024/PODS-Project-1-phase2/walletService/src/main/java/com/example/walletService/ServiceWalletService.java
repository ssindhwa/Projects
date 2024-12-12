package com.example.walletService;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;


@Service
public class ServiceWalletService {
    @Autowired
    private ServiceWalletRepository serviceWalletRepository;

    @Autowired
    private RestTemplate restTemplate;

    //get all wallets
    public List<ServiceWallet> getServiceWallets(){
        return serviceWalletRepository.findAll();
    }

    //get wallet by user_id
    public ServiceWalletResponse getServiceWallet(Integer user_id){
        HashMap<String, Integer> params = new HashMap<>();
        params.put("userId", user_id);
        try {
            //check if user exists else 404
            ResponseEntity<ServiceUser> response
                    = restTemplate.getForEntity(
                    "http://anushka-userservice:8080/users/{userId}",
                    ServiceUser.class, params);

            if (response.getBody()!=null) {
                ServiceUser mappedUser = response.getBody();
                Integer mappedId = mappedUser.getId();
                ServiceWallet wallet= serviceWalletRepository.findById(mappedId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Wallet not found"));
                return new ServiceWalletResponse(wallet.getUserId(), wallet.getBalance());
            }
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    return null;
    }

    //save wallet in db
    public ResponseEntity<?> saveServiceWallet(Integer userID, ServiceWalletResponse serviceWallet){
        HashMap<String, Integer> params = new HashMap<>();
        params.put("userId", userID);
        try {
            //check valid user
            ResponseEntity<ServiceUser> response
                    = restTemplate.getForEntity(
                    "http://anushka-userservice:8080/users/{userId}",
                    ServiceUser.class, params);

            if (response.hasBody()) {
                ServiceWallet serviceWallet1 = new ServiceWallet(userID,serviceWallet.getBalance());
                 serviceWalletRepository.save(serviceWallet1);
                 return ResponseEntity.status(HttpStatus.OK).body(new ServiceWalletResponse(userID,serviceWallet.getBalance()));

            }
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    //update wallet
     public ResponseEntity<?> updateServiceWallet(Integer userId, ServiceWalletDTO serviceWalletDTO){
        String action = serviceWalletDTO.getAction();
        Integer amount = serviceWalletDTO.getAmount();
        ServiceWallet serviceWallet = serviceWalletRepository.findById(userId).orElse(null);
         System.out.println("start");
        if(serviceWallet!=null) System.out.println(serviceWallet.getBalance());
        ServiceWallet serviceWallet1 = new ServiceWallet();
        if(serviceWallet==null){
            //if the wallet doesnt exist first check for valid user
            HashMap<String, Integer> params = new HashMap<>();
            params.put("userId", userId);
            try {
                ResponseEntity<ServiceUser> response
                        = restTemplate.getForEntity(
                        "http://anushka-userservice:8080/users/{userId}",
                        ServiceUser.class, params);

                if (response.hasBody()) {
                    //if user exists create a 0 balance wallet for them
                    serviceWallet1.setUserId(userId);
                    serviceWallet1.setBalance(0);
                    serviceWalletRepository.save(serviceWallet1);
                }
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
            }
            Integer balance =  serviceWallet1.getBalance();
            System.out.println("New created");
            System.out.println(balance);
            if (action.equals("debit") ) {
                //check balance debit only if >=0
                balance -= amount;
                if (balance >= 0) {
                    serviceWallet1.setBalance(balance);
                    serviceWalletRepository.updateBalance(balance,userId);
                    return ResponseEntity.status(HttpStatus.OK).body(new ServiceWalletResponse(userId,serviceWallet1.getBalance()));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient Balance");
                }
            }
            else if (action.equals("credit")) {
                //credit account
                balance += amount;
                System.out.println(balance);
                serviceWallet1.setBalance(balance);
                serviceWalletRepository.updateBalance(balance,userId);
                return ResponseEntity.status(HttpStatus.OK).body(new ServiceWalletResponse(userId,serviceWallet1.getBalance()));
            }
        }
        else {
            //above steps repeated but wallet already existed
            Integer balanceCurr = serviceWallet.getBalance();
            System.out.println("Already exists");
            System.out.println(balanceCurr);
            if (action.equals("debit") ) {
                balanceCurr -= amount;
                if (balanceCurr >= 0) {
                    serviceWallet.setBalance(balanceCurr);
                    serviceWalletRepository.updateBalance(balanceCurr,userId);
                    return ResponseEntity.status(HttpStatus.OK).body(new ServiceWalletResponse(userId,serviceWallet.getBalance()));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient balance");
                }
            }
            if (action.equals("credit") ) {
                balanceCurr += amount;
                serviceWallet.setBalance(balanceCurr);
                serviceWalletRepository.updateBalance(balanceCurr,userId);
                return ResponseEntity.status(HttpStatus.OK).body(new ServiceWalletResponse(userId,serviceWallet.getBalance()));
            }
        }
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong Request");
     }

     //delete wallet by id
    public ResponseEntity<?> deleteServiceWallet(Integer userId){
        serviceWalletRepository.findById(userId).ifPresent(serviceWallet -> serviceWalletRepository.deleteById(userId));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

        //delete all wallets
    public void deleteAllServiceWallets(){
        serviceWalletRepository.deleteAll();
    }
}
