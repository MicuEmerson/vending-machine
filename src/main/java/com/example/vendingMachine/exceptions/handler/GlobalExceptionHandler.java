package com.example.vendingMachine.exceptions.handler;

import com.example.vendingMachine.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ActionNotPermitted.class)
	public final ResponseEntity<ErrorDetails> handleActionNotPermitted(ActionNotPermitted ex, WebRequest request) {
		return createResponse(ex, request, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(InsufficientDeposit.class)
	public final ResponseEntity<ErrorDetails> handleInsufficientDepositException(InsufficientDeposit ex, WebRequest request) {
		return createResponse(ex, request, HttpStatus.PAYMENT_REQUIRED);
	}

	@ExceptionHandler(OutOfStock.class)
	public final ResponseEntity<ErrorDetails> handleOutOfStockException(OutOfStock ex, WebRequest request) {
		return createResponse(ex, request, HttpStatus.GONE);
	}

	@ExceptionHandler(BadRequest.class)
	public final ResponseEntity<ErrorDetails> handleBadRequestException(BadRequest ex, WebRequest request) {
		return createResponse(ex, request, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public final ResponseEntity<ErrorDetails> handleEntityNotFoundExceptionException(EntityNotFoundException ex, WebRequest request) {
		return createResponse(ex, request, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorDetails> handleInternalServerError(Exception ex, WebRequest request) {
		return createResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ResponseEntity<ErrorDetails> createResponse(Exception ex, WebRequest request, HttpStatus status){
		ErrorDetails errorDetails = createBody(ex, request);

		logger.error(errorDetails.getPath() + " responded with stack strace " + Arrays.toString(ex.getStackTrace()));
		return new ResponseEntity(errorDetails, status);
	}

	private ErrorDetails createBody(Throwable t, WebRequest request){
		return ErrorDetails.builder()
				.message(t.getMessage())
				.path(request.getDescription(false).substring(4))
				.build();
	}
}