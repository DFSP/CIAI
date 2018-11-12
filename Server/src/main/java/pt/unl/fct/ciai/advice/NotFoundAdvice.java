package pt.unl.fct.ciai.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import pt.unl.fct.ciai.exception.NotFoundException;

@ControllerAdvice
public class NotFoundAdvice {

	@ResponseBody
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String notFoundHandler(NotFoundException ex) {
		return ex.getMessage();
	}
	
}
