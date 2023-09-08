package com.example.healthcare.controller;

import com.example.healthcare.controller.request.diet.DietRequest;
import com.example.healthcare.controller.response.diet.DietWithImgResponse;
import com.example.healthcare.domain.memeber.Member;
import com.example.healthcare.exception.CustomExceptions;
import com.example.healthcare.service.DietService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diet")
@Slf4j
public class DietController {

    private final DietService dietService;
    @PostMapping("register")
    public ResponseEntity<String> registerDiet(@AuthenticationPrincipal Member member,
                                               @RequestPart("dietDate") String dietDate,
                                               @RequestPart("dietRequests")List<DietRequest> dietRequests,
                                               @RequestPart(value = "files", required = false)List<MultipartFile> files){

        try {
            dietService.registerDiet(member.getId(), dietDate,dietRequests, files);
            return ResponseEntity.ok("식단등록이 완료 되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 ID가 null입니다.");
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 날짜 형식입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생하였습니다.");
        }
    }

    @GetMapping("")
    public ResponseEntity<DietWithImgResponse> myDietForDate(
            @AuthenticationPrincipal Member member,
            @RequestParam String selectDate) {
        return handleDietResponse(member, () -> dietService.myDietForDate(member.getId(), selectDate));
    }


    @GetMapping("/week")
    public ResponseEntity<DietWithImgResponse> myDietForWeek(
            @AuthenticationPrincipal Member member,
            @RequestParam String selectDate) {
        return handleDietResponse(member, () -> dietService.myDietForWeek(member.getId(), selectDate));
    }

    @GetMapping("/month")
    public ResponseEntity<DietWithImgResponse> myDietForMonth(
            @AuthenticationPrincipal Member member,
            @RequestParam String selectDate) {
        return handleDietResponse(member, () -> dietService.myDietForMonth(member.getId(), selectDate));
    }


    @GetMapping("/my_all_diets")
    public ResponseEntity<DietWithImgResponse> getAllDiets(@AuthenticationPrincipal Member member) {
        return handleDietResponse(member, () -> dietService.getAllDiets(member.getId()));
    }

    @GetMapping("/my_diet_detail/{dietId}")
    public ResponseEntity<DietWithImgResponse> getDetailDiets(
            @AuthenticationPrincipal Member member,
            @PathVariable Long dietId) {
        return handleDietResponse(member, () -> dietService.getDetailDiets(member.getId(), dietId));
    }

    @PutMapping("/success")
    public ResponseEntity<String> dietSuccess(
            @AuthenticationPrincipal Member member,
            @RequestParam Long dietId,
            @RequestParam Long dietInfoId
    ){
        try {
            dietService.dietSuccess(member.getId(), dietId,dietInfoId);
            return ResponseEntity.ok("고생하셨습니다!!");
        } catch (CustomExceptions.DietNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("식단 정보를 찾을 수 없습니다.");
        } catch (CustomExceptions.AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한이 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 에러가 발생하였습니다.");
        }
    }

    @PutMapping("/my_diet_detail/{dietId}/update")
    public ResponseEntity<String> updateDiet(
            @AuthenticationPrincipal Member member,
            @PathVariable Long dietId,
            @RequestPart("dietDate") String dietDate,
            @RequestPart("dietRequests")List<DietRequest> dietRequests,
            @RequestPart(value = "files", required = false)List<MultipartFile> files
    ) {
        try {

            dietService.updateDiet(member.getId(), dietId, dietDate, dietRequests, files);
            return ResponseEntity.ok("식단 정보가 수정 되었습니다.");
        } catch(CustomExceptions.DietNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("식단 정보를 찾을 수 없습니다.");
        } catch(CustomExceptions.AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한이 없습니다.");
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 에러가 발생하였습니다.");
        }
    }

    @DeleteMapping("/my_diet_detail/{dietId}/delete")
    public ResponseEntity<String> deleteDiet(@AuthenticationPrincipal Member member,
                                             @PathVariable Long dietId){
        try {
            dietService.deleteDiet(member.getId(), dietId);
            return ResponseEntity.ok("식단 삭제가 완료 되었습니다.");
        }  catch(CustomExceptions.DietNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("식단 정보를 찾을 수 없습니다.");
        } catch(CustomExceptions.AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 에러가 발생하였습니다.");
        }
    }

    @GetMapping("/public")
    public ResponseEntity<List<Map<String,DietWithImgResponse>>> getPublicUserDiet(){
        try {
            return ResponseEntity.ok(dietService.getAllUserDiets());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private ResponseEntity<DietWithImgResponse> handleDietResponse(Member member, Supplier<DietWithImgResponse> supplier) {
        try {
            return ResponseEntity.ok(supplier.get());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
