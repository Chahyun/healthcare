package com.example.healthcare.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardImageUrlRepository extends JpaRepository<BoardImageUrlRepository, Long> {
}
