package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Advertisementtransaction;
import com.paisa_square.paisa.repository.Advertisementtransactionrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Advertisementtransactioncontrol {
    @Autowired
    private Advertisementtransactionrepository adTransRepo;
    @GetMapping("/{userid}/getadvertisementtransactiondata")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Advertisementtransaction> getadvertisementtransactiondata(@PathVariable("userid") Integer userid) {
        return adTransRepo.findAllByadvertiserId(userid);
    }
}
