package com.paisa_square.paisa.service;

import com.paisa_square.paisa.model.Contactus;
import com.paisa_square.paisa.repository.Contactusrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Contactusservice {
    @Autowired
    private Contactusrepository issuerepo;
    public Contactus saveissue(Contactus issue) {
        return issuerepo.save(issue);
    }
}
