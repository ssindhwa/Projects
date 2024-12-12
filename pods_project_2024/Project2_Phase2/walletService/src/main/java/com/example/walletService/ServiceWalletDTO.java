package com.example.walletService;



public class ServiceWalletDTO {


    private String action;

    private Integer amount;



    public ServiceWalletDTO(){

    }

    public ServiceWalletDTO(String action,Integer amount){

            this.action=action;
            this.amount = amount;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

}
