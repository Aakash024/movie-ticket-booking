package com.aakash.movie.ticket.booking.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "shows")
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Column(name = "screen_name")
    private String screenName;

    @Column(name = "show_time")
    private LocalDateTime showTime;

    private BigDecimal price;

    @Column(name = "total_rows")
    private Integer totalRows;

    @Column(name = "seats_per_row")
    private Integer seatsPerRow;

    public Show() {
    }

    public Integer getId() {
        return id;
    }

    public Movie getMovie() {
        return movie;
    }

    public String getScreenName() {
        return screenName;
    }

    public LocalDateTime getShowTime() {
        return showTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getTotalRows() {
        return totalRows;
    }

    public Integer getSeatsPerRow() {
        return seatsPerRow;
    }
}