package com.paisa_square.paisa.repository;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Contactus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Contactusrepository extends JpaRepository<Contactus,Long> {

        //List<Advertise> find
}
