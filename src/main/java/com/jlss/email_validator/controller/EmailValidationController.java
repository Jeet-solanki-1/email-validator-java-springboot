package com.jlss.email_validator.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.jlss.email_validator.model.EmailValidationResponse;
import com.jlss.email_validator.service.EmailValidationService;

@RestController
public class EmailValidationController{
	private final EmailValidationService service;

	public EmailValidationController(EmailValidationService service){
		this.service=service;
	}

	@GetMapping("/email")
	public ResponseEntity<EmailValidationResponse> validateEmail(@RequestParam String email){
		try{
			EmailValidationResponse resp = service.validateEmail(email);
			return ResponseEntity.ok(resp);
		}
		catch(Exception e){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}