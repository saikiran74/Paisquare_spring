package com.paisa_square.paisa.repository;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Advertisementtransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Advertisementtransactionrepository extends JpaRepository<Advertisementtransaction,Long> {
    List<Advertisementtransaction> findAllByadvertiserId(Integer userid);
}
