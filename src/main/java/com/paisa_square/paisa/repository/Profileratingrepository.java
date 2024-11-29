package com.paisa_square.paisa.repository;

import com.paisa_square.paisa.model.Profilerating;
import com.paisa_square.paisa.model.Register;
import com.paisa_square.paisa.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Profileratingrepository  extends JpaRepository<Profilerating,Long> {
    Optional<Profilerating> findByUserIdAndAdvertiserId(Long user_id,Long advertiser_id);

}
