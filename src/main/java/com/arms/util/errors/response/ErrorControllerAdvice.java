package com.arms.util.errors.response;


import com.arms.util.response.CommonResponse.ApiResult;
import com.arms.util.slack.SlackNotificationService;
import com.arms.util.slack.SlackProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import static com.arms.util.response.CommonResponse.error;

@ControllerAdvice
@RequiredArgsConstructor
public class ErrorControllerAdvice {

  private final SlackNotificationService slackNotificationService;


  private  <E> ResponseEntity<ApiResult<E>> newResponse(String message, ErrorCode errorCode, HttpStatus status) {
    HttpHeaders headers = getHttpHeaders();
    return new ResponseEntity<>(error(message, errorCode, status), headers, status);
  }

  private  <E> ResponseEntity<ApiResult<E>> newResponse(ErrorCode errorCode, HttpStatus status) {
    HttpHeaders headers = getHttpHeaders();
    return new ResponseEntity<>(error(errorCode, status), headers, status);
  }

  private  HttpHeaders getHttpHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    return headers;
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public <E> ResponseEntity<ApiResult<E>> handleArgumentException(IllegalArgumentException e) {
    return newResponse(e.getMessage(),ErrorCode.COMMON_INVALID_PARAMETER,HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public <E> ResponseEntity<ApiResult<E>> handleAllException(Exception e) {
    slackNotificationService.sendMessageToChannel(SlackProperty.Channel.schedule, e);
    return newResponse(e.getMessage(), ErrorCode.COMMON_SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
