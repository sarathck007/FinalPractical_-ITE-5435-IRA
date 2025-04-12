package com.sck.spring.finalex.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sck.spring.finalex.dto.ReservationDTO;
import com.sck.spring.finalex.model.Customer;
import com.sck.spring.finalex.model.Payment;
import com.sck.spring.finalex.model.Reservation;
import com.sck.spring.finalex.service.CustomerService;
import com.sck.spring.finalex.service.PaymentService;
import com.sck.spring.finalex.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;
import java.util.UUID;

@Controller
public class WebController {

	private final CustomerService customerService;
	private final PaymentService paymentService;
	private final ReservationService reservationService;
	private final ObjectMapper objectMapper;

	@Autowired
	public WebController(CustomerService customerService, PaymentService paymentService,
			ReservationService reservationService, ObjectMapper objectMapper) {
		this.customerService = customerService;
		this.paymentService = paymentService;
		this.reservationService = reservationService;
		this.objectMapper = objectMapper;
	}

	@GetMapping("/")
	public String showForm(Model model) {
		// Add a new DTO to the model
		model.addAttribute("reservationDTO", new ReservationDTO());
		return "reservation-form";
	}

	@PostMapping("/submit")
	public String processForm(@ModelAttribute ReservationDTO reservationDTO, Model model) {
		try {
			// Log the DTO as JSON
			String dtoJson = objectMapper.writeValueAsString(reservationDTO);
			System.out.println("Reservation DTO JSON: " + dtoJson);

			// Create and save Customer entity (Entity 2)
			Customer customer = new Customer();
			customer.setFirstName(reservationDTO.getFirstName());
			customer.setLastName(reservationDTO.getLastName());
			customer.setPhone(reservationDTO.getPhone());
			customer.setPassengers(reservationDTO.getPassengers());
			customer.setTravelClass(reservationDTO.getTravelClass());
			customer.setEmail(reservationDTO.getEmail() != null ? reservationDTO.getEmail()
					: reservationDTO.getFirstName().toLowerCase() + "." + reservationDTO.getLastName().toLowerCase()
							+ "@example.com");

			customer = customerService.save(customer);

			// Convert Customer to JSON using Jackson
			String customerJson = objectMapper.writeValueAsString(customer);
			System.out.println("Customer JSON: " + customerJson);
			objectMapper.writeValue(new File("customer.json"), customer);

			// Calculate price based on class and passengers
			int basePrice = 0;
			if (reservationDTO.getTravelClass().equals("Economy class")) {
				basePrice = 500;
			} else if (reservationDTO.getTravelClass().equals("Business class")) {
				basePrice = 1200;
			} else if (reservationDTO.getTravelClass().equals("First class")) {
				basePrice = 2000;
			}
			int totalAmount = basePrice * reservationDTO.getPassengers();

			// Create and save Payment entity (Entity 3)
			Payment payment = new Payment();
			payment.setAmount(totalAmount);
			payment.setDate(reservationDTO.getDepartureDate());
			payment = paymentService.save(payment);

			// Convert Payment to JSON using Jackson
			String paymentJson = objectMapper.writeValueAsString(payment);
			System.out.println("Payment JSON: " + paymentJson);
			objectMapper.writeValue(new File("payment.json"), payment);

			// Create and save Reservation entity (Entity 1 - Main Web Entity)
			Reservation reservation = new Reservation();
			reservation.setReservationNumber("HA" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
			reservation.setOrigin(reservationDTO.getOrigin());
			reservation.setDestination(reservationDTO.getDestination());
			reservation.setDepartureDate(reservationDTO.getDepartureDate());
			reservation.setDepartureTime(reservationDTO.getDepartureTime());
			reservation.setPassengers(reservationDTO.getPassengers());
			reservation.setTravelClass(reservationDTO.getTravelClass());

			// Link Customer and Payment to Reservation
			reservation.setCustomer(customer);
			reservation.setPayment(payment);

			// Save Reservation to MongoDB
			reservation = reservationService.save(reservation);

			// Convert Reservation to JSON using Jackson
			String reservationJson = objectMapper.writeValueAsString(reservation);
			System.out.println("Reservation JSON: " + reservationJson);
			objectMapper.writeValue(new File("reservation.json"), reservation);

			// Add attributes to the model for the success page
			model.addAttribute("customer", customer);
			model.addAttribute("payment", payment);
			model.addAttribute("reservation", reservation);

			return "confirmation";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "An error occurred: " + e.getMessage());
			return "reservation-form";
		}
	}
}