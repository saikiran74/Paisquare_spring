package com.paisa_square.paisa.repository;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Followers;
import com.paisa_square.paisa.model.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface Followerrepository extends JpaRepository<Followers,Long> {
    @Query("SELECT DATE_FORMAT(f.opendate, '%M') AS month, COUNT(f) AS count FROM Followers f WHERE f.advertiser.id = :id GROUP BY DATE_FORMAT(f.opendate, '%M') ORDER BY f.opendate")
    List<Object[]> followersgraph(@Param("id") Long id);
    Optional<Followers> findByAdvertiserIdAndUserId(Long advertiserId, Long userId);

    List<Followers> findByUserId(Long userid);
}
