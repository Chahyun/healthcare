package com.example.healthcare.repository;

import com.example.healthcare.domain.Diet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {
    Optional<Diet>  findFirstByOrderByIdDesc();
}
