package com.example.healthcare.service;

import com.example.healthcare.controller.request.ExerciseRequest;
import com.example.healthcare.controller.response.ExerciseResponse;
import com.example.healthcare.domain.Exercise;
import com.example.healthcare.domain.Member;
import com.example.healthcare.domain.enumType.ExerciseRole;
import com.example.healthcare.domain.enumType.MemberDisclosureStatusRole;
import com.example.healthcare.exception.CustomExceptions;
import com.example.healthcare.repository.ExerciseRepository;
import com.example.healthcare.repository.MemberRepository;
import com.example.healthcare.util.DateTimeParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private RedisTemplate<String, List<ExerciseResponse>> redisTemplate;

    /**
     * 나의 운동 계획을 등록합니다.
     *
     * @param userId 사용자 memberID
     * @param requests 운동 계획 등록 요청 객체
     */
    public void registerExercise(Long userId, List<ExerciseRequest> requests) {
        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID가 null입니다.");
        }
        for(ExerciseRequest request : requests){
            exerciseRepository.save(Exercise.createExercise(userId, request,
                    DateTimeParser.dateParser(request.getExerciseDate())));
        }
        // 운동 계획을 데이터베이스에 저장합니다.
    }

    /**
     * 주어진 날짜에 해당하는 하루 동안의 사용자의 운동 데이터를 가져옵니다.
     *
     * @param userId 사용자 memberID
     * @param selectDate 선택한 날짜
     * @return 운동 데이터의 ExerciseResponse 리스트
     */
    @Cacheable(value = "exerciseCache", key = "'myExerciseForDate_' + #userId + '_' + #selectDate")
    public List<ExerciseResponse> myExerciseForDate(Long userId, LocalDate selectDate) {
        // 선택한 날짜의 시작과 종료 일시를 계산합니다.
        LocalDateTime startDate = selectDate.atStartOfDay();
        LocalDateTime endDate = selectDate.atTime(23, 59, 59);

        // 계산한 일시 범위로 운동 데이터 조회를 수행합니다.
        return searchExercise(userId, startDate, endDate);
    }

    /**
     * 주어진 날짜에 해당하는 주 동안의 사용자의 운동 데이터를 가져옵니다.
     *
     * @param userId 사용자 memberID
     * @param selectDate 선택한 날짜
     * @return 운동 데이터의 ExerciseResponse 리스트
     */
    @Cacheable(value = "exerciseCache", key = "'myExerciseForWeek_' + #userId + '_' + #selectDate")
    public List<ExerciseResponse> myExerciseForWeek(Long userId, LocalDate selectDate) {
        // 선택한 날짜의 주의 시작과 종료 일시를 계산합니다.
        LocalDate startOfWeek = selectDate.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = selectDate.with(DayOfWeek.SUNDAY);
        LocalDateTime startDate = startOfWeek.atStartOfDay();
        LocalDateTime endDate = endOfWeek.atTime(23, 59, 59);

        // 계산한 일시 범위로 운동 데이터 조회를 수행합니다.
        return searchExercise(userId, startDate, endDate);
    }

    /**
     * 주어진 날짜에 해당하는 달 동안의 사용자의 운동 데이터를 가져옵니다.
     *
     * @param userId 사용자 memberID
     * @param selectDate 선택한 날짜
     * @return 운동 데이터의 ExerciseResponse 리스트
     */
    @Cacheable(value = "exerciseCache", key = "'myExerciseForMonth_' + #userId + '_' + #selectDate")
    public List<ExerciseResponse> myExerciseForMonth(Long userId, LocalDate selectDate) {
        // 선택한 날짜의 월의 시작과 종료 일시를 계산합니다.
        LocalDate startOfMonth = selectDate.withDayOfMonth(1);
        LocalDate endOfMonth = selectDate.withDayOfMonth(selectDate.lengthOfMonth());
        LocalDateTime startDate = startOfMonth.atStartOfDay();
        LocalDateTime endDate = endOfMonth.atTime(23, 59, 59);

        // 계산한 일시 범위로 운동 데이터 조회를 수행합니다.
        return searchExercise(userId, startDate, endDate);
    }


    /**
     * 주어진 사용자와 기간에 해당하는 운동 데이터를 가져와서 운동 기록을 ExerciseResponse 객체들로 변환하여 반환합니다.
     *
     * @param userId 사용자 memberID
     * @param startDate 조회 시작일시
     * @param endDate 조회 종료일시
     * @return ExerciseResponse 객체들의 리스트
     * @throws CustomExceptions.ExerciseNotFoundException 기간 내에 운동 기록이 없을 경우 예외 발생
     */
    private List<ExerciseResponse> searchExercise(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        // 사용자 정보를 조회하여 존재 여부를 확인합니다.
        memberService.findMemberForId(userId);

        // 주어진 기간에 해당하는 운동 데이터를 가져옴
        List<Exercise> userExerciseForDate =
                exerciseRepository.findByUserIdAndExerciseDateBetween(userId, startDate, endDate);

        // 운동 데이터가 비어있는지 검증하고 예외 처리
        validateExerciseListNotEmpty(userExerciseForDate);

        // 운동 데이터를 ExerciseResponse 객체로 변환하여 리스트로 반환
        return convertToExerciseResponses(userExerciseForDate);
    }

    public void deleteExercise(Long id, Long exerciseId) {
        Exercise exercise = getExerciseById(exerciseId);
        checkExerciseOwnership(exercise, id);
        exerciseRepository.deleteById(exerciseId);
    }


    public void updateExercise(Long id, Long exerciseId, ExerciseRequest request) {
        Exercise exercise = getExerciseById(exerciseId);
        checkExerciseOwnership(exercise, id);
        exerciseRepository.save(Exercise.updateExercise(exercise,request,
                DateTimeParser.dateParser(request.getExerciseDate())));
    }


    public void successExercise(Long id, Long exerciseId) {
        Exercise exercise = getExerciseById(exerciseId);
        checkExerciseOwnership(exercise, id);
        exercise.setStatus(ExerciseRole.COMPLETE);
        exerciseRepository.save(exercise);
    }

    /**
     * 운동 데이터 리스트가 비어있는지 검증하고, 비어있다면 예외를 발생시킵니다.
     *
     * @param exerciseList 운동 데이터 리스트
     * @throws CustomExceptions.ExerciseNotFoundException 운동 데이터 리스트가 비어있을 경우 예외 발생
     */
    private void validateExerciseListNotEmpty(List<Exercise> exerciseList) {
        if (exerciseList.isEmpty()) {
            throw new CustomExceptions.ExerciseNotFoundException("해당 기간의 운동 기록이 없습니다.");
        }
    }

    /**
     * 운동 데이터 리스트를 ExerciseResponse 객체들로 변환하여 반환합니다.
     *
     * @param exerciseList 운동 데이터 리스트
     * @return ExerciseResponse 객체들의 리스트
     */
    private List<ExerciseResponse> convertToExerciseResponses(List<Exercise> exerciseList) {
        List<ExerciseResponse> exerciseResponses = new ArrayList<>();
        for (Exercise exercise : exerciseList) {
            exerciseResponses.add(ExerciseResponse.createFromExercise(exercise));
        }
        return exerciseResponses;
    }



    /**
     * 운동 ID에 해당하는 운동 데이터를 조회하는 메서드입니다.
     *
     * @param exerciseId 운동 ID
     * @return 운동 데이터
     * @throws CustomExceptions.ExerciseNotFoundException 운동 데이터가 존재하지 않을 경우
     */
    private Exercise getExerciseById(Long exerciseId) {
        return exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new CustomExceptions.ExerciseNotFoundException("해당 운동이 존재하지 않습니다."));
    }

    /**
     * 운동 데이터의 소유자 권한을 확인하는 메서드입니다.
     *
     * @param exercise 운동 데이터
     * @param userId   회원 ID
     * @throws CustomExceptions.AccessDeniedException 권한이 없을 경우
     */
    private void checkExerciseOwnership(Exercise exercise, Long userId) {
        if (!exercise.getUserId().equals(userId)) {
            throw new CustomExceptions.AccessDeniedException("해당 운동의 변경 권한이 존재하지 않습니다.");
        }
    }


    public Map<String, List<ExerciseResponse>> getAllUserExercises(LocalDate selectDate) {
        List<Member> publicMembers = memberRepository.findAllByDisclosureStatus(MemberDisclosureStatusRole.PUBLIC);

        if(publicMembers.isEmpty()){
            throw new CustomExceptions.MemberNotFoundException("공개한 유저가 없습니다.");
        }

        Map<String, List<ExerciseResponse>> userExercisesMap = new HashMap<>();
        LocalDateTime startDate = selectDate.atStartOfDay();
        LocalDateTime endDate = selectDate.atTime(23, 59, 59);

        for(Member member : publicMembers){
            log.info(member.getNickname());
            try {
                userExercisesMap.put(member.getNickname(), searchExercise(member.getId(), startDate, endDate));
            } catch (CustomExceptions.ExerciseNotFoundException e) {
                continue;
            }

            log.info(searchExercise(member.getId(), startDate, endDate).toString());
        }
        return userExercisesMap;
    }
}
