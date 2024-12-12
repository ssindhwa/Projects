package com.example.userService;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceUserRepository extends JpaRepository<ServiceUser,Integer> {
    //find user by email
    ServiceUser findByEmail(String email);
}
