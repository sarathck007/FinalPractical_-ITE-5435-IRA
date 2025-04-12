# SarathChandranKarintha_ITE5435_FinalExam

## Overview

This is a Spring Boot web application developed for **Husky Air**, which is transitioning from a console-based reservation system to a web-based platform. The application includes REST API integration, JSON processing using Jackson, and MongoDB for data storage. The front end is implemented with Thymeleaf templates.

This project fulfills the requirements of the **ITE5435 Final Exam**.

## Features

- Web-based reservation system with form submission
- Spring MVC architecture
- REST API endpoints using Spring Web
- JSON serialization and deserialization using Jackson
- MongoDB integration via Spring Data MongoDB
- Thymeleaf-based web frontend
- Three entities: `Reservation`, `Customer`, and `Payment`
- REST methods: `GET`, `POST`, `PUT`, and `DELETE`

## Technologies Used

- Java 21
- Spring Boot
- Spring MVC
- Spring Data MongoDB
- Jackson (for JSON processing)
- Thymeleaf (frontend rendering)
- MongoDB (NoSQL database)
- Postman (for REST API testing)

## How to Run the Project

1. Clone the repository
2. Make sure MongoDB is running locally
3. Import project in Eclipse/IntelliJ
4. Run `FinalExamApplication.java` as a Spring Boot application
5. Access the app via browser at `http://localhost:8080`
6. Use Postman to test REST API endpoints

## REST API Endpoints

- `GET /api/reservations` - Get all reservations
- `POST /api/reservations` - Add a new reservation (from web form)
- `PUT /api/reservations/{id}` - Update a reservation
- `DELETE /api/reservations/{id}` - Delete a reservation


## Author

Sarath Chandran Karintha  



