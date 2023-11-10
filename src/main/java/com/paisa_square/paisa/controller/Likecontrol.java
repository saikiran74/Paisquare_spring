package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Likes;
import com.paisa_square.paisa.repository.Advertiserepository;
import com.paisa_square.paisa.repository.Likerepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class Likecontrol {
    @Autowired
    private Advertiserepository adrepo;
    @Autowired
    private Likerepository likerepo;
    @PostMapping("{userid}/{advertisementid}/like")
    @CrossOrigin(origins = "http://localhost:4200/")
    public Likes like(@RequestBody Likes like,@PathVariable("userid") Long userid,@PathVariable("advertisementid") Long advertisementid) throws Exception{
        Likes likeobj=null;
        Optional<Advertise> advertismentmodel = adrepo.findById(advertisementid);
        if(advertismentmodel.isPresent()){
            Advertise advertise=advertismentmodel.get();
            if(advertise.getLikes().contains(userid)) {
                System.out.println("Already user like this"+advertisementid);
                advertise.getLikes().remove(userid);
                adrepo.save(advertise);
            }
            else{
                System.out.println("user not liked this"+advertisementid);
                advertise.getLikes().add(userid);
                adrepo.save(advertise);
            }
            likeobj=likerepo.save(like);
        }
        return likeobj;
    }
}