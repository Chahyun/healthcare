package com.example.healthcare.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DietImageUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long dietId;
    private String imgUrl;


    public static DietImageUrl createDietImageUrl(Long dietId, String imgUrl){
        return DietImageUrl.builder()
                .dietId(dietId)
                .imgUrl(imgUrl)
                .build();
    }
}
