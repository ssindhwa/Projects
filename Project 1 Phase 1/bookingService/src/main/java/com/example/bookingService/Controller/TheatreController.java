package com.example.bookingService.Controller;


import com.example.bookingService.Entity.Theatre;
import com.example.bookingService.Services.TheatreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
@RestController
@RequestMapping("/")
public class TheatreController {

    @Autowired
    private TheatreService theatreService;

    @GetMapping("/theatres")
    public ResponseEntity<List<Theatre>> getTheatres(){
        List<Theatre> theatres = theatreService.getTheatres();
        return ResponseEntity.status(HttpStatus.OK).body(theatres);
    }

}
