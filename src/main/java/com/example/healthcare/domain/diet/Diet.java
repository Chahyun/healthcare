package com.example.healthcare.domain.diet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Diet {

    @Id
    private Long id;
    private Long userId;
    private LocalDate dietDate;


    public static Diet createDiet(Long id, Long userId, LocalDate dietDate){
        return Diet.builder()
                .id(id)
                .userId(userId)
                .dietDate(dietDate)
                .build();

    }
}
