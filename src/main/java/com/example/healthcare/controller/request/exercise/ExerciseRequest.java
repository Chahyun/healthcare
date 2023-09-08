package com.example.healthcare.controller.request.exercise;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseRequest {
    private String sports;
    private int weight;
    private int cnt;
    private int breakTime;
    private String exerciseDate;


}
