package com.aakash.movie.ticket.booking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aakash.movie.ticket.booking.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Optional<Booking> findByBookingCode(String bookingCode);

    boolean existsByBookingCode(String bookingCode);
}