package br.com.place.exceptions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class PlaceExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(PlaceNotFoundException.class)
	public ResponseEntity<PlaceErrorResponse> placeNotFoundException(PlaceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(PlaceErrorResponse.builder().message(ex.getMessage())
				.code(HttpStatus.NOT_FOUND.value()).status(HttpStatus.NOT_FOUND).time(LocalDateTime.now()).build());
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		Map<String, List<String>> erros = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			var field = error.getField();
			var message = error.getDefaultMessage();
			if (erros.containsKey(field)) {
				erros.get(field).add(message);
			} else {
				erros.put(field, new ArrayList<>(Arrays.asList(message)));
			}
		});
		return ResponseEntity.status(status).body(PlaceValidationResponse.builder().message("Validation erro!")
				.code(status.value()).time(LocalDateTime.now()).status((HttpStatus) status).erros(erros).build());
	}
}
