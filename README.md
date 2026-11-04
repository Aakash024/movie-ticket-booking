# Movie Ticket Booking
Movie ticket booking app with Spring Boot, MySQL and a web frontend.
A full stack movie ticket booking app with REST APIs and a MySQL database, where users pick seats on a live seat map and cancel later with a unique booking code. I built it to learn Spring Boot, especially how to stop two people booking the same seat.

# Screenshots
<img width="1909" height="928" alt="Movie list" src="https://github.com/user-attachments/assets/3c19bae9-8328-4ae5-8232-9825da2f3b4f" />
<img width="877" height="928" alt="Seat map" src="https://github.com/user-attachments/assets/decb7270-1a83-4c6f-a471-2d30438a5e07" />
<img width="602" height="525" alt="Ticket confirmation" src="https://github.com/user-attachments/assets/685303f0-45bc-4a44-ad70-3dafe490bb4f" />

# Features

- Browse shows: movies with genre, language, duration, screen, time and price
- Seat map: a theatre style layout loaded from the database, with available, selected and booked seats shown differently
- Multi-seat booking with the total price shown before you confirm
- Double-booking protection: seats are checked inside a transaction, and a database UNIQUE constraint stops two people taking the same seat at the same time
- Unique booking codes: each booking gets a random code such as `MTB-7K4Q9X`
- Cancellation by code and name: a booking can only be looked up or cancelled with both its code and the name used to book
- Cancellation rule: bookings can't be cancelled once the show has started
- Input validation and clear errors: invalid seats, taken seats and wrong codes return proper HTTP responses (400, 404, 409) with readable messages
- Mobile-friendly layout: the seat map scrolls on small screens

# Tech stack

- Language: Java 17+ (built with Java 26)
- Backend: Spring Boot 4, Spring Web (REST APIs), Spring Data JPA (Hibernate)
- Database: MySQL 8
- Frontend: HTML, CSS and JavaScript (no framework), served by Spring Boot
- Tools: Maven, Git, GitHub, Eclipse, MySQL Workbench
