package com.aakash.movie.ticket.booking.service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.aakash.movie.ticket.booking.dto.BookingRequest;
import com.aakash.movie.ticket.booking.dto.BookingResponse;
import com.aakash.movie.ticket.booking.entity.BookedSeat;
import com.aakash.movie.ticket.booking.entity.Booking;
import com.aakash.movie.ticket.booking.entity.Show;
import com.aakash.movie.ticket.booking.repository.BookedSeatRepository;
import com.aakash.movie.ticket.booking.repository.BookingRepository;
import com.aakash.movie.ticket.booking.repository.ShowRepository;

@Service
public class BookingService {

    private static final String CODE_PREFIX = "MTB-";
    // No I, O, 0 or 1, so codes are easy to read and type
    private static final String CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final ShowRepository showRepository;
    private final BookingRepository bookingRepository;
    private final BookedSeatRepository bookedSeatRepository;

    public BookingService(ShowRepository showRepository, BookingRepository bookingRepository,
            BookedSeatRepository bookedSeatRepository) {
        this.showRepository = showRepository;
        this.bookingRepository = bookingRepository;
        this.bookedSeatRepository = bookedSeatRepository;
    }

    @Transactional
    public BookingResponse book(BookingRequest request) {
        if (request.showId() == null || request.customerName() == null
                || request.customerName().isBlank()
                || request.seats() == null || request.seats().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "showId, customerName and at least one seat are required");
        }

        Show show = showRepository.findById(request.showId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Show " + request.showId() + " not found"));

        Set<String> requested = new LinkedHashSet<>();
        for (String raw : request.seats()) {
            String code = raw == null ? "" : raw.trim().toUpperCase();
            validateSeat(show, code);
            if (!requested.add(code)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Seat " + code + " was selected twice");
            }
        }

        Set<String> alreadyBooked = bookedSeatRepository.findByShowId(show.getId()).stream()
                .map(b -> "" + b.getSeatRow() + b.getSeatNumber())
                .collect(Collectors.toSet());
        for (String code : requested) {
            if (alreadyBooked.contains(code)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Seat " + code + " is already booked");
            }
        }

        BigDecimal total = show.getPrice().multiply(BigDecimal.valueOf(requested.size()));
        Booking booking = bookingRepository.save(
                new Booking(generateUniqueCode(), show.getId(), request.customerName().trim(), total));

        try {
            for (String code : requested) {
                bookedSeatRepository.save(new BookedSeat(booking.getId(), show.getId(),
                        code.charAt(0), Integer.parseInt(code.substring(1))));
            }
            bookedSeatRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "One of these seats was just booked by someone else");
        }

        return toResponse(booking, show, new ArrayList<>(requested));
    }

    @Transactional(readOnly = true)
    public BookingResponse lookup(String code, String name) {
        Booking booking = findVerified(code, name);
        Show show = showRepository.findById(booking.getShowId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Show not found"));
        List<String> seats = bookedSeatRepository.findByBookingId(booking.getId()).stream()
                .map(b -> "" + b.getSeatRow() + b.getSeatNumber())
                .sorted()
                .toList();
        return toResponse(booking, show, seats);
    }

    @Transactional
    public void cancel(String code, String name) {
        Booking booking = findVerified(code, name);
        Show show = showRepository.findById(booking.getShowId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Show not found"));

        if (!show.getShowTime().isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "This show has already started, so the booking can no longer be cancelled");
        }
        // Seats are freed automatically by ON DELETE CASCADE in the database
        bookingRepository.delete(booking);
    }

    // The same message for a wrong code or a wrong name, so nobody can guess which was right
    private Booking findVerified(String code, String name) {
        if (code == null || code.isBlank() || name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Booking code and name are required");
        }
        return bookingRepository.findByBookingCode(normalizeCode(code))
                .filter(b -> b.getCustomerName().equalsIgnoreCase(name.trim()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No booking found with that code and name"));
    }

    private String normalizeCode(String code) {
        String c = code.trim().toUpperCase().replace(" ", "");
        return c.startsWith(CODE_PREFIX) ? c : CODE_PREFIX + c;
    }

    private String generateUniqueCode() {
        String code;
        do {
            StringBuilder sb = new StringBuilder(CODE_PREFIX);
            for (int i = 0; i < 6; i++) {
                sb.append(CODE_CHARS.charAt(RANDOM.nextInt(CODE_CHARS.length())));
            }
            code = sb.toString();
        } while (bookingRepository.existsByBookingCode(code));
        return code;
    }

    private BookingResponse toResponse(Booking booking, Show show, List<String> seats) {
        return new BookingResponse(booking.getBookingCode(), show.getId(), show.getMovie().getTitle(),
                show.getScreenName(), show.getShowTime(), booking.getCustomerName(),
                seats, booking.getTotalAmount());
    }

    private void validateSeat(Show show, String code) {
        if (code.length() < 2) {
            throw invalid(code);
        }
        int number;
        try {
            number = Integer.parseInt(code.substring(1));
        } catch (NumberFormatException e) {
            throw invalid(code);
        }
        int row = code.charAt(0) - 'A';
        if (row < 0 || row >= show.getTotalRows() || number < 1 || number > show.getSeatsPerRow()) {
            throw invalid(code);
        }
    }

    private ResponseStatusException invalid(String code) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seat " + code + " does not exist");
    }
}