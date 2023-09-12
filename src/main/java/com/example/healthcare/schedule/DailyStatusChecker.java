package com.example.healthcare.schedule;

import com.example.healthcare.domain.diet.Diet;
import com.example.healthcare.domain.diet.DietInfo;
import com.example.healthcare.domain.enumType.diet.DietStatusRole;
import com.example.healthcare.domain.exercise.Exercise;
import com.example.healthcare.domain.enumType.exercise.ExerciseRole;
import com.example.healthcare.repository.diet.DietInfoRepository;
import com.example.healthcare.repository.diet.DietRepository;
import com.example.healthcare.repository.exercise.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyStatusChecker {

    final ExerciseRepository exerciseRepository;
    final DietInfoRepository dietInfoRepository;
    final DietRepository dietRepository;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void executeDailyExerciseStatusChecker() {
        List<Exercise> exercises = exerciseRepository.findByExerciseDateBeforeAndStatus(
                LocalDateTime.now(), ExerciseRole.SCHEDULED_TO_BE_COMPLETED
        );
        log.info(exercises.toString());
        for(Exercise exercise : exercises) {
            exercise.setStatus(ExerciseRole.INCOMPLETE);
            log.info(exercise.toString());
            exerciseRepository.save(exercise);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void executeDailyDietStatusChecker() {
        List<Diet> diets = dietRepository.findByDietDateBefore(LocalDate.now());
        for(Diet diet : diets){
            List<DietInfo> dietInfos = dietInfoRepository.findAllByDietIdAndDietStatusRole(diet.getId(), DietStatusRole.SCHEDULED_TO_BE_EAT);
            for(DietInfo dietInfo : dietInfos){
                dietInfo.setDietStatusRole(DietStatusRole.INCOMPLETE);
                dietInfoRepository.save(dietInfo);
            }
        }
    }
}
