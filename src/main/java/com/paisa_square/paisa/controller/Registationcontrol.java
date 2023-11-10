package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.serice.Registerservice;
import com.paisa_square.paisa.model.Register;
import org.hibernate.service.spi.InjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class Registationcontrol {
    @Autowired
    private Registerservice service;
    @PostMapping("/registeruser")
    @CrossOrigin(origins = "http://localhost:4200/")
    public Register registerUser(@RequestBody Register user) throws Exception {
        String tempEmailId = user.getEmail();
        System.out.println("____________________________________++++++++++++++++++");
        if(tempEmailId!=null && !"".equals(tempEmailId)){
            Register userobj=service.fetchUserByEmailId(tempEmailId);
            if(userobj!=null){
                throw new Exception("emailid is exist");
            }
        }
        Register userobj=null;
        userobj=service.saveUser(user);
        return userobj;
    }
    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:4200/")
    public Register loginUser(@RequestBody Register login) throws Exception {
        String tempEmailId=login.getEmail();
        String tempPass=login.getPassword();
        System.out.println("sai");
        Register userObj=null;
        if(tempEmailId!=null && tempPass!=null){
            userObj=service.fetchUserByEmailIdAndPassword(tempEmailId,tempPass);
        }
        if(userObj==null){
            throw new Exception("Bad email and password"+tempEmailId+tempPass);
        }
        return userObj;
    }

}
