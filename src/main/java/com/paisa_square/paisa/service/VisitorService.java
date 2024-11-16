package com.paisa_square.paisa.service;

import com.paisa_square.paisa.model.Visits;
import com.paisa_square.paisa.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisitorService {
    @Autowired
    private VisitorRepository visitrepo;
    public Visits savevisitor(Visits visit) {
        return visitrepo.save(visit);
    }
}
