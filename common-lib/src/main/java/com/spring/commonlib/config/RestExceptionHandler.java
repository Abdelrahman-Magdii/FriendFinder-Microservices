package com.spring.commonlib.config;

import com.spring.commonlib.config.translate.BundleTranslator;
import com.spring.commonlib.exceptions.BusinessException;
import com.spring.commonlib.exceptions.FieldException;
import com.spring.commonlib.exceptions.SysException;
import com.spring.commonlib.model.exception.ErrorExceptionApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final BundleTranslator bundleTranslator;

    @Autowired
    public RestExceptionHandler(BundleTranslator bundleTranslator) {
        this.bundleTranslator = bundleTranslator;
    }

    @ExceptionHandler(FieldException.class)
    public final ResponseEntity<Object> handleFieldException(FieldException exception) {
        return buildResponseEntity(new ErrorExceptionApi(HttpStatus.BAD_REQUEST,
                        bundleTranslator.getMessages(exception.getMessage())));
    }

    @ExceptionHandler(BusinessException.class)
    public final ResponseEntity<Object> handleBusinessException(
                    BusinessException exception) {
        return buildResponseEntity(new ErrorExceptionApi(HttpStatus.FOUND,
                        bundleTranslator.getMessages(exception.getMessage())));
    }

    @ExceptionHandler(SysException.class)
    public final ResponseEntity<Object> handleSysException(SysException exception) {
        return buildResponseEntity(
                        new ErrorExceptionApi(HttpStatus.FOUND, exception.getMessage()));
    }

    private ResponseEntity<Object> buildResponseEntity(
                    ErrorExceptionApi errorExceptionApi) {
        return new ResponseEntity<Object>(errorExceptionApi,
                        errorExceptionApi.getStatus());
    }
}
