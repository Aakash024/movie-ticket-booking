package com.aakash.movie.ticket.booking.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.aakash.movie.ticket.booking.dto.SeatView;
import com.aakash.movie.ticket.booking.entity.Show;
import com.aakash.movie.ticket.booking.repository.BookedSeatRepository;
import com.aakash.movie.ticket.booking.repository.ShowRepository;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    private final ShowRepository showRepository;
    private final BookedSeatRepository bookedSeatRepository;

    public ShowController(ShowRepository showRepository, BookedSeatRepository bookedSeatRepository) {
        this.showRepository = showRepository;
        this.bookedSeatRepository = bookedSeatRepository;
    }

    @GetMapping
    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

    @GetMapping("/{id}/seats")
    public List<SeatView> getSeats(@PathVariable("id") Integer id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Show " + id + " not found"));

        Set<String> booked = bookedSeatRepository.findByShowId(id).stream()
                .map(b -> "" + b.getSeatRow() + b.getSeatNumber())
                .collect(Collectors.toSet());

        List<SeatView> seats = new ArrayList<>();
        for (int r = 0; r < show.getTotalRows(); r++) {
            char row = (char) ('A' + r);
            for (int n = 1; n <= show.getSeatsPerRow(); n++) {
                String code = "" + row + n;
                seats.add(new SeatView(code, row, n, booked.contains(code)));
            }
        }
        return seats;
    }
}