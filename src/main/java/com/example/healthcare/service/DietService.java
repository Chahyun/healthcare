package com.example.healthcare.service;

import com.example.healthcare.controller.request.diet.DietRequest;
import com.example.healthcare.controller.response.diet.DietImgUrlResponse;
import com.example.healthcare.controller.response.diet.DietInfoResponse;
import com.example.healthcare.controller.response.diet.DietWithImgResponse;
import com.example.healthcare.domain.diet.Diet;
import com.example.healthcare.domain.diet.DietImageUrl;
import com.example.healthcare.domain.diet.DietInfo;
import com.example.healthcare.domain.enumType.diet.DietStatusRole;
import com.example.healthcare.domain.enumType.member.MemberDisclosureStatusRole;
import com.example.healthcare.domain.memeber.Member;
import com.example.healthcare.exception.CustomExceptions;
import com.example.healthcare.repository.DietImageUrlRepository;
import com.example.healthcare.repository.DietInfoRepository;
import com.example.healthcare.repository.DietRepository;
import com.example.healthcare.repository.MemberRepository;
import com.example.healthcare.util.FileRandomNaming;
import com.example.healthcare.util.S3Deleter;
import com.example.healthcare.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DietService {

    private final DietRepository dietRepository;
    private final DietInfoRepository dietInfoRepository;
    private final DietImageUrlRepository dietImageUrlRepository;
    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;
    private final S3Deleter s3Deleter;

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
    public void registerDiet(Long userId, String dietDate, List<DietRequest> dietRequests, List<MultipartFile> files) {
        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID가 null입니다.");
        }

        Long latestDietId = 0L;
        Optional<Diet> latestDietOptional = dietRepository.findFirstByOrderByIdDesc();
        if (latestDietOptional.isPresent()) {
            latestDietId = latestDietOptional.get().getId() + 1;
        }
        LocalDate date = LocalDate.parse(dietDate, DateTimeFormatter.ISO_DATE);
        dietRepository.save(Diet.createDiet(latestDietId, userId, date));
        if (files!=null) { // 사진 파일을 보냈을 때만 처리
            for (MultipartFile file : files) {
                try {
                    String fileUrl = s3Uploader.uploadAndGenerateUrl(
                            file, "diet-images/" + FileRandomNaming.fileRandomNaming(file));
                    dietImageUrlRepository.save(DietImageUrl.createDietImageUrl(latestDietId, fileUrl));
                } catch (IOException e) {
                    log.error("파일 업로드 실패: " + e.getMessage());
                    throw new CustomExceptions.FileUploadException("파일 업로드 실패", e);
                }
            }
        }
        for(DietRequest dietRequest : dietRequests){
            dietInfoRepository.save(
                    DietInfo.createDietInfo(latestDietId, dietRequest));
        }
    }

    /**
     * 사용자의 특정 날짜의 식단을 조회합니다.
     *
     * @param userId     사용자 ID
     * @param selectDate 조회하고자 하는 날짜(ISO 날짜 형식)
     * @return 해당 날짜의 식단 및 관련 이미지 응답
     * @throws IllegalArgumentException 사용자 ID가 null인 경우 예외 발생
     * @throws DateTimeParseException selectDate의 잘못된 데이터 형식이 들어올 시 예외 발생
     */
    public DietWithImgResponse myDietForDate(Long userId, String selectDate) {
        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID가 null입니다.");
        }
        LocalDate date = LocalDate.parse(selectDate, DateTimeFormatter.ISO_DATE);

        return searchDiet(userId,date, date);
    }


    /**
     * 사용자의 특정 주의 식단을 조회합니다.
     *
     * @param userId     사용자 ID
     * @param selectDate 조회하고자 하는 날짜(ISO 날짜 형식)
     * @return 해당 주의 식단 및 관련 이미지 응답
     * @throws IllegalArgumentException 사용자 ID가 null인 경우 예외 발생
     * @throws DateTimeParseException selectDate의 잘못된 데이터 형식이 들어올 시 예외 발생
     */
    public DietWithImgResponse myDietForWeek(Long userId, String selectDate) {
        // 선택한 날짜의 주의 시작과 종료 일시를 계산합니다.
        LocalDate date = LocalDate.parse(selectDate, DateTimeFormatter.ISO_DATE);
        LocalDate startDate = date.with(DayOfWeek.MONDAY);
        LocalDate endDate = date.with(DayOfWeek.SUNDAY);

        return searchDiet(userId, startDate, endDate);
    }

    /**
     * 사용자의 특정 월의 식단을 조회합니다.
     *
     * @param userId     사용자 ID
     * @param selectDate 조회하고자 하는 날짜(ISO 날짜 형식)
     * @return 해당 월의 식단 및 관련 이미지 응답
     * @throws IllegalArgumentException 사용자 ID가 null인 경우 예외 발생
     * @throws DateTimeParseException selectDate의 잘못된 데이터 형식이 들어올 시 예외 발생
     */
    public DietWithImgResponse myDietForMonth(Long userId, String selectDate) {
        try {
            StringBuilder sb = new StringBuilder(selectDate);
            sb.append("-01");
            LocalDate date = LocalDate.parse(sb.toString(), DateTimeFormatter.ISO_DATE);
            LocalDate startDate = date.withDayOfMonth(1);
            LocalDate endDate = date.withDayOfMonth(date.lengthOfMonth());
            return searchDiet(userId, startDate, endDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("날짜 형식이 잘못되었습니다.");
        } catch (Exception e) {
            throw new CustomExceptions.DataAccessException("데이터 조회 중 오류가 발생했습니다.");
        }
    }

    /**
     * 식단을 검색하여 관련 데이터를 반환합니다.
     *
     * @param userId     사용자 ID
     * @param startDate  조회 시작 날짜
     * @param endDate    조회 종료 날짜
     * @return 식단과 관련 데이터 응답
     */
    private DietWithImgResponse searchDiet(Long userId, LocalDate startDate, LocalDate endDate) {
        try {
            List<Diet> diets = dietRepository.findByUserIdAndDietDateBetween(userId, startDate, endDate);
            return getDietData(diets);
        } catch (CustomExceptions.DietNotFoundException e) {
            // 조회된 데이터가 없을 경우 처리
            throw new CustomExceptions.DietNotFoundException("지정된 식단이 없습니다.");
        } catch (Exception e) {
            // 그 외 예외 상황에 대한 처리
            throw new CustomExceptions.DataAccessException("데이터 처리 중 오류가 발생했습니다.");
        }
    }

    public void dietSuccess(Long userId, Long dietId , Long dietInfoId) {
        Diet diet =  dietRepository.findById(dietId).orElseThrow(
                () -> new CustomExceptions.DietNotFoundException("식단 정보가 존재하지 않습니다."));

        DietInfo dietInfo = dietInfoRepository.findById(dietInfoId).orElseThrow(
                () -> new CustomExceptions.DietNotFoundException("식단 정보가 존재하지 않습니다."));

        if(dietInfo.getDietId() == dietId && diet.getUserId() == userId) {
            dietInfo.setDietStatusRole(DietStatusRole.COMPLETE);
            dietInfoRepository.save(dietInfo);
        } else {
            throw new CustomExceptions.AccessDeniedException("이 식단의 권한이 없습니다.");
        }
    }


    public DietWithImgResponse getAllDiets(Long userId) {
        List<Diet> diets = dietRepository.findByUserId(userId);
        return getDietData(diets);
    }

    public DietWithImgResponse getDetailDiets(Long userId, Long dietId) {
        List<Diet> diets = dietRepository.findByUserIdAndId(userId, dietId);
        return getDietData(diets);
    }

    private DietWithImgResponse getDietData (List<Diet> diets){
        if (diets == null || diets.isEmpty()) {
            throw new CustomExceptions.DietNotFoundException("조회된 데이터가 없습니다.");
        }

        List<DietImageUrl> dietImageUrls = diets.stream()
                .map(diet -> dietImageUrlRepository.findAllByDietId(diet.getId()))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        List<DietInfo> dietInfos = diets.stream()
                .map(diet -> dietInfoRepository.findAllByDietId(diet.getId()))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        List<DietInfoResponse> dietInfoResponses = dietInfos.stream()
                .map(DietInfoResponse::createDietResponse)
                .collect(Collectors.toList());

        List<DietImgUrlResponse> dietImgUrlResponses = dietImageUrls.stream()
                .map(DietImgUrlResponse::createDietImgUrlResponse)
                .collect(Collectors.toList());

        return new DietWithImgResponse(dietInfoResponses, dietImgUrlResponses);
    }



    @Transactional
    public void updateDiet(
            Long userId, Long dietId,
            String dietDate, List<DietRequest> dietRequests,
            List<MultipartFile> files) {
        Diet diet =  dietRepository.findById(dietId).orElseThrow(
                () -> new CustomExceptions.DietNotFoundException("식단 정보가 존재하지 않습니다."));

        if(diet.getUserId() == userId){
            dietInfoRepository.deleteAllByDietId(diet.getId());
            for(DietRequest dietRequest : dietRequests){
                dietInfoRepository.save(DietInfo.createDietInfo(diet.getId(), dietRequest));
            }

            // S3에서 기존 이미지 삭제
            List<DietImageUrl> oldImageUrls = dietImageUrlRepository.findAllByDietId(diet.getId());
            dietImageUrlRepository.deleteAllByDietId(diet.getId());
            for (DietImageUrl imageUrl : oldImageUrls) {
                String objectKey = getImageObjectKey(imageUrl.getImgUrl());
                s3Deleter.deleteObject(objectKey);
            }
            if (files!=null) { // 사진 파일을 보냈을 때만 처리
                for (MultipartFile file : files) {
                    try {
                        String fileUrl = s3Uploader.uploadAndGenerateUrl(
                                file, "diet-images/" + FileRandomNaming.fileRandomNaming(file));
                        dietImageUrlRepository.save(DietImageUrl.createDietImageUrl(diet.getId(), fileUrl));
                    } catch (IOException e) {
                        log.error("파일 업로드 실패: " + e.getMessage());
                        throw new CustomExceptions.FileUploadException("파일 업로드 실패", e);
                    }
                }
            }
            diet.setDietDate(LocalDate.parse(dietDate, DateTimeFormatter.ISO_DATE));
        } else {
            throw new CustomExceptions.AccessDeniedException("이 식단의 권한이 없습니다.");
        }
    }

    @Transactional
    public void deleteDiet(Long userId, Long dietId) {
        try {
            Diet diet =  dietRepository.findById(dietId).orElseThrow(
                    () -> new CustomExceptions.DietNotFoundException("식단 정보가 존재하지 않습니다."));

            if(diet.getUserId() == userId){
                List<DietImageUrl> imageUrls = dietImageUrlRepository.findAllByDietId(diet.getId());
                for (DietImageUrl imageUrl : imageUrls) {
                    String objectKey = getImageObjectKey(imageUrl.getImgUrl());
                    log.info(objectKey);
                    s3Deleter.deleteObject(objectKey);
                }
                dietInfoRepository.deleteAllByDietId(diet.getId());
                dietImageUrlRepository.deleteAllByDietId(diet.getId());
                dietRepository.deleteById(dietId);
            }
        } catch (Exception e) {
            log.error("식단 삭제 중 오류 발생: " + e.getMessage());
            throw e;
        }
    }


    private String getImageObjectKey(String imgUrl){
        StringTokenizer st = new StringTokenizer(imgUrl,"/");
        StringBuilder objectKey = new StringBuilder("diet-images/");
        String str = "";
        while(st.hasMoreTokens()){
            str = st.nextToken();
        }
        objectKey.append(str);
        return objectKey.toString();
    }

    public List<Map<String, DietWithImgResponse>> getAllUserDiets() {
        List<Member> members = memberRepository.findAllByDisclosureStatus(MemberDisclosureStatusRole.PUBLIC);
        List<Map<String, DietWithImgResponse>> getAllList = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();

        Map<String, DietWithImgResponse> map = new HashMap<>();
        for (Member member : members) {
            List<Diet> diets = dietRepository.findByUserIdAndDietDateBetween(member.getId(), currentDate, currentDate);

            for (Diet diet : diets) {
                List<DietInfoResponse> dietInfoResponses = new ArrayList<>();
                List<DietInfo> dietInfos = dietInfoRepository.findAllByDietId(diet.getId());

                for (DietInfo dietInfo : dietInfos) {
                    dietInfoResponses.add(DietInfoResponse.createDietResponse(dietInfo));
                }

                List<DietImgUrlResponse> dietImgUrlResponses = new ArrayList<>();
                List<DietImageUrl> dietImageUrls = dietImageUrlRepository.findAllByDietId(diet.getId());

                for (DietImageUrl dietImageUrl : dietImageUrls) {
                    dietImgUrlResponses.add(DietImgUrlResponse.createDietImgUrlResponse(dietImageUrl));
                }

                String nickname = member.getNickname();

                // 해당 멤버에 대한 맵이 이미 있으면 가져와서 업데이트, 없으면 새로 생성
                DietWithImgResponse dietResponse = map.getOrDefault(nickname, new DietWithImgResponse(
                        dietInfoResponses, dietImgUrlResponses
                ));
                dietResponse.getDietResponses().addAll(dietInfoResponses);
                dietResponse.getDietImgUrlResponses().addAll(dietImgUrlResponses);

                map.put(nickname, dietResponse);
            }
            getAllList.add(map);
            map = new HashMap<>();
        }
        return getAllList;
    }

}
