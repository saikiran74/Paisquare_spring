package com.paisa_square.paisa.repository;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Advertiserepository extends JpaRepository<Advertise,Long> {
    List<Advertise> findByadvertiserId(Integer userid);
}
