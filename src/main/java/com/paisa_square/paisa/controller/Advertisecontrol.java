package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Advertisementtransaction;
import com.paisa_square.paisa.model.Comments;
import com.paisa_square.paisa.model.Register;
import com.paisa_square.paisa.repository.Advertisementtransactionrepository;
import com.paisa_square.paisa.repository.Advertiserepository;
import com.paisa_square.paisa.repository.Registerrepository;
import com.paisa_square.paisa.serice.Advertiseservice;
import com.paisa_square.paisa.serice.Registerservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.math.BigDecimal.valueOf;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
public class Advertisecontrol {
    @Autowired
    private Registerservice registerservice;
    @Autowired
    private Registerrepository registerRepo;
    @Autowired
    private Advertiseservice service;
    @Autowired
    private Advertisementtransactionrepository adtransRepo;
    @Autowired
    private Advertiserepository adrepo;
    @GetMapping("/advertisements")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Advertise> getAllAdvertisements() {
        return service.findAlladvertisement();
    }
    @GetMapping("/{advertisementid}/idadvertisements")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Advertise> getIDAdvertisements(@PathVariable("advertisementid") Long advertisementid) {
        return Collections.singletonList(adrepo.findById(advertisementid).orElse(null));
    }
    @GetMapping("/{userid}/useradvertisements")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Advertise> getUserAdvertisements(@PathVariable("userid") Integer userid) {
        return service.findAllByadvertiserId(userid);
    }

    @PostMapping("/{userid}/advertise")
    @CrossOrigin(origins = "http://localhost:4200")
    public Advertise advertise(@RequestBody Advertise ad,@PathVariable("userid") Long userid) throws Exception {
        Optional<Register> registermodel = registerservice.fetchId(userid);
        Advertise trans=null;
        if (registermodel.isPresent()) {
            Register register = registermodel.get();
            ad.setAdvertiser(register);
            if(ad.getPaisa()==null){
                ad.setPaisa(BigDecimal.ZERO);
            }
            if(ad.getPai()==null){
                ad.setPai(BigDecimal.ZERO);
            }
            if(ad.getPaiperclick()==null){
                ad.setPaiperclick(BigDecimal.ZERO);
            }
            if(ad.getPaisaperclick()==null){
                ad.setPaisaperclick(BigDecimal.ZERO);
            }
            if(register.getPai().compareTo(ad.getPai())>=0 || register.getPaisa().compareTo(ad.getPaisa())>=0){
                    System.out.println("inside the here");
                    register.setPai(register.getPai().subtract(ad.getPai()));
                    register.setPaisa(register.getPaisa().subtract(ad.getPaisa()));
                    ad.setAvailablepai(ad.getPai());
                    ad.setAvailablepaisa(ad.getPaisa());
                    registerRepo.save(register);
                    trans=service.savead(ad);
                    Advertisementtransaction transaction=new Advertisementtransaction();
                    transaction.setAdvertisement(trans);
                    transaction.setAdvertiser(trans.getAdvertiser());
                    transaction.setAdvertisementpai(trans.getPai());
                    transaction.setAdvertisementpaisa(trans.getPaisa());
                    transaction.setAvailablepaisa(register.getPaisa());
                    transaction.setAvailablepai(register.getPai());
                    System.out.println("trans table data--->"+transaction);
                    adtransRepo.save(transaction);
                }
            else
                throw new Exception("emailid is exist");
        }
        else {
            throw new IllegalArgumentException("userId not found with id: " + userid);
        }
        return ad;
    }

    @GetMapping("/{advertisementid}/commentslist")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Comments> getAllCommentList(@PathVariable("advertisementid") Integer advertisementid) {
        return service.findByadvertisementid(advertisementid);
    }

}
