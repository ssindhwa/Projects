package com.example.bookingService.Entity;

public class BookingDTO {
    private Integer show_id;
    private Integer seats_booked;
    private Integer user_id;

    public BookingDTO(Integer show_id, Integer seats_booked, Integer user_id) {
        this.show_id = show_id;
        this.seats_booked = seats_booked;
        this.user_id = user_id;
    }

    public BookingDTO() {
    }

    public Integer getShow_id() {
        return show_id;
    }

    public void setShow_id(Integer show_id) {
        this.show_id = show_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public int getSeats_booked() {
        return seats_booked;
    }

    public void setSeats_booked(int seats_booked) {
        this.seats_booked = seats_booked;
    }
}
