package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.model.Advertisementtransaction;
import com.paisa_square.paisa.model.Payment;
import com.paisa_square.paisa.repository.Advertisementtransactionrepository;
import com.paisa_square.paisa.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "${cors.allowedOrigins}")
@RequestMapping("/payments")
public class Payments {
    @Autowired
    private Advertisementtransactionrepository adTransRepo;
    @Autowired
    private PaymentRepository paymentRepository;
    @GetMapping("/getadvertisementtransactiondata/{userid}")
    public List<Advertisementtransaction> getadvertisementtransactiondata(@PathVariable("userid") Integer userid) {
        return adTransRepo.findAllByadvertiserId(userid);
    }


    @PostMapping("/addfunds")
    public ResponseEntity<String> getadvertisementtransactiondata(@RequestBody Payment payment) {
        if (paymentRepository.findByTransactionId(payment.getTransactionId()).isPresent()) {

            System.out.println("Payment already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Payment already exists.");
        }
        paymentRepository.save(payment);
        System.out.println("Payment already exists. else");
        return ResponseEntity.ok("Payment saved successfully!");
    }
}

