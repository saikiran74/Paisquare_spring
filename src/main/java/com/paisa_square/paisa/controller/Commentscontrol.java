package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Comments;
import com.paisa_square.paisa.repository.Advertiserepository;
import com.paisa_square.paisa.repository.Commentrepository;
import com.paisa_square.paisa.serice.Commentservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class Commentscontrol {
    @Autowired
    private Advertiserepository adrepo;
    @Autowired
    private Commentservice service;
    @Autowired
    private Commentrepository commentrepo;
    @PostMapping("{userid}/{advertisementid}/comments")
    @CrossOrigin(origins = "http://localhost:4200/")
    public Comments comment(@RequestBody Comments comment,@PathVariable("userid") Long userid,@PathVariable("advertisementid") Integer advertisementid) throws Exception {
        Optional<Advertise> advertisemodel = Optional.ofNullable(service.fetchId(advertisementid));
        if (advertisemodel.isPresent()) {
            Advertise advertisement = advertisemodel.get();
            advertisement.getCommenteduser().add(userid);
            adrepo.save(advertisement);
            comment.setAdvertise(advertisement);
            service.savecomment(comment);
        } else {
            throw new IllegalArgumentException("Advertisement not found with id: " + 1);
        }
        return comment;
    }
    @GetMapping("/commentslist")
    @CrossOrigin(origins = "http://localhost:4200/")
    public List<Comments> getAllComments() {
        return commentrepo.findAll();
    }
}
