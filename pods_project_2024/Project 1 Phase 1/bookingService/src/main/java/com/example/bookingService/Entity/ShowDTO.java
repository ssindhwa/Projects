package com.example.bookingService.Entity;

import jakarta.persistence.Column;

public class ShowDTO {

    private Integer id;

    private Integer theatre_id;

    private String title;

    public ShowDTO() {
    }

    public ShowDTO(Integer id, Integer theatre_id, String title, Integer price, Integer seats_available) {
        this.id = id;
        this.theatre_id = theatre_id;
        this.title = title;
        this.price = price;
        this.seats_available = seats_available;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTheatre_id() {
        return theatre_id;
    }

    public void setTheatre_id(Integer theatre_id) {
        this.theatre_id = theatre_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getSeats_available() {
        return seats_available;
    }

    public void setSeats_available(Integer seats_available) {
        this.seats_available = seats_available;
    }

    private Integer price;

    private Integer seats_available;
}
