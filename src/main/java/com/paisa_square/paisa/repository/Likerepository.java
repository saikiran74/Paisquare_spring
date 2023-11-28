package com.paisa_square.paisa.repository;

import com.paisa_square.paisa.model.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Likerepository extends JpaRepository<Likes,Long> {
    @Query("SELECT DATE_FORMAT(f.opendate, '%M') AS month, COUNT(f) AS count FROM Likes f WHERE f.advertiser.id = :id GROUP BY DATE_FORMAT(f.opendate, '%M') ORDER BY f.opendate")
    List<Object[]> likegraph(@Param("id") Long id);
}
