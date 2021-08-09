package br.com.jrafael.infrastructure.handler;

import br.com.jrafael.infrastructure.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ControllerAdvice
public class RestExceptionHandler {

    private static final Logger LOGGER = Logger.getLogger(RestExceptionHandler.class.getName());

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                   WebRequest request) {
        String error = ex.getName() + " must be of the type " + ex.getRequiredType().getName();

        return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ResponseEntity<String> handleBusinessErrors(HttpServletRequest req, BusinessException be) {

        ResponseEntity<String> responseEntity = new ResponseEntity<>(be.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);

        return responseEntity;
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    @ResponseBody
//    public ResponseEntity<ValidationError> handleValidationErrors(HttpServletRequest req,
//                                                                  MethodArgumentNotValidException manvex) {
//
//        BindingResult result = manvex.getBindingResult();
//        List<FieldError> fieldErrors = result.getFieldErrors();
//        ValidationError validationError = new ValidationError();
//
//        for (FieldError fieldError : fieldErrors) {
//            validationError.addFieldError(fieldError.getField(), fieldError.getCode());
//        }
//
//        return new ResponseEntity<>(validationError, HttpStatus.BAD_REQUEST);
//
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<CustomMessageError> handleValidationErrors(HttpServletRequest req,
                                                                  MethodArgumentNotValidException manvex) {

        BindingResult result = manvex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        ValidationError validationError = new ValidationError();

        String message = "";
        for (FieldError fieldError : fieldErrors) {
            String error = fieldError.getField() +": "+ fieldError.getCode();
            message = message.length() == 0 ? error : message + " and " + error;
            validationError.addFieldError(fieldError.getField(), fieldError.getCode());
        }

        return new ResponseEntity<>(new CustomMessageError(HttpStatus.BAD_REQUEST.value(), message) , HttpStatus.BAD_REQUEST);
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class InvalidField implements Serializable {

        private String field;

        private String message;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class ValidationError implements Serializable {

        private List<InvalidField> fieldErrors = new ArrayList<>();

        public void addFieldError(String path, String message) {
            InvalidField error = new InvalidField(path, message);
            fieldErrors.add(error);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class CustomMessageError implements Serializable {
        private int status_code;
        private String message;
    }
}
