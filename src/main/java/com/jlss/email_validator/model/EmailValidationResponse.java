package com.jlss.email_validator.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailValidationResponse{
	private String email;
	private boolean syntaxValid;
	private boolean domaiExists;
	private String message;
	private String mxRecords;

}