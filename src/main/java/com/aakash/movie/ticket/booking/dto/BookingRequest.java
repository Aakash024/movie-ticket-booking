package com.aakash.movie.ticket.booking.dto;

import java.util.List;

public record BookingRequest(Integer showId, String customerName, List<String> seats) {
}