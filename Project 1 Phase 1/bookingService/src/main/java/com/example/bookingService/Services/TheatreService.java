package com.example.bookingService.Services;

import com.example.bookingService.Entity.Theatre;
import com.example.bookingService.Repository.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
public class TheatreService {
    @Autowired
    private TheatreRepository theatreRepository;
    String line ="";
    public void populateTheatreFromCSV() {
        try  {
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/csv/theatres.csv"));

            while ((line = br.readLine()) != null) {
                String[] data = line.split(","); // Assuming CSV has comma-separated values
                Theatre theatre = new Theatre();
                theatre.setId(Integer.valueOf(data[0]));
                theatre.setName(data[1]);
                theatre.setLocation(data[2]);
                theatreRepository.save(theatre);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Theatre> getTheatres(){
        return theatreRepository.findAll();
    }

    public Theatre getTheatre(Integer theatre_id){
        return theatreRepository.findById(theatre_id).orElse(null);
    }
}
