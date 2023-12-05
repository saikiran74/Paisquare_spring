package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Followers;
import com.paisa_square.paisa.model.Likes;
import com.paisa_square.paisa.model.Register;
import com.paisa_square.paisa.repository.Advertiserepository;
import com.paisa_square.paisa.repository.Likerepository;
import com.paisa_square.paisa.repository.Registerrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class Likecontrol {
    @Autowired
    private Registerrepository registorrepo;
    @Autowired
    private Advertiserepository adrepo;
    @Autowired
    private Likerepository likerepo;
    @PostMapping("{userid}/{advertisementid}/like")
    @CrossOrigin(origins = "http://localhost:4200/")
    public Likes like(@RequestBody Likes like,@PathVariable("userid") Long userid,@PathVariable("advertisementid") Long advertisementid) throws Exception{
        Likes likeobj=null;
        Optional<Advertise> advertismentmodel = adrepo.findById(advertisementid);
        Optional<Register> registermodel = registorrepo.findById(userid);
        if(advertismentmodel.isPresent()){
            Advertise advertise=advertismentmodel.get();
            Register register =registermodel.get();
            like.setAdvertisement(advertise);
            like.setUser(register);
            like.setAdvertiser(advertise.getAdvertiser());
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
            Advertise advertise2=advertismentmodel.get();
            Optional<Likes> likesModel = likerepo.findByAdvertisementIdAndUserIdAndAdvertiserId(advertisementid, userid,advertise2.getAdvertiser().getId());
            if (likesModel.isPresent()) {
                Likes likesobj = likesModel.get();
                if(likesobj.isLiked()){
                    likesobj.setLiked(false);
                    likesobj.setLastupdate(new Date());
                    likerepo.save(likesobj);
                }
                else{
                    likesobj.setLiked(true);
                    likesobj.setLastupdate(new Date());
                    likerepo.save(likesobj);
                }
            }
            else {
                like.setLiked(true);
                likeobj = likerepo.save(like);
            }
        }
        return likeobj;
    }
    @GetMapping("{userid}/likesgraph")
    @CrossOrigin(origins = "http://localhost:4200/")
    public List<Object[]> likegraph(@PathVariable("userid") Long userid) throws Exception {
        System.out.println(likerepo.likegraph(userid));
        return likerepo.likegraph(userid);
    }
    @GetMapping("/{userid}/getlikedadvertisementslist")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Advertise> getlikedadvertisementslist(@PathVariable("userid") Integer userid) {
        System.out.println(adrepo.findAllBylikes(userid));
        return adrepo.findAllBylikes(userid);
    }
}