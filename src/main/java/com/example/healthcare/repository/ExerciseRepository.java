package com.example.healthcare.repository;

import com.example.healthcare.domain.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByUserIdAndExerciseDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
