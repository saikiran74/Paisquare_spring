package com.paisa_square.paisa.service;

import com.paisa_square.paisa.model.Followers;
import com.paisa_square.paisa.model.Register;
import com.paisa_square.paisa.repository.Followerrepository;
import com.paisa_square.paisa.repository.Registerrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Followerservice {
    @Autowired
    private Followerrepository repo;
    @Autowired
    private Registerrepository registerrepo;
    public Followers savefollower(Followers follower) {
        return repo.save(follower);
    }
    public Optional<Register> followerlist(Long userid){
        return registerrepo.findByUserId(userid);
    }
}
