package com.example.healthcare.controller;

import com.example.healthcare.controller.request.diet.DietRequest;
import com.example.healthcare.controller.response.diet.DietWithImgResponse;
import com.example.healthcare.domain.memeber.Member;
import com.example.healthcare.service.DietService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diet")
@Slf4j
public class DietController {

    private final DietService dietService;
    @PostMapping("/register")
    public ResponseEntity<String> registerDiet(@AuthenticationPrincipal Member member,
                                               @RequestPart("dietDate") String dietDate,
                                               @RequestPart("dietRequests") List<DietRequest> dietRequests,
                                               @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        dietService.registerDiet(member.getId(), dietDate, dietRequests, files);
        return ResponseEntity.ok("식단 등록이 완료 되었습니다.");
    }


    @GetMapping("")
    public ResponseEntity<DietWithImgResponse> myDietForDate(
            @AuthenticationPrincipal Member member,
            @RequestParam String selectDate) {
        return ResponseEntity.ok(dietService.myDietForDate(member.getId(), selectDate));
    }


    @GetMapping("/week")
    public ResponseEntity<DietWithImgResponse> myDietForWeek(
            @AuthenticationPrincipal Member member,
            @RequestParam String selectDate) {
        return ResponseEntity.ok(dietService.myDietForWeek(member.getId(), selectDate));
    }

    @GetMapping("/month")
    public ResponseEntity<DietWithImgResponse> myDietForMonth(
            @AuthenticationPrincipal Member member,
            @RequestParam String selectDate) {
        return ResponseEntity.ok(dietService.myDietForMonth(member.getId(), selectDate));
    }


    @GetMapping("/my-all-diets")
    public ResponseEntity<DietWithImgResponse> getAllDiets(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(dietService.getAllDiets(member.getId()));
    }

    @GetMapping("/my-diet-detail/{dietId}")
    public ResponseEntity<DietWithImgResponse> getDetailDiets(
            @AuthenticationPrincipal Member member,
            @PathVariable Long dietId) {
        return ResponseEntity.ok(dietService.getDetailDiets(member.getId(), dietId));
    }

    @PutMapping("/success")
    public ResponseEntity<String> dietSuccess(
            @AuthenticationPrincipal Member member,
            @RequestParam Long dietId,
            @RequestParam Long dietInfoId
    ){
        dietService.dietSuccess(member.getId(), dietId,dietInfoId);
        return ResponseEntity.ok("고생하셨습니다!!");
    }


    @PutMapping("/my-diet-detail/{dietId}/update")
    public ResponseEntity<String> updateDiet(
            @AuthenticationPrincipal Member member,
            @PathVariable Long dietId,
            @RequestPart("dietDate") String dietDate,
            @RequestPart("dietRequests")List<DietRequest> dietRequests,
            @RequestPart(value = "files", required = false)List<MultipartFile> files
    ) {
        dietService.updateDiet(member.getId(), dietId, dietDate, dietRequests, files);
        return ResponseEntity.ok("식단 정보가 수정 되었습니다.");
    }


    @DeleteMapping("/my-diet-detail/{dietId}/delete")
    public ResponseEntity<String> deleteDiet(@AuthenticationPrincipal Member member,
                                             @PathVariable Long dietId){
        dietService.deleteDiet(member.getId(), dietId);
        return ResponseEntity.ok("식단 삭제가 완료 되었습니다.");
    }


    @GetMapping("/public")
    public ResponseEntity<List<Map<String,DietWithImgResponse>>> getPublicUserDiet(){
        return ResponseEntity.ok(dietService.getAllUserDiets());
    }

}
