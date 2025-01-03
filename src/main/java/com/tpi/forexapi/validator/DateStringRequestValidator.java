package com.tpi.forexapi.validator;

import com.tpi.forexapi.annotation.DateStringRequest;
import com.tpi.forexapi.constant.ErrorCodes;
import com.tpi.forexapi.exception.ValidationErrorException;
import com.tpi.forexapi.util.DateTimeUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * 請求參數中的日期字串檢核邏輯
 */
@Slf4j
@Component
@RegisterReflectionForBinding
public class DateStringRequestValidator implements ConstraintValidator<DateStringRequest, String> {
    private String dateFormat;
    private int rangeStart;
    private int rangeEnd;
    private boolean checkRange;

    @Override
    public void initialize(DateStringRequest constraintAnnotation) {
        dateFormat = constraintAnnotation.dateFormat();
        rangeStart = constraintAnnotation.rangeStart();
        rangeEnd = constraintAnnotation.rangeEnd();
        checkRange = constraintAnnotation.checkRange();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 把文字解析成日期物件 若失敗會拋錯
        LocalDate localDate = DateTimeUtils.stringToLocalDate(value, dateFormat);

        // 是否檢核日期範圍
        if (!checkRange) {
            return true;
        }

        // 若日期區間不符規則， response 需回 error code E001
        LocalDate today = LocalDate.now();
        LocalDate rangeStartLocalDate = today.plusDays(rangeStart);
        LocalDate rangeEndLocalDate = today.plusDays(rangeEnd);
        if (localDate.isBefore(rangeStartLocalDate) || localDate.isAfter(rangeEndLocalDate)) {
            throw new ValidationErrorException(ErrorCodes.INVALID_DATE_FORMAT_CODE, ErrorCodes.INVALID_DATE_FORMAT_MSG);
        }
        return true;
    }
}
