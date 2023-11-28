package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.model.*;
import com.paisa_square.paisa.repository.Advertiserepository;
import com.paisa_square.paisa.repository.Followerrepository;
import com.paisa_square.paisa.repository.Registerrepository;
import com.paisa_square.paisa.serice.Followerservice;
import com.paisa_square.paisa.serice.Registerservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
@RestController
public class Followercontrol {
    @Autowired
    private Followerservice service;
    @Autowired
    private Registerservice registerservice;
    @Autowired
    private Followerrepository followersrepo;
    @Autowired
    private Registerrepository Registerrepo;
    @Autowired
    private Advertiserepository adrepo;

    @PostMapping("/{userid}/{advertiserid}/follow")
    @CrossOrigin(origins = "http://localhost:4200/")
    public Followers comment(@RequestBody Followers follow, @PathVariable("advertiserid") Long advertiserid,@PathVariable("userid") Long userid) throws Exception {
        Optional<Register> registermodel = registerservice.fetchId(userid);
        Optional<Register> advertisermodel = registerservice.fetchId(advertiserid);
        if (registermodel.isPresent()) {
            Register register = registermodel.get();
            Register advertiser = advertisermodel.get();
            follow.setAdvertiser(advertiser);
            follow.setUser(register);
            if(register.getFollowing().contains(advertiserid)){
                System.out.println("advertiserid exist in the register");
                register.getFollowing().remove(advertiserid);
                Registerrepo.save(register);
            }
            else{
                System.out.println("advertiserid not  exist saving the register");
                register.getFollowing().add(advertiserid);
                Registerrepo.save(register);
            }
        }
        System.out.println("advertisment id not exits"+userid);
        followersrepo.save(follow);
        return follow;
    }
    @GetMapping("{userid}/userdata")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Register> getfollowers(@PathVariable("userid") Long userid){
        System.out.println("follower"+Registerrepo.findById(userid));
        return Collections.singletonList(Registerrepo.findById(userid).orElse(null));
    }

    @GetMapping("{userid}/followersgraph")
    @CrossOrigin(origins = "http://localhost:4200/")
    public List<Object[]> followersgraph(@PathVariable("userid") Long userid) throws Exception {
        System.out.println(followersrepo.followersgraph(userid));
        return followersrepo.followersgraph(userid);
    }

}
