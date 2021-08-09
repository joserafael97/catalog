package br.com.jrafael.infrastructure.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotEqualFieldsValidator.class)
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEqualFields {
    String message() default "Fields are not equal";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String originalField();
    String fieldToCompare();
}
