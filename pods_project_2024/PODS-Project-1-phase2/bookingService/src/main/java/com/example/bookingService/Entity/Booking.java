package com.example.bookingService.Entity;




import jakarta.persistence.*;

@Entity
@Table(name="booking")
public class Booking {



    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookingidgen")
    @SequenceGenerator(name = "bookingidgen",initialValue = 1,allocationSize = 1)
    private Integer id;

    @Column(name = "seats_booked")
    private Integer seatsBooked;

    @Column(name = "user_id")
    private Integer userId;

    @ManyToOne
    @JoinColumn(name="show_id",nullable = false)
    private Show show;
    public Booking(){}


    public Booking(int seatsBooked,int userId, Show show){
        this.seatsBooked=seatsBooked;
        this.userId=userId;
        this.show=show;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getShowId() {
        return show.getId();
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public Integer getSeatsBooked() {
        return seatsBooked;
    }

    public void setSeatsBooked(Integer seatsBooked) {
        this.seatsBooked = seatsBooked;
    }
}
