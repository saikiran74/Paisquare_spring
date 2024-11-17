package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.model.Contactus;
import com.paisa_square.paisa.service.Contactusservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
public class Contactuscontrol {
    @Autowired
    private Contactusservice service;
    @PostMapping("/contactus")
    public Contactus contact(@RequestBody Contactus issue) throws Exception {
        Contactus saveissueobj=null;
        saveissueobj=service.saveissue(issue);
        if(issue==null){
            throw new Exception("Bad contactus details");
        }
        return saveissueobj;
    }
}
