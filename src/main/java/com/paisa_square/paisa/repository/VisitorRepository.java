package com.paisa_square.paisa.repository;

import com.paisa_square.paisa.model.Contactus;
import com.paisa_square.paisa.model.Visits;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitorRepository extends JpaRepository<Visits,Long> {
}
