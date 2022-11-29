package com.hospitalreview.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "UserName 중복"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "UserName not found");

    private HttpStatus status;
    private String message;
}
