package com.example.bookingService.Controller;


import com.example.bookingService.Entity.Show;
import com.example.bookingService.Entity.ShowDTO;
import com.example.bookingService.Services.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class ShowController {
    @Autowired
    private ShowService showService;
    @GetMapping("/shows/{id}")
    public ResponseEntity<ShowDTO> getShow(@PathVariable Integer id) {
        ShowDTO show = showService.getShow(id);
        return ResponseEntity.status(HttpStatus.OK).body(show);
    }

    @GetMapping("/shows/theatres/{theatre_id}")
    public ResponseEntity<List<ShowDTO>> getShowByTheatre(@PathVariable Integer theatre_id){

        return showService.getShowByTheatre(theatre_id);
    }

}
