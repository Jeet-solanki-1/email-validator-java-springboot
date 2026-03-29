package com.jlss.email_validator.service;

import org.springframework.stereotype.Service;
import com.jlss.email_validator.model.EmailValidationResponse;
import java.util.regex.Pattern;

@Service
public class EmailValidationService{
	private static final String EMAIL_REGEX=
	"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

	public EmailValidationResponse validateEmail(String email) throws Exception{
		boolean syntaxValid = validSyntax(email);
		if (!syntaxValid){
			return new EmailValidationResponse(
				email,false,false,"Invalid email format. Exmaple: user@domain.com", null);
		}

		return null;
	}
	private boolean validSyntax(String email){
		if (email==null || email.isEmpty()){
			return false;
		}
		return EMAIL_PATTERN.matcher(email).matches();
	}

}	