package com.paisa_square.paisa.repository;

import com.paisa_square.paisa.model.Contactus;
import com.paisa_square.paisa.model.Visits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface VisitorRepository extends JpaRepository<Visits,Long> {
    @Query("SELECT DATE_FORMAT(v.lastupdate, '%M') AS month, COUNT(v) AS count FROM Visits v WHERE v.advertiser.id = :id and v.visited=true GROUP BY DATE_FORMAT(v.lastupdate, '%M') ORDER BY v.lastupdate")
    List<Object[]> yearlygraph(@Param("id") Long id);

    @Query("SELECT CONCAT(day(a.lastupdate),'th') AS weekly, COUNT(a) AS count " +
            "FROM Visits a " +
            "WHERE a.advertiser.id = :id AND a.visited=true AND DATEDIFF(CURDATE(), a.lastupdate) <= 7" +
            "GROUP BY day(a.lastupdate) " +
            "ORDER BY day(a.lastupdate)")
    List<Object[]> weeklygraph(@Param("id") Long id);

    @Query("SELECT concat(day(a.lastupdate),'th') AS date, COUNT(a) AS count " +
            "FROM Visits a " +
            "WHERE a.advertiser.id = :id AND a.visited=true AND DATE_FORMAT(CURDATE(), '%Y%m')=DATE_FORMAT(a.lastupdate, '%Y%m')" +
            "GROUP BY day(a.lastupdate) " +
            "ORDER BY day(a.lastupdate)")
    List<Object[]> thismonth(@Param("id") Long id);
    @Query("SELECT concat(day(a.lastupdate),'th') AS date, COUNT(a) AS count " +
            "FROM Visits a " +
            "WHERE a.advertiser.id = :id AND a.visited=true AND " +
            "CONCAT(IF(MONTH(CURDATE()) = 1, 12, MONTH(CURDATE()) - 1),IF(MONTH(CURDATE()) = 1, \n" +
            " YEAR(CURDATE()) - 1, YEAR(CURDATE())))=CONCAT(MONTH(a.lastupdate),YEAR(a.lastupdate))" +
            "GROUP BY day(a.lastupdate) " +
            "ORDER BY day(a.lastupdate)")
    List<Object[]> lastmonth(@Param("id") Long id);

    Optional<Visits> findByUseridAndAdvertisement_Id(String userid, Long advertisementId);
}
