package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Comments;
import com.paisa_square.paisa.model.Followers;
import com.paisa_square.paisa.model.Register;
import com.paisa_square.paisa.repository.Followerrepository;
import com.paisa_square.paisa.serice.Advertiseservice;
import com.paisa_square.paisa.serice.Commentservice;
import com.paisa_square.paisa.serice.Followerservice;
import com.paisa_square.paisa.serice.Registerservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
public class Followercontrol {
    @Autowired
    private Followerservice service;
    @Autowired
    private Registerservice userservice;
    @Autowired
    private Followerrepository followersrepo;
    @PostMapping("/{advertiserid}/follow")
    @CrossOrigin(origins = "http://localhost:4200/")
    public Followers comment(@RequestBody Followers follow, @PathVariable("advertiserid") Long advertiserid) throws Exception {
        Optional<Register> registermodel = userservice.fetchId(advertiserid);
        if (registermodel.isPresent()) {
            service.savefollower(follow);
        } else {
            throw new IllegalArgumentException("Advertisement not found with id: " +advertiserid);
        }
        return follow;
    }
    @GetMapping("/followerslist")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Followers> getfollowers(){
        return followersrepo.findAll();
    }

}
