package com.paisa_square.paisa.serice;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Comments;
import com.paisa_square.paisa.model.Register;
import com.paisa_square.paisa.repository.Advertiserepository;
import com.paisa_square.paisa.repository.Commentrepository;
import com.paisa_square.paisa.repository.Contactusrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Commentservice {
    @Autowired
    private Commentrepository commentrepo;
    @Autowired
    private Advertiserepository adrepo;
    public Advertise fetchId(Integer id){
        return adrepo.findById(id);
    }
    public Comments savecomment(Comments comment) {
        return commentrepo.save(comment);
    }
    public Advertise saveintoad(Advertise advertise){
        return adrepo.save(advertise);
    }


}
