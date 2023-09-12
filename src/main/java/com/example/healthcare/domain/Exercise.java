package com.example.healthcare.domain;

import com.example.healthcare.controller.request.ExerciseRequest;
import com.example.healthcare.domain.enumType.ExerciseRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String sports;
    private int weight;
    private int cnt;
    private int breakTime;
    private LocalDateTime exerciseDate;

    @Enumerated(EnumType.STRING)
    private ExerciseRole status;

    public static Exercise createExercise(Long userId, ExerciseRequest request, LocalDateTime exerciseDateTime){
        return Exercise.builder().
                userId(userId)
                .sports(request.getSports())
                .weight(request.getWeight())
                .cnt(request.getCnt())
                .breakTime(request.getBreakTime())
                .exerciseDate(exerciseDateTime)
                .status(ExerciseRole.SCHEDULED_TO_BE_COMPLETED)
                .build();
    }

    public static Exercise updateExercise(Exercise exercise, ExerciseRequest request, LocalDateTime exerciseDateTime){
        exercise.setSports(request.getSports());
        exercise.setCnt(request.getCnt());
        exercise.setBreakTime(request.getBreakTime());
        exercise.setWeight(request.getWeight());
        exercise.setExerciseDate(exerciseDateTime);
        return exercise;
    }
}
