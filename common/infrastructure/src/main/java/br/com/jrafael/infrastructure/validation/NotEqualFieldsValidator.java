package br.com.jrafael.infrastructure.validation;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotEqualFieldsValidator implements ConstraintValidator<NotEqualFields, Object> {

    private String otherField;
    private String field;

    @Override
    public void initialize(NotEqualFields constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.otherField = constraintAnnotation.fieldToCompare();
        this.field = constraintAnnotation.originalField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object fieldObj = new BeanWrapperImpl(value).getPropertyValue(field);
        Object otherFieldObj = new BeanWrapperImpl(value).getPropertyValue(otherField);

        return fieldObj != null && otherFieldObj != null && otherFieldObj.equals(fieldObj);
    }

}
