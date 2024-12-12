package com.example.walletService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class ServiceWalletController {

    @Autowired
    private ServiceWalletService serviceWalletService;

    @GetMapping("/wallets")
   public List<ServiceWallet> getServiceWallets(){
      return serviceWalletService.getServiceWallets();
    }

    @GetMapping("/wallets/{user_id}")
    public ResponseEntity<?> getServiceWallet(@PathVariable Integer user_id){
        ServiceWalletResponse serviceWallet= serviceWalletService.getServiceWallet(user_id);
        if(serviceWallet!=null){
            return  ResponseEntity.status(HttpStatus.OK).body(serviceWallet);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/wallets/{user_id}")
    public ResponseEntity<?> createServiceWallet(@PathVariable Integer user_id, @RequestBody ServiceWalletResponse serviceWallet){
        return serviceWalletService.saveServiceWallet(user_id,serviceWallet);
    }

    @PutMapping("/wallets/{user_id}")
    public ResponseEntity<?> updateServiceWallet(@PathVariable Integer user_id,@RequestBody ServiceWalletDTO serviceWalletDTO) {
        return serviceWalletService.updateServiceWallet(user_id,serviceWalletDTO);
    }

    @DeleteMapping("wallets/{user_id}")
    public ResponseEntity<?> deleteServiceWalletById(@PathVariable Integer user_id){
        return serviceWalletService.deleteServiceWallet(user_id);

    }

    @DeleteMapping("/wallets")
    public ResponseEntity<?> deleteServiceWallets(){
        serviceWalletService.deleteAllServiceWallets();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
