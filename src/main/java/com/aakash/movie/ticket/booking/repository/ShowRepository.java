package com.aakash.movie.ticket.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aakash.movie.ticket.booking.entity.Show;

public interface ShowRepository extends JpaRepository<Show, Integer> {
}