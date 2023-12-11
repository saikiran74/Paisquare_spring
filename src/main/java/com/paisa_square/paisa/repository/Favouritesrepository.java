package com.paisa_square.paisa.repository;

import com.paisa_square.paisa.model.Favourites;
import com.paisa_square.paisa.model.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface Favouritesrepository extends JpaRepository<Favourites,Long> {
    @Query("SELECT DATE_FORMAT(f.lastupdate, '%M') AS month, COUNT(f) AS count FROM Favourites f WHERE f.advertiser.id = :id and f.favourites=true GROUP BY DATE_FORMAT(f.lastupdate, '%M') ORDER BY f.lastupdate")
    List<Object[]> yearlygraph(@Param("id") Long id);
    @Query("SELECT CONCAT(day(a.lastupdate),'th') AS weekly, COUNT(a) AS count " +
            "FROM Favourites a " +
            "WHERE a.advertiser.id = :id AND a.favourites=true AND DATEDIFF(CURDATE(), a.lastupdate) <= 7" +
            "GROUP BY day(a.lastupdate) " +
            "ORDER BY day(a.lastupdate)")
    List<Object[]> weeklygraph(@Param("id") Long id);
    @Query("SELECT concat(day(a.lastupdate),'th') AS date, COUNT(a) AS count " +
            "FROM Favourites a " +
            "WHERE a.advertiser.id = :id AND a.favourites=true AND DATE_FORMAT(CURDATE(), '%Y%m')=DATE_FORMAT(a.lastupdate, '%Y%m')" +
            "GROUP BY day(a.lastupdate) " +
            "ORDER BY day(a.lastupdate)")
    List<Object[]> thismonth(@Param("id") Long id);
    @Query("SELECT concat(day(a.lastupdate),'th') AS date, COUNT(a) AS count " +
            "FROM Favourites a " +
            "WHERE a.advertiser.id = :id AND a.favourites=true AND " +
            "CONCAT(IF(MONTH(CURDATE()) = 1, 12, MONTH(CURDATE()) - 1),IF(MONTH(CURDATE()) = 1, \n" +
            " YEAR(CURDATE()) - 1, YEAR(CURDATE())))=CONCAT(MONTH(a.lastupdate),YEAR(a.lastupdate))" +
            "GROUP BY day(a.lastupdate) " +
            "ORDER BY day(a.lastupdate)")
    List<Object[]> lastmonth(@Param("id") Long id);

    Optional<Favourites> findByAdvertisementIdAndUserIdAndAdvertiserId(Long advertisementId, Long userId, Long advertiserId);

}
