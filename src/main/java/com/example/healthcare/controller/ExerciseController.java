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
import java.time.format.DateTimeParseException;
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
                                                   @RequestBody ExerciseRequest request) {
        try {
            exerciseService.registerExercise(member.getId(), request);
            return ResponseEntity.ok("운동 계획이 등록되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 ID가 null입니다.");
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 날짜 형식입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생하였습니다.");
        }
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
    public ResponseEntity<List<ExerciseResponse>> myExercisesForWeek(
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
    public ResponseEntity<List<ExerciseResponse>> myExercisesForMonth(
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
        try {
            exerciseService.deleteExercise(member.getId(), exerciseId);
            return ResponseEntity.ok("삭제 되었습니다.");
        } catch (CustomExceptions.ExerciseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 운동을 찾을 수 없습니다.");
        } catch (CustomExceptions.AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 운동의 삭제 권한이 없습니다.");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생 하였습니다.");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateExercise(@AuthenticationPrincipal Member member,
                                                 @RequestParam Long exerciseId,
                                                 @RequestBody ExerciseRequest request) {
        try {
            exerciseService.updateExercise(member.getId(), exerciseId, request);
            return ResponseEntity.ok("수정 되었습니다.");
        } catch (CustomExceptions.ExerciseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 운동을 찾을 수 없습니다.");
        } catch (CustomExceptions.AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 운동의 변경 권한이 없습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 날짜 형식입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생하였습니다.");
        }
    }

    @PutMapping("/success")
    public ResponseEntity<String> successExercise(@AuthenticationPrincipal Member member,
                                                 @RequestParam Long exerciseId) {
        try {
            exerciseService.successExercise(member.getId(), exerciseId);
            return ResponseEntity.ok("고생 하셨습니다!");
        } catch (CustomExceptions.ExerciseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 운동을 찾을 수 없습니다.");
        } catch (CustomExceptions.AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 운동의 변경 권한이 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생하였습니다.");
        }
    }


    @GetMapping("/public")
    public ResponseEntity<Map<String, List<ExerciseResponse>>> getPublicUserExercises(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectDate
    ) {
        try {
            log.info(selectDate.toString());
            Map<String, List<ExerciseResponse>> userExercisesMap = exerciseService.getAllUserExercises(selectDate);
            return ResponseEntity.ok(userExercisesMap);
        } catch (CustomExceptions.MemberNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
