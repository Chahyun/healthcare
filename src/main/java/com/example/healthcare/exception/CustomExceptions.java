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

    public static class AccessDeniedException extends RuntimeException {
        public AccessDeniedException(String message) {super(message);}
    }

    public static class FileUploadException extends RuntimeException {

        public FileUploadException(String message) {
            super(message);
        }
        public FileUploadException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class DietNotFoundException extends RuntimeException {
        public DietNotFoundException(String message) {super(message);}
    }

    public static class DataAccessException extends RuntimeException {
        public DataAccessException(String message){super(message);} {
        }
    }
}
