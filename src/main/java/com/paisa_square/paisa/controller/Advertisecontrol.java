package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Comments;
import com.paisa_square.paisa.model.Register;
import com.paisa_square.paisa.repository.Advertiserepository;
import com.paisa_square.paisa.repository.Commentrepository;
import com.paisa_square.paisa.serice.Advertiseservice;
import com.paisa_square.paisa.serice.Registerservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
public class Advertisecontrol {
    @Autowired
    private Registerservice registerservice;
    @Autowired
    private Advertiseservice service;
    @Autowired
    private Advertiserepository adrepo;
    @GetMapping("/advertisements")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Advertise> getAllAdvertisements() {
        System.out.println(service.findAlladvertisement());
        return service.findAlladvertisement();
    }
    @GetMapping("/{advertisementid}/idadvertisements")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Advertise> getIDAdvertisements(@PathVariable("advertisementid") Long advertisementid) {
        System.out.println(adrepo.findById(advertisementid));
        return Collections.singletonList(adrepo.findById(advertisementid).orElse(null));
    }
    @GetMapping("/{userid}/useradvertisements")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Advertise> getUserAdvertisements(@PathVariable("userid") Integer userid) {
        System.out.println(service.findAlladvertisement());
        return service.findAllByadvertiserId(userid);
    }

    @PostMapping("/{userid}/advertise")
    @CrossOrigin(origins = "http://localhost:4200")
    public Advertise advertise(@RequestBody Advertise ad,@PathVariable("userid") Long userid) throws Exception {
        Optional<Register> registermodel = registerservice.fetchId(userid);
        if (registermodel.isPresent()) {
            Register register = registermodel.get();
            ad.setAdvertiser(register);
            System.out.println(register);
            System.out.println(ad);
            service.savead(ad);
            if (ad == null) {
                throw new Exception("Bad adveritise details");
            }
        }else {
            throw new IllegalArgumentException("Advertisement not found with id: " + 1);
        }
        return ad;
    }

    @GetMapping("/{advertisementid}/commentslist")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Comments> getAllCommentList(@PathVariable("advertisementid") Integer advertisementid) {
        System.out.println("-=-=-=-=-=-====-==-=-=");
        System.out.println(service.findByadvertisementid(advertisementid));
        return service.findByadvertisementid(advertisementid);
    }

}
