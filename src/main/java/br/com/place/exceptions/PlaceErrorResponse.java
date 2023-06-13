package br.com.place.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PlaceErrorResponse {
	private String message;
	private int code;
	private HttpStatus status;
	private LocalDateTime time;
}
