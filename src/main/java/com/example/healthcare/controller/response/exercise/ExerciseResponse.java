package com.example.healthcare.controller.response.exercise;

import com.example.healthcare.domain.exercise.Exercise;
import com.example.healthcare.domain.enumType.exercise.ExerciseRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseResponse {
    private Long id;
    private String sports;
    private int weight;
    private int cnt;
    private int breakTime;
    private LocalDateTime exerciseDate;

    @Enumerated(EnumType.STRING)
    private ExerciseRole status;


    public static ExerciseResponse createFromExercise(Exercise exercise) {
        return ExerciseResponse.builder()
                .id(exercise.getId())
                .sports(exercise.getSports())
                .weight(exercise.getWeight())
                .cnt(exercise.getCnt())
                .breakTime(exercise.getBreakTime())
                .exerciseDate(exercise.getExerciseDate())
                .status(exercise.getStatus())
                .build();
    }

}
