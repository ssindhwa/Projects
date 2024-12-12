package com.example.bookingService.Repository;


import com.example.bookingService.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Integer> {
    //@Query(value = "SELECT * FROM Booking WHERE Booking.user_id=:user_id",nativeQuery = true)
    List<Booking> findByUserId(Integer user_id);

    //custom query to get booking by show and users
    @Query(value = "SELECT * FROM Booking WHERE Booking.user_id=:user_id AND Booking.show_id=:show_id",nativeQuery = true)
    List<Booking> getBookingByShowUser(@Param("user_id")Integer user_id,@Param("show_id")Integer show_id);

    //custom query to delete all booking with user id
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Booking WHERE Booking.user_id =:user_id",nativeQuery = true)
    void deleteShowByUserId(@Param("user_id") Integer user_id);

    //custom query to delete all booking with show_id and user_id
    @Modifying
    @Transactional
   @Query(value= "DELETE FROM BOOKING WHERE Booking.user_id=:user_id AND Booking.show_id=:show_id",nativeQuery = true)
    void deleteBookingByShowUser(@Param("user_id")Integer user_id,@Param("show_id")Integer show_id);
}
