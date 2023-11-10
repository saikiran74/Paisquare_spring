package com.paisa_square.paisa.repository;

import com.paisa_square.paisa.model.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Likerepository extends JpaRepository<Likes,Long> {
}
