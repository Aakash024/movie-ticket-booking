package com.aakash.movie.ticket.booking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record BookingResponse(String bookingCode, Integer showId, String movieTitle,
        String screenName, LocalDateTime showTime, String customerName,
        List<String> seats, BigDecimal totalAmount) {
}