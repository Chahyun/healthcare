package com.example.healthcare.repository;

import com.example.healthcare.domain.diet.Diet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {
    Optional<Diet>  findFirstByOrderByIdDesc();

    List<Diet> findByUserIdAndDietDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    List<Diet> findByUserId(Long userId);

    List<Diet> findByUserIdAndId(Long userId, Long dietId);


}
