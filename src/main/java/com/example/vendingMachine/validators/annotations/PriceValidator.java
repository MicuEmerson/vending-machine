package com.example.vendingMachine.validators.annotations;

import com.example.vendingMachine.validators.PriceConstrains;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({FIELD, ANNOTATION_TYPE, TYPE_USE, METHOD, PARAMETER})
@Constraint(validatedBy = PriceConstrains.class)
public @interface PriceValidator {
	String message() default "Price must be multiple of 5";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
