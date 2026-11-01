package com.aakash.movie.ticket.booking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "booked_seats")
public class BookedSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "booking_id")
    private Integer bookingId;

    @Column(name = "show_id")
    private Integer showId;

    @Column(name = "seat_row")
    private char seatRow;

    @Column(name = "seat_number")
    private Integer seatNumber;

    public BookedSeat() {
    }

    public BookedSeat(Integer bookingId, Integer showId, char seatRow, Integer seatNumber) {
        this.bookingId = bookingId;
        this.showId = showId;
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
    }

    public Integer getId() {
        return id;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public Integer getShowId() {
        return showId;
    }

    public char getSeatRow() {
        return seatRow;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }
}