package com.example.healthcare.controller.response.diet;

import com.example.healthcare.domain.diet.DietImageUrl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DietImgUrlResponse {
    private Long id;
    private Long dietId;
    private String imgUrl;


    public static DietImgUrlResponse createDietImgUrlResponse(DietImageUrl url){
        return DietImgUrlResponse.builder()
                .id(url.getId())
                .dietId(url.getDietId())
                .imgUrl(url.getImgUrl())
                .build();
    }
}
