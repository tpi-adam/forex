package com.tpi.forexapi.annotation;

import com.tpi.forexapi.constant.ErrorCodes;
import com.tpi.forexapi.util.DateTimeUtils;
import com.tpi.forexapi.validator.DateStringRequestValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 檢核請求參數的日期字串
 */
@Constraint(validatedBy = DateStringRequestValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateStringRequest {
    String message() default ErrorCodes.INVALID_DATE_FORMAT_MSG;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 該日期的請求格式
     */
    String dateFormat() default DateTimeUtils.DATE_FORMAT_YYYYMMDD_SLASH;

    /**
     * 是否檢核日期範圍
     */
    boolean checkRange() default false;

    /**
     * 日期範圍的起始點
     */
    int rangeStart() default 0;

    /**
     * 日期範圍的終點
     */
    int rangeEnd() default 0;
}
