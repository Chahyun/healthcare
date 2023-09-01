package com.example.healthcare.service;

import com.example.healthcare.controller.request.DietRequest;
import com.example.healthcare.domain.Diet;
import com.example.healthcare.domain.DietImageUrl;
import com.example.healthcare.exception.CustomExceptions;
import com.example.healthcare.repository.DietImageUrlRepository;
import com.example.healthcare.repository.DietRepository;
import com.example.healthcare.util.DateTimeParser;
import com.example.healthcare.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DietService {

    private final DietRepository dietRepository;
    private final DietImageUrlRepository dietImageUrlRepository;
    private final S3Uploader s3Uploader;

    /**
     * 사용자의 식단을 등록합니다.
     *
     * @param userId       사용자 ID
     * @param dietRequests 식단 요청 목록
     * @param files        식단 관련 이미지 파일 목록
     * @throws IllegalArgumentException 사용자 ID가 null인 경우 예외 발생
     * @throws CustomExceptions.FileUploadException 파일 업로드 실패 시 예외 발생
     * @throws DateTimeParseException DateTimeParser.dateParser의 잘못된 데이터 형식이 들어올 시 예외 발생
     */
    @Transactional
    public void registerDiet(Long userId, List<DietRequest> dietRequests, List<MultipartFile> files) {
        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID가 null입니다.");
        }
        Long latestDietId = 1L;

        Optional<Diet> latestDietOptional = dietRepository.findFirstByOrderByIdDesc();
        if (latestDietOptional.isPresent()) {
            latestDietId = latestDietOptional.get().getId() + 1;
        }
        for (MultipartFile file : files) {
            try {
                String fileUrl = s3Uploader.uploadAndGenerateUrl(file, "diet-images/" + file.getOriginalFilename());
                dietImageUrlRepository.save(DietImageUrl.createDietImageUrl(latestDietId, fileUrl));
            } catch (IOException e) {
                log.error("파일 업로드 실패: " + e.getMessage());
                throw new CustomExceptions.FileUploadException("파일 업로드 실패", e);
            }
        }
        for(DietRequest dietRequest : dietRequests){
            dietRepository.save(
                    Diet.createDiet(userId, dietRequest, DateTimeParser.dateParser(dietRequest.getDietDate()))
            );
        }
    }
}
