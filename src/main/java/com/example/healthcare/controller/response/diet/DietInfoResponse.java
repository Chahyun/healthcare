package com.example.healthcare.controller.response.diet;

import com.example.healthcare.domain.diet.DietInfo;
import com.example.healthcare.domain.enumType.diet.DietStatusRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Enumerated;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DietInfoResponse {

    private Long id;
    private Long dietId;
    private String foodName;
    private Double carbohydrate;
    private Double protein;
    private Double unsaturatedFat;
    private Double transFat;
    private Double saturatedFat;
    private Double kcal;
    private String dietTime;
    @Enumerated
    private DietStatusRole dietStatusRole;

    public static DietInfoResponse createDietResponse(DietInfo dietInfo){
        return DietInfoResponse.builder()
                .id(dietInfo.getId())
                .dietId(dietInfo.getDietId())
                .foodName(dietInfo.getFoodName())
                .carbohydrate(dietInfo.getCarbohydrate())
                .protein(dietInfo.getProtein())
                .unsaturatedFat(dietInfo.getUnsaturatedFat())
                .transFat(dietInfo.getTransFat())
                .saturatedFat(dietInfo.getSaturatedFat())
                .kcal(dietInfo.getKcal())
                .dietTime(dietInfo.getDietTime())
                .dietStatusRole(dietInfo.getDietStatusRole())
                .build();
    }

}
