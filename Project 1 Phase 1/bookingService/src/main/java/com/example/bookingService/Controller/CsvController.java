package com.example.bookingService.Controller;

import com.example.bookingService.Services.ShowService;
import com.example.bookingService.Services.TheatreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsvController {
    @Autowired
    private TheatreService theatreService;
    @Autowired
    private ShowService showService;
    @RequestMapping("/")
    public void setData(){
        //read data from csv files
        theatreService.populateTheatreFromCSV();
        showService.populateShowFromCSV();
    }


}
