package com.paisa_square.paisa.repository;

import com.paisa_square.paisa.model.Contactus;
import com.paisa_square.paisa.model.Visits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;

@EnableJpaRepositories
public interface VisitorRepository extends JpaRepository<Visits,Long> {
    @Query("SELECT DATE_FORMAT(v.opendate, '%M') AS month, COUNT(v) AS count FROM Visits v WHERE v.advertiser.id = :id GROUP BY DATE_FORMAT(v.opendate, '%M') ORDER BY v.opendate")
    List<Object[]> visitorsgraph(@Param("id") Long id);
}
