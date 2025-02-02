package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Comments;
import com.paisa_square.paisa.model.Register;
import com.paisa_square.paisa.repository.Advertiserepository;
import com.paisa_square.paisa.repository.Commentrepository;
import com.paisa_square.paisa.repository.Registerrepository;
import com.paisa_square.paisa.service.Commentservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "${cors.allowedOrigins}")
public class Commentscontrol {
    @Autowired
    private Advertiserepository adrepo;
    @Autowired
    private Registerrepository registerRepo;
    @Autowired
    private Commentservice commentservice;
    @Autowired
    private Commentrepository commentrepo;
    @PostMapping("/comments/{userid}/{advertisementid}")
    public Comments comment(@RequestBody Comments comment,@PathVariable("userid") Long userid,@PathVariable("advertisementid") Long advertisementid) throws Exception {
        Optional<Advertise> advertisemodel = commentservice.fetchId(advertisementid);
        Advertise advertisement = advertisemodel.get();
        Optional<Register> advertiserIdModel= registerRepo.findById(advertisement.getAdvertiser().getId());
        Register advertiserInRegister=advertiserIdModel.get();
        advertisement.getCommenteduser().add(userid);
        //Updating comments count in advertise table
        advertiserInRegister.setNoOfComments(advertiserInRegister.getNoOfComments()+1);
        advertisement.setCommentscount(advertisement.getCommentscount()+1);
        registerRepo.save(advertiserInRegister);
        adrepo.save(advertisement);
        comment.setAdvertise(advertisement);
        commentservice.savecomment(comment);
        return comment;
    }
    @GetMapping("/commentslist")
    public List<Comments> getAllComments() {
        return commentrepo.findAll();
    }

}
