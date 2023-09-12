package com.example.healthcare.domain.diet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
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
