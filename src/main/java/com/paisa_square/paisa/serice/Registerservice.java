package com.paisa_square.paisa.serice;

import com.paisa_square.paisa.repository.Registerrepository;
import com.paisa_square.paisa.model.Register;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Registerservice {
    @Autowired
    private Registerrepository repo;
    public Register saveUser(Register user){
        return repo.save(user);
    }
    public Register fetchUserByEmailId(String email){
        return repo.findByEmail(email);
    }
    public Register fetchUserByEmailIdAndPassword(String email,String password){
        return repo.findByEmailAndPassword(email,password);
    }

    public Optional<Register> fetchId(Long id){
        return repo.findById(id);
    }
}
