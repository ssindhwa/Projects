package com.example.walletService;

public class ServiceWalletResponse {
    private Integer user_id;

    private Integer balance;

    public Integer getUser_id() {
        return user_id;
    }

    public ServiceWalletResponse() {
    }

    public ServiceWalletResponse(Integer user_id, Integer balance) {
        this.user_id = user_id;
        this.balance = balance;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
}
