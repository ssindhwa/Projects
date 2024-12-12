package com.example.bookingService.Entity;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
public class Show {
    @Id
    private Integer id;

    @Column(name="theatre_id")
    private Integer theatreId;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private Integer price;

    @Column(name = "seats_available")
    private Integer seatsAvailable;


    @OneToMany(mappedBy = "show",cascade = CascadeType.ALL)
    private Collection<Booking> bookings;

    public Show(Integer id, Integer theatreId, String title, Integer price, Integer seatsAvailable) {
        this.id = id;
        this.theatreId = theatreId;
        this.title = title;
        this.price = price;
        this.seatsAvailable = seatsAvailable;

    }

    public  Show(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTheatreId() {
        return theatreId;
    }

    public void setTheatreId(Integer theatreId) {
        this.theatreId = theatreId;
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

    public Integer getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(Integer seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }
}
