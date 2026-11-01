package com.aakash.movie.ticket.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aakash.movie.ticket.booking.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
}