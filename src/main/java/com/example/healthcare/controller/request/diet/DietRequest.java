package com.example.healthcare.controller.request.diet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DietRequest {


    private String foodName;
    private Double carbohydrate;
    private Double protein;
    private Double unsaturatedFat;
    private Double transFat;
    private Double saturatedFat;
    private Double kcal;
    private String dietTime;
}
