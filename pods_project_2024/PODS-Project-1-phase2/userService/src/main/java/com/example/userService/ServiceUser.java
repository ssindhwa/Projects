package com.example.userService;



import jakarta.persistence.*;

@Entity
public class ServiceUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "useridgen")
    @SequenceGenerator(name="useridgen",initialValue = 1,allocationSize = 1)
    private Integer id;
    @Column(name="name",nullable = false)
    private String name;
    @Column(name="email",unique = true)
    private String email;

    public ServiceUser(String name, String email){
        this.name = name;
        this.email = email;
    }

    public ServiceUser(){}
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Override
    public String toString(){
       return "ServiceUser{" +
                "id = " + id +
                ",name = " + name +'\'' +
                ",email =" +email + '\'' +
                '}';
    }


}
