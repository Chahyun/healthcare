package com.example.healthcare.repository;

import com.example.healthcare.domain.exercise.Exercise;
import com.example.healthcare.domain.enumType.exercise.ExerciseRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByUserIdAndExerciseDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    List<Exercise> findByExerciseDateBeforeAndStatus(LocalDateTime currentTime, ExerciseRole exerciseRole);

    List<Exercise> findAllByUserId(Long userId);

}
