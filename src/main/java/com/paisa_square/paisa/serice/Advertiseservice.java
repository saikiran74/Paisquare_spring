package com.paisa_square.paisa.serice;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Comments;
import com.paisa_square.paisa.repository.Advertiserepository;
import com.paisa_square.paisa.repository.Commentrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Advertiseservice {
    @Autowired
    private Advertiserepository adrepo;
    @Autowired
    private Commentrepository commentrepo;
    public Optional<Advertise> fetchId(Long id){
        return adrepo.findById(id);
    }
    public Advertise savead(Advertise ad){
        return adrepo.save(ad);
    }
    public List<Advertise> findAlladvertisement(){
        return adrepo.findAll();
    }
    public List<Comments> findByadvertisementid(Integer advertisementid){
        return commentrepo.findByadvertiseId(advertisementid);
    }
    public List<Advertise> findAllByadvertiserId(Integer userid){
        return adrepo.findByadvertiserId(userid);
    }
}
