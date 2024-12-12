package com.example.bookingService.Entity;



public class ServiceWallet {

    private Integer userId;

    private Integer balance;

    public ServiceWallet(Integer userId, Integer balance){
        this.userId=userId;
        this.balance=balance;
    }

    public ServiceWallet(){}

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
}
