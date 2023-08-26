package com.example.healthcare.controller;

import com.example.healthcare.controller.request.ExerciseRequest;
import com.example.healthcare.controller.response.ExerciseResponse;
import com.example.healthcare.domain.Member;
import com.example.healthcare.exception.CustomExceptions;
import com.example.healthcare.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/exercise")
@RequiredArgsConstructor
@Slf4j
public class ExerciseController {

    private final ExerciseService exerciseService;


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
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectDate) {

        try {
            List<ExerciseResponse> exerciseResponses = exerciseService.myExerciseForDate(member.getId(), selectDate);
            log.info(member.getId().toString()+"의 "+selectDate+" 날짜 조회");
            return ResponseEntity.ok(exerciseResponses);
        } catch (CustomExceptions.MemberNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (CustomExceptions.ExerciseNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 주의 운동 데이터 조회를 처리하는 엔드포인트입니다.
     *
     * @param member      현재 로그인한 회원 정보
     * @param selectDate  조회하려는 날짜
     * @return            해당 주의 운동 데이터 리스트 또는 예외 상태 응답
     */
    @GetMapping("/week")
    public ResponseEntity<List<ExerciseResponse>> myExercisesWeek(
            @AuthenticationPrincipal Member member,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectDate) {
        try {
            List<ExerciseResponse> exerciseResponses = exerciseService.myExerciseForWeek(member.getId(), selectDate);
            log.info(member.getId().toString()+"의 "+selectDate+" 주 조회");
            return ResponseEntity.ok(exerciseResponses);
        } catch (CustomExceptions.MemberNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (CustomExceptions.ExerciseNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 월의 운동 데이터 조회를 처리하는 엔드포인트입니다.
     *
     * @param member      현재 로그인한 회원 정보
     * @param selectDate  조회하려는 날짜
     * @return            해당 월의 운동 데이터 리스트 또는 예외 상태 응답
     */
    @GetMapping("/month")
    public ResponseEntity<List<ExerciseResponse>> myExercisesMonth(
            @AuthenticationPrincipal Member member,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectDate) {
        try {
            List<ExerciseResponse> exerciseResponses = exerciseService.myExerciseForMonth(member.getId(), selectDate);
            log.info(member.getId().toString()+"의 "+selectDate+" 월 조회");
            return ResponseEntity.ok(exerciseResponses);
        } catch (CustomExceptions.MemberNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (CustomExceptions.ExerciseNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
