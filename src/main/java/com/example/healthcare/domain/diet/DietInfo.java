package com.example.healthcare.domain.diet;

import com.example.healthcare.controller.request.diet.DietRequest;
import com.example.healthcare.domain.enumType.diet.DietStatusRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class DietInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long dietId;
    private String foodName;
    private Double carbohydrate;
    private Double protein;
    private Double unsaturatedFat;
    private Double transFat;
    private Double saturatedFat;
    private Double kcal;

    @Enumerated(EnumType.STRING)
    private DietStatusRole dietStatusRole;
    private String dietTime;


    public static DietInfo createDietInfo(Long dietId, DietRequest request){
        return DietInfo.builder()
                .dietId(dietId)
                .foodName(request.getFoodName())
                .carbohydrate(request.getCarbohydrate())
                .protein(request.getProtein())
                .unsaturatedFat(request.getUnsaturatedFat())
                .transFat(request.getTransFat())
                .saturatedFat(request.getSaturatedFat())
                .kcal(request.getKcal())
                .dietTime(request.getDietTime())
                .dietStatusRole(DietStatusRole.SCHEDULED_TO_BE_EAT)
                .build();

    }

}
