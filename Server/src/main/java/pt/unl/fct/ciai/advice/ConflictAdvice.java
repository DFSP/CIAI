package pt.unl.fct.ciai.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import pt.unl.fct.ciai.exception.ConflictException;

public class ConflictAdvice {

	@ResponseBody
	@ExceptionHandler(ConflictException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	String conflictHandler(ConflictException ex) {
		return ex.getMessage();
	}
	
}
