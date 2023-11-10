package com.paisa_square.paisa.serice;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Comments;
import com.paisa_square.paisa.model.Register;
import com.paisa_square.paisa.repository.Advertiserepository;
import com.paisa_square.paisa.repository.Commentrepository;
import com.paisa_square.paisa.repository.Registerrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Advertiseservice {
    @Autowired
    private Advertiserepository adrepo;
    @Autowired
    private Commentrepository commentrepo;
    public Advertise fetchId(Integer id){
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
}
