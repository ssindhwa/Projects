package com.example.bookingService.Repository;

import com.example.bookingService.Entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show,Integer> {
    //query to get show by theatre, need to do this because of java naming conventions not used
    //@Query(value = "SELECT * FROM SHOW WHERE SHOW.theatre_id=:theatre_id",nativeQuery = true)
    List<Show> findByTheatreId(Integer theatre_id);

    //custom query to update show seats
    @Transactional
    @Modifying
    @Query(value = "UPDATE SHOW SET SHOW.seats_available =?1 WHERE SHOW.id =?2",nativeQuery = true)
    void updateShowSeats(@Param(value = "seats_available") Integer seats_available,@Param(value = "id") Integer id);
}


