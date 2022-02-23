package com.example.vendingMachine.validators;

import com.example.vendingMachine.validators.annotations.PriceValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PriceConstrains implements ConstraintValidator<PriceValidator, Long> {
	@Override
	public boolean isValid(Long aLong, ConstraintValidatorContext constraintValidatorContext) {
		return aLong % 5 == 0;
	}
}
