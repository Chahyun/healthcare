package com.example.healthcare.controller.response;

import com.example.healthcare.domain.Exercise;
import com.example.healthcare.domain.enumType.ExerciseRole;
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
    private String sports;
    private int weight;
    private int cnt;
    private int breakTime;
    private LocalDateTime exerciseDate;

    @Enumerated(EnumType.STRING)
    private ExerciseRole status;


    public static ExerciseResponse createFromExercise(Exercise exercise) {
        return ExerciseResponse.builder()
                .sports(exercise.getSports())
                .weight(exercise.getWeight())
                .cnt(exercise.getCnt())
                .breakTime(exercise.getBreakTime())
                .exerciseDate(exercise.getExerciseDate())
                .status(exercise.getStatus())
                .build();
    }

}
