package com.tpi.forexapi.handler;

import com.tpi.forexapi.dto.ExceptionDTO;
import com.tpi.forexapi.dto.ValidationErrorResponseDTO;
import com.tpi.forexapi.exception.ValidationErrorException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 全域例外處理handler
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 所有沒有被特別捕捉的例外一律透過此處處理
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value
            = {Exception.class})
    public ResponseEntity<ExceptionDTO> handleConflict(
            Exception ex, WebRequest request) {
        return ResponseEntity.internalServerError().body(new ExceptionDTO(ex.getMessage()));
    }

    /**
     * 自定義檢核錯誤
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value
            = {ValidationException.class})
    public ResponseEntity<ValidationErrorResponseDTO> handleValidationException(
            ValidationException ex, WebRequest request) {
        ValidationErrorResponseDTO.Error error = ValidationErrorResponseDTO.Error.builder().build();
        // 根據拋錯類型決定回應方式
        if (ex.getCause() instanceof ValidationErrorException validationErrorException) {
            error.setCode(validationErrorException.getErrorCode());
            error.setMessage(validationErrorException.getErrorMessage());
        } else if (ex.getCause() instanceof RuntimeException runtimeException) {
            error.setMessage(runtimeException.getMessage());
        } else {
            error.setMessage(ex.getMessage());
        }
        return ResponseEntity.badRequest().body(ValidationErrorResponseDTO.builder()
                                                                          .error(error)
                                                                          .build());
    }

    /**
     * 系統內建檢核錯誤
     *
     * @param ex      the exception to handle
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        return ResponseEntity.badRequest().body(ValidationErrorResponseDTO.builder()
                                                                          .error(ValidationErrorResponseDTO.Error.builder()
                                                                                                                 .message(
                                                                                                                         Arrays.stream(
                                                                                                                                       ex.getDetailMessageArguments())
                                                                                                                               .map(Object::toString)
                                                                                                                               .collect(
                                                                                                                                       Collectors.joining(
                                                                                                                                               ",")))
                                                                                                                 .build())
                                                                          .build());
    }
}
