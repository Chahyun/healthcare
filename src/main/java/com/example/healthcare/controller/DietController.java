package com.example.healthcare.controller;

import com.example.healthcare.controller.request.DietRequest;
import com.example.healthcare.domain.Member;
import com.example.healthcare.service.DietService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diet")
public class DietController {

    private final DietService dietService;
    @PostMapping("register")
    public ResponseEntity<String> registerDiet(@AuthenticationPrincipal Member member,
                                               @RequestPart("dietRequests")List<DietRequest> dietRequests,
                                               @RequestPart("files")List<MultipartFile> files){
        try {
            dietService.registerDiet(member.getId(), dietRequests, files);
            return ResponseEntity.ok("식단등록이 완료 되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 ID가 null입니다.");
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 날짜 형식입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생하였습니다.");
        }
    }
}
