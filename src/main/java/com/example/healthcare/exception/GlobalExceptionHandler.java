package com.example.healthcare.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage() +" 서버 오류가 발생하였습니다.");
    }

    @ExceptionHandler(CustomExceptions.MemberNotFoundException.class)
    public ResponseEntity<String> handleMemberNotFoundException(CustomExceptions.MemberNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage() + " 해당 유저가 없습니다.");
    }

    @ExceptionHandler(CustomExceptions.PasswordMismatchException.class)
    public ResponseEntity<String> handlePasswordMismatchException(CustomExceptions.PasswordMismatchException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage() +" 비밀번호를 잘못 입력 하셨습니다");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage() + " 사용자 ID가 null입니다.");
    }

    @ExceptionHandler(CustomExceptions.ExerciseNotFoundException.class)
    public ResponseEntity<String> handleExerciseNotFoundException(CustomExceptions.ExerciseNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage() + " 해당 운동이 없습니다.");
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage() +" 잘못된 날짜 형식입니다.");
    }

    @ExceptionHandler(CustomExceptions.DataAccessException.class)
    public ResponseEntity<String> handleDataAccessException(CustomExceptions.DataAccessException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage() + " 데이터를 가져오는데 문제가 발생 하였습나다.");
    }

    @ExceptionHandler(CustomExceptions.DietNotFoundException.class)
    public ResponseEntity<String> handleDietNotFoundException(CustomExceptions.DietNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage() + " 식단 정보를 찾을 수 없습니다.");
    }

    @ExceptionHandler(CustomExceptions.AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(CustomExceptions.AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage() +  " 수정 권한이 없습니다.");
    }

    @ExceptionHandler(CustomExceptions.FileUploadException.class)
    public ResponseEntity<String> handleFileUploadException(CustomExceptions.FileUploadException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage() +" 사진 업로드에 문제가 발생 하였습니다.");
    }

}

