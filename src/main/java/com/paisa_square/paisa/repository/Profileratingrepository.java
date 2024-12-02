package com.paisa_square.paisa.repository;

import com.paisa_square.paisa.model.Likes;
import com.paisa_square.paisa.model.Profilerating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface Profileratingrepository  extends JpaRepository<Profilerating,Long> {

    Optional<Profilerating> findByUserIdAndAdvertiserId(@Param("user") Long user, @Param("advertiser") Long advertiser);

}