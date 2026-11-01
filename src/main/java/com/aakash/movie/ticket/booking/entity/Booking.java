package com.aakash.movie.ticket.booking.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "booking_code")
    private String bookingCode;

    @Column(name = "show_id")
    private Integer showId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public Booking() {
    }

    public Booking(String bookingCode, Integer showId, String customerName, BigDecimal totalAmount) {
        this.bookingCode = bookingCode;
        this.showId = showId;
        this.customerName = customerName;
        this.totalAmount = totalAmount;
    }

    public Integer getId() {
        return id;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public Integer getShowId() {
        return showId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}