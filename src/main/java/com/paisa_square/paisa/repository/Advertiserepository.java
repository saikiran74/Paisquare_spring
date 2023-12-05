package com.paisa_square.paisa.repository;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Comments;
import com.paisa_square.paisa.model.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface Advertiserepository extends JpaRepository<Advertise,Long> {
    List<Advertise> findByadvertiserId(Integer userid);

    List<Advertise> findAllBylikes(Integer userid);


    List<Advertise> findByfavourites(Integer userid);


    List<Advertise> findAllByvisiteduser(Integer userid);


    @Query(value = "SELECT * FROM Register r WHERE r.following = :userId", nativeQuery = true)
    List<Advertise> findAllByFollowerId(@Param("userId") Long userId);
}
