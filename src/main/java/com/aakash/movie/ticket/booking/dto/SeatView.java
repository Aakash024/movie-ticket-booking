package com.aakash.movie.ticket.booking.dto;

public record SeatView(String seat, char row, int number, boolean booked) {
}