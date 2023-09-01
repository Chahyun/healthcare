package com.example.healthcare.domain;

import com.example.healthcare.controller.request.DietRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Diet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String foodName;
    private Double carbohydrate;
    private Double protein;
    private Double unsaturatedFat;
    private Double transFat;
    private Double saturatedFat;
    private Double kcal;
    private LocalDateTime dietDate;


    public static Diet createDiet(Long userId, DietRequest request, LocalDateTime dietDate){
        return Diet.builder()
                .userId(userId)
                .foodName(request.getFoodName())
                .carbohydrate(request.getCarbohydrate())
                .protein(request.getProtein())
                .unsaturatedFat(request.getUnsaturatedFat())
                .transFat(request.getTransFat())
                .saturatedFat(request.getSaturatedFat())
                .kcal(request.getKcal())
                .dietDate(dietDate)
                .build();

    }
}
