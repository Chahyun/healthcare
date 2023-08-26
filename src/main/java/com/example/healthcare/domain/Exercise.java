package com.example.healthcare.domain;

import com.example.healthcare.domain.enumType.ExerciseRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String sports;
    private int weight;
    private int cnt;
    private int breakTime;
    private LocalDateTime exerciseDate;

    @Enumerated(EnumType.STRING)
    private ExerciseRole status;

}
