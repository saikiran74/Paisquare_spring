package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Blockedadvertiser;
import com.paisa_square.paisa.model.Register;
import com.paisa_square.paisa.repository.Advertiserepository;
import com.paisa_square.paisa.repository.Blockedrepository;
import com.paisa_square.paisa.repository.Registerrepository;
import com.paisa_square.paisa.serice.Followerservice;
import com.paisa_square.paisa.serice.Registerservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class Blockedcontrol {
    @Autowired
    private Followerservice service;
    @Autowired
    private Registerservice registerservice;
    @Autowired
    private Blockedrepository blockedrepo;
    @Autowired
    private Registerrepository registerRepo;
    @Autowired
    private Advertiserepository adrepo;
    @PostMapping("/{userid}/{advertiserid}/blockadvertiser")
    @CrossOrigin(origins = "http://localhost:4200/")
    public Blockedadvertiser block(@RequestBody Blockedadvertiser block, @PathVariable("advertiserid") Long advertiserid, @PathVariable("userid") Long userid) throws Exception {
        Optional<Register> registermodel = registerservice.fetchId(userid);
        Optional<Register> advertisermodel = registerservice.fetchId(advertiserid);
        if (registermodel.isPresent()) {
            Register register = registermodel.get();
            Register advertiser = advertisermodel.get();
            block.setAdvertiser(advertiser);
            block.setUser(register);
            if(register.getBlocked().contains(advertiserid)){
                System.out.println("advertiserid exist in the register  blocked removing.");
                register.getBlocked().remove(advertiserid);
                registerRepo.save(register);
            }
            else{
                System.out.println("advertiserid not  exist saving the register blocked adding");
                register.getBlocked().add(advertiserid);
                registerRepo.save(register);
            }
        }
        Optional<Blockedadvertiser> Blockedmodel = blockedrepo.findByAdvertiserIdAndUserId(advertiserid, userid);
        if (Blockedmodel.isPresent()) {
            Blockedadvertiser blockedsobj = Blockedmodel.get();
            if(blockedsobj.isBlocked()){
                blockedsobj.setBlocked(false);
                blockedsobj.setLastupdate(new Date());
                blockedrepo.save(blockedsobj);
            }
            else{
                blockedsobj.setBlocked(true);
                blockedsobj.setLastupdate(new Date());
                blockedrepo.save(blockedsobj);
            }
        }
        else
            blockedrepo.save(block);
        System.out.println("blockadvertiser->advertisment id not exits"+userid);
        return block;
    }
    @GetMapping("/{userid}/UserBlockedProfiles")
    public List<Register> getUserBlockedProfiles(@PathVariable("userid") Long userid) {
        List<Register> BlockAdvertiserModel=registerRepo.findAllProfilesUserBlocker(userid);
        if (!BlockAdvertiserModel.isEmpty()) {
            return registerRepo.findAllProfilesUserBlocker(userid);
        } else {
            return Collections.emptyList();
        }
    }
    @GetMapping("/{userid}/getUserBlockedAdvertisementsList")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Advertise> getUserBlockedAdvertisementsList(@PathVariable("userid") Long userid) {
        Optional<Register> registermodel = registerservice.fetchId(userid);
        if (registermodel.isPresent()) {
            return adrepo.findAdvertiseByUserBlocked(userid);
        } else {
            return Collections.emptyList();
        }
    }
}
