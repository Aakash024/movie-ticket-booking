package com.aakash.movie.ticket.booking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aakash.movie.ticket.booking.dto.BookingRequest;
import com.aakash.movie.ticket.booking.dto.BookingResponse;
import com.aakash.movie.ticket.booking.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse book(@RequestBody BookingRequest request) {
        return bookingService.book(request);
    }

    @GetMapping("/{code}")
    public BookingResponse lookup(@PathVariable("code") String code,
            @RequestParam("name") String name) {
        return bookingService.lookup(code, name);
    }

    @DeleteMapping("/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable("code") String code,
            @RequestParam("name") String name) {
        bookingService.cancel(code, name);
    }
}