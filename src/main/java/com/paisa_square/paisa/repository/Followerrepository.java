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
    @Query("SELECT DATE_FORMAT(f.lastupdate, '%M') AS month, COUNT(f) AS count FROM Followers f WHERE f.advertiser.id = :id and f.following=true GROUP BY DATE_FORMAT(f.lastupdate, '%M') ORDER BY f.lastupdate")
    List<Object[]> yearlygraph(@Param("id") Long id);
    @Query("SELECT CONCAT(day(a.lastupdate),'th') AS weekly, COUNT(a) AS count " +
            "FROM Followers a " +
            "WHERE a.advertiser.id = :id AND a.following=true AND DATEDIFF(CURDATE(), a.lastupdate) <= 7" +
            "GROUP BY day(a.lastupdate) " +
            "ORDER BY day(a.lastupdate)")
    List<Object[]> weeklygraph(@Param("id") Long id);
    @Query("SELECT concat(day(a.lastupdate),'th') AS date, COUNT(a) AS count " +
            "FROM Followers a " +
            "WHERE a.advertiser.id = :id AND a.following=true AND DATE_FORMAT(CURDATE(), '%Y%m')=DATE_FORMAT(a.lastupdate, '%Y%m')" +
            "GROUP BY day(a.lastupdate) " +
            "ORDER BY day(a.lastupdate)")
    List<Object[]> thismonth(@Param("id") Long id);
    @Query("SELECT concat(day(a.lastupdate),'th') AS date, COUNT(a) AS count " +
            "FROM Followers a " +
            "WHERE a.advertiser.id = :id AND a.following=true AND " +
            "CONCAT(IF(MONTH(CURDATE()) = 1, 12, MONTH(CURDATE()) - 1),IF(MONTH(CURDATE()) = 1, \n" +
            " YEAR(CURDATE()) - 1, YEAR(CURDATE())))=CONCAT(MONTH(a.lastupdate),YEAR(a.lastupdate))" +
            "GROUP BY day(a.lastupdate) " +
            "ORDER BY day(a.lastupdate)")
    List<Object[]> lastmonth(@Param("id") Long id);

    @Query("SELECT concat(day(a.lastupdate),'th') AS date, COUNT(a) AS count " +
            "FROM Followers a " +
            "WHERE a.advertiser.id = :id AND a.following=true AND " +
            "DATE_FORMAT(CURDATE(), '%d')=DATE_FORMAT(a.lastupdate, '%d')" +
            "GROUP BY day(a.lastupdate) " +
            "ORDER BY day(a.lastupdate)")
    List<Object[]> today(@Param("id") Long id);

    Optional<Followers> findByAdvertiserIdAndUserId(Long advertiserId, Long userId);


}
