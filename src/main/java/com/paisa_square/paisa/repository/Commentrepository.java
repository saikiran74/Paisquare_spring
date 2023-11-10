package com.paisa_square.paisa.repository;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Comments;
import com.paisa_square.paisa.model.Contactus;
import com.paisa_square.paisa.model.Register;
import jakarta.persistence.TypedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Commentrepository  extends JpaRepository<Comments,Long> {

    List<Comments> findByadvertiseId(Integer advertisementid);
    //TypedQuery<Comments> query=entityManager.createQuery("FROM Comments order by id",Comment.class)
    // remove(obj) this is to removed obj from database
}
