package com.aakash.movie.ticket.booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aakash.movie.ticket.booking.entity.BookedSeat;

public interface BookedSeatRepository extends JpaRepository<BookedSeat, Integer> {

    List<BookedSeat> findByShowId(Integer showId);

    List<BookedSeat> findByBookingId(Integer bookingId);
}