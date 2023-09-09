package com.example.healthcare.controller;

import com.example.healthcare.controller.request.exercise.ExerciseRequest;
import com.example.healthcare.controller.response.exercise.ExerciseResponse;
import com.example.healthcare.domain.memeber.Member;
import com.example.healthcare.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/exercise")
@RequiredArgsConstructor
@Slf4j
public class ExerciseController {

    private final ExerciseService exerciseService;

    @PostMapping("/register")
    public ResponseEntity<String> registerExercise(@AuthenticationPrincipal Member member,
                                                   @RequestBody List<ExerciseRequest>  requests) {
        exerciseService.registerExercise(member.getId(), requests);
        return ResponseEntity.ok("운동 계획이 등록되었습니다.");
    }



    /**
     * 하루의 운동 데이터 조회를 처리하는 엔드포인트입니다.
     *
     * @param member      현재 로그인한 회원 정보
     * @param selectDate  조회하려는 날짜
     * @return            해당 날짜의 운동 데이터 리스트 또는 예외 상태 응답
     */
    @GetMapping("")
    public ResponseEntity<List<ExerciseResponse>> myExerciseForDay(
            @AuthenticationPrincipal Member member,
            @RequestParam String selectDate) {

        List<ExerciseResponse> exerciseResponses = exerciseService.myExerciseForDate(member.getId(), selectDate);
        log.info(member.getId().toString()+"의 "+selectDate+" 날짜 조회");
        return ResponseEntity.ok(exerciseResponses);

    }

    /**
     * 주의 운동 데이터 조회를 처리하는 엔드포인트입니다.
     *
     * @param member      현재 로그인한 회원 정보
     * @param selectDate  조회하려는 날짜
     * @return            해당 주의 운동 데이터 리스트 또는 예외 상태 응답
     */
    @GetMapping("/week")
    public ResponseEntity<List<ExerciseResponse>> myExercisesForWeek(
            @AuthenticationPrincipal Member member,
            @RequestParam String selectDate) {

        log.info(member.getId().toString()+"의 "+selectDate+" 주 조회");
        return ResponseEntity.ok(exerciseService.myExerciseForWeek(member.getId(), selectDate));
    }

    /**
     * 월의 운동 데이터 조회를 처리하는 엔드포인트입니다.
     *
     * @param member      현재 로그인한 회원 정보
     * @param selectDate  조회하려는 날짜
     * @return            해당 월의 운동 데이터 리스트 또는 예외 상태 응답
     */
    @GetMapping("/month")
    public ResponseEntity<List<ExerciseResponse>> myExercisesForMonth(
            @AuthenticationPrincipal Member member,
            @RequestParam String selectDate) {

        log.info(member.getId().toString()+"의 "+selectDate+" 월 조회");
        return ResponseEntity.ok(exerciseService.myExerciseForMonth(member.getId(), selectDate));

    }

    /**
     * 특정 운동 데이터를 삭제하는 엔드포인트입니다.
     *
     * @param member      현재 로그인한 회원 정보
     * @param exerciseId  삭제하려는 운동 데이터의 ID
     * @return            삭제 결과 메시지 또는 예외 상태 응답
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteExercise(@AuthenticationPrincipal Member member,
                                                 @RequestParam Long exerciseId) {

        exerciseService.deleteExercise(member.getId(), exerciseId);
        return ResponseEntity.ok("삭제 되었습니다.");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateExercise(@AuthenticationPrincipal Member member,
                                                 @RequestParam Long exerciseId,
                                                 @RequestBody ExerciseRequest request) {

        exerciseService.updateExercise(member.getId(), exerciseId, request);
        return ResponseEntity.ok("수정 되었습니다.");
    }

    @PutMapping("/success")
    public ResponseEntity<String> successExercise(@AuthenticationPrincipal Member member,
                                                 @RequestParam Long exerciseId) {

        exerciseService.successExercise(member.getId(), exerciseId);
        return ResponseEntity.ok("고생 하셨습니다!");
    }


    @GetMapping("/public")
    public ResponseEntity<Map<String, List<ExerciseResponse>>> getPublicUserExercises(@RequestParam String selectDate) {
        log.info(selectDate.toString());
        return ResponseEntity.ok(exerciseService.getAllUserExercises(selectDate));
    }

    @GetMapping("/my-all-exercises")
    public ResponseEntity<List<ExerciseResponse>> getAllExercises(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(exerciseService.getAllExercises(member.getId()));
    }
}
