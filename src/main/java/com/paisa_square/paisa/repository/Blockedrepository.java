package com.paisa_square.paisa.repository;

import com.paisa_square.paisa.model.Blockedadvertiser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface Blockedrepository  extends JpaRepository<Blockedadvertiser,Long> {
    Optional<Blockedadvertiser> findByAdvertiserIdAndUserId(Long advertiserId, Long userId);



}
