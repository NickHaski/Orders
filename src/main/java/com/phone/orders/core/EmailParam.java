package com.phone.orders.core;

import org.apache.commons.lang3.StringUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailParam.EmailParamValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailParam {

    String EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    String message() default "Email is invalid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class EmailParamValidator implements ConstraintValidator<EmailParam, String> {

        @Override
        public void initialize(final EmailParam paramA) {
            //empty
        }

        @Override
        public boolean isValid(final String email, final ConstraintValidatorContext ctx) {
            return StringUtils.isNotEmpty(email) && email.matches(EMAIL);
        }
    }
}
