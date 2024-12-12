package com.example.bookingService.Entity;



import jakarta.persistence.*;



@Entity
public class Theatre {

    @Id
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name="location")
    private String location;

    public Theatre(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
