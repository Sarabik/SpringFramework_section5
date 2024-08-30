package com.springframework.section5.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomErrorController {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<Map<String, String>> handleBindErrors(MethodArgumentNotValidException e) {
		BindingResult bindingResult = e.getBindingResult();
		Map<String, String> errorMap = new HashMap<>();
		bindingResult.getFieldErrors().forEach(error ->
					errorMap.put(error.getField(), error.getDefaultMessage()));
		return ResponseEntity.badRequest().body(errorMap);
	}

	@ExceptionHandler
	ResponseEntity<Map<String, String>> handleJPAViolations(TransactionSystemException e) {
		Map<String, String> errorMap = new HashMap<>();
		if (e.getCause().getCause() instanceof final ConstraintViolationException exception) {
			exception.getConstraintViolations().forEach(
				t -> errorMap.put(t.getPropertyPath().toString(), t.getMessage()));
		}
		return ResponseEntity.badRequest().body(errorMap);
	}

}
