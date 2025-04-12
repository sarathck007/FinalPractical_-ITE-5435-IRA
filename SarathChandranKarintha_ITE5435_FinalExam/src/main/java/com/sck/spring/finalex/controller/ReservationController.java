package com.sck.spring.finalex.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sck.spring.finalex.model.Customer;
import com.sck.spring.finalex.model.Payment;
import com.sck.spring.finalex.model.Reservation;
import com.sck.spring.finalex.repository.CustomerRepository;
import com.sck.spring.finalex.repository.PaymentRepository;
import com.sck.spring.finalex.repository.ReservationRepository;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    // GET all reservations
    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
    
    // GET a reservation by ID
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable String id) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        return optionalReservation
                .map(reservation -> ResponseEntity.ok().body(reservation))
                .orElse(ResponseEntity.notFound().build());
    }
    
    // POST a new reservation
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        try {
            // First save the Customer
            if (reservation.getCustomer() != null) {
                Customer customer = customerRepository.save(reservation.getCustomer());
                reservation.setCustomer(customer);
            }
            
            // Then save the Payment
            if (reservation.getPayment() != null) {
                Payment payment = paymentRepository.save(reservation.getPayment());
                reservation.setPayment(payment);
            }
            
            // Finally save the Reservation with references to saved Customer and Payment
            Reservation savedReservation = reservationRepository.save(reservation);
            return new ResponseEntity<>(savedReservation, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // PUT (update) an existing reservation
    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable String id, @RequestBody Reservation reservation) {
        Optional<Reservation> reservationData = reservationRepository.findById(id);
        
        if (reservationData.isPresent()) {
            try {
                // Update the ID to ensure we're updating the correct reservation
                reservation.setId(id);
                
                // Handle Customer update
                if (reservation.getCustomer() != null) {
                    Customer customer = reservation.getCustomer();
                    if (customer.getId() == null && reservationData.get().getCustomer() != null) {
                        customer.setId(reservationData.get().getCustomer().getId());
                    }
                    customer = customerRepository.save(customer);
                    reservation.setCustomer(customer);
                }
                
                // Handle Payment update
                if (reservation.getPayment() != null) {
                    Payment payment = reservation.getPayment();
                    if (payment.getId() == null && reservationData.get().getPayment() != null) {
                        payment.setId(reservationData.get().getPayment().getId());
                    }
                    payment = paymentRepository.save(payment);
                    reservation.setPayment(payment);
                }
                
                // Save the updated Reservation
                Reservation updatedReservation = reservationRepository.save(reservation);
                return new ResponseEntity<>(updatedReservation, HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // DELETE a reservation
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteReservation(@PathVariable String id) {
        try {
            // Optional: also delete related Customer and Payment
            Optional<Reservation> reservation = reservationRepository.findById(id);
            if (reservation.isPresent()) {
                if (reservation.get().getCustomer() != null) {
                    customerRepository.deleteById(reservation.get().getCustomer().getId());
                }
                if (reservation.get().getPayment() != null) {
                    paymentRepository.deleteById(reservation.get().getPayment().getId());
                }
            }
            
            reservationRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}