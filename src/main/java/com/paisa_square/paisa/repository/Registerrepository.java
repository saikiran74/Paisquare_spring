package com.paisa_square.paisa.repository;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface Registerrepository extends JpaRepository<Register,Long> {
    Register findByEmail(String email);
    Register findByEmailAndPassword(String email,String password);

    @Query("SELECT a FROM Register a " +
            "INNER JOIN Followers f ON a.id = f.advertiser.id " +
            "WHERE f.user.id = :userId AND f.following = true")
    List<Register> findAllProfilesUserFollowing(@Param("userId") Long userId);

    @Query("SELECT a FROM Register a " +
            "INNER JOIN Blockedadvertiser f ON a.id = f.advertiser.id " +
            "WHERE f.user.id = :userId AND f.blocked = true")
    List<Register> findAllProfilesUserBlocker(@Param("userId") Long userId);
}
