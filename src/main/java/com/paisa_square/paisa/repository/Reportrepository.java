package com.paisa_square.paisa.repository;

import com.paisa_square.paisa.model.Register;
import com.paisa_square.paisa.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface Reportrepository  extends JpaRepository<Report,Long> {
}
