package com.paisa_square.paisa.serice;

import com.paisa_square.paisa.model.Comments;
import com.paisa_square.paisa.model.Followers;
import com.paisa_square.paisa.model.Register;
import com.paisa_square.paisa.repository.Followerrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Followerservice {
    @Autowired
    private Followerrepository repo;
    public Followers savefollower(Followers follower) {
        return repo.save(follower);
    }
}
