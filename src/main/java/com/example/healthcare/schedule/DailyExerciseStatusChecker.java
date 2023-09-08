package com.example.healthcare.schedule;

import com.example.healthcare.domain.exercise.Exercise;
import com.example.healthcare.domain.enumType.exercise.ExerciseRole;
import com.example.healthcare.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyExerciseStatusChecker {

    final ExerciseRepository repository;
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void executeDailyExerciseStatusChecker() {
        List<Exercise> exercises = repository.findByExerciseDateBeforeAndStatus(
                LocalDateTime.now(), ExerciseRole.SCHEDULED_TO_BE_COMPLETED
        );
        log.info(exercises.toString());
        for(Exercise exercise : exercises) {
            exercise.setStatus(ExerciseRole.INCOMPLETE);
            log.info(exercise.toString());
            repository.save(exercise);
        }
    }

}
