package com.example.bookingService.Services;

import com.example.bookingService.Entity.Show;
import com.example.bookingService.Entity.ShowDTO;
import com.example.bookingService.Entity.Theatre;
import com.example.bookingService.Repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowService {
    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private TheatreService theatreService;
    String line ="";
    public void populateShowFromCSV() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/csv/shows.csv"));

            while ((line = br.readLine()) != null) {
                String[] data = line.split(","); // Assuming CSV has comma-separated values
                Show show = new Show();
                show.setId(Integer.valueOf(data[0]));
                show.setTheatreId(Integer.valueOf(data[1]));
                show.setTitle(data[2]);
                show.setPrice(Integer.valueOf(data[3]));
                show.setSeatsAvailable(Integer.valueOf(data[4]));
                showRepository.save(show);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ShowDTO getShow(Integer id){
        Show show= showRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Show not found"));

        return new ShowDTO(id,show.getTheatreId(),show.getTitle(),show.getPrice(),show.getSeatsAvailable());
    }

    public ResponseEntity<List<ShowDTO>> getShowByTheatre(Integer theatre_id){
        Theatre theatre = theatreService.getTheatre(theatre_id);
        if(theatre==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<Show> shows = showRepository.findByTheatreId(theatre_id) ;
        List<ShowDTO> showDTOs = shows.stream()
                .map(show -> new ShowDTO(show.getId(), show.getTheatreId(),show.getTitle(),show.getPrice(),show.getSeatsAvailable()))
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(showDTOs);
    }
}
