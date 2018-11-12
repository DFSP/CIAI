package pt.unl.fct.ciai.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import pt.unl.fct.ciai.exception.BadRequestException;

public class BadRequestAdvice {

	@ResponseBody
	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String badRequestHandler(BadRequestException ex) {
		return ex.getMessage();
	}
	
}
