package com.example.healthcare.exception;

public class CustomExceptions {

    public static class PasswordMismatchException extends RuntimeException {
        public PasswordMismatchException(String message) {
            super(message);
        }
    }

    public static class MemberNotFoundException extends RuntimeException {
        public MemberNotFoundException(String message) {
            super(message);
        }
    }

    public static class ExerciseNotFoundException extends RuntimeException {
        public ExerciseNotFoundException(String message) {super(message);}
    }
}
