package com.jlss.email_validator.service;

import org.springframework.stereotype.Service;
import com.jlss.email_validator.model.EmailValidationResponse;

import java.util.regex.Pattern;

import org.xbill.DNS.Record;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Type;
import org.xbill.DNS.Lookup;



@Service
public class EmailValidationService{
	private static final String EMAIL_REGEX=
	"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+(\\.[a-zA-Z]{2,})?$";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

	public EmailValidationResponse validateEmail(String email) throws Exception{
		boolean syntaxValid = validSyntax(email);
		if (!syntaxValid){
			return new EmailValidationResponse(
				email,false,false,"Invalid email format. Exmaple: user@domain.com", null);
		}	

		String domain = email.substring(email.indexOf('@')+1);

		MXLookupResult mxResult = performMXLookup(domain);

		return new EmailValidationResponse(
			email,true, mxResult.hashMX,
			mxResult.message,
			mxResult.mxRecords
		);
	}

	private boolean validSyntax(String email){
		if (email==null || email.isEmpty()){
			return false;
		}
		return EMAIL_PATTERN.matcher(email).matches();
	}

	private MXLookupResult performMXLookup(String domain){
		try{
			Record[] records = new Lookup(domain, Type.MX).run();
			if (records!=null && records.length>0){
				StringBuilder sb = new StringBuilder();

				for(Record record: records) {
					MXRecord mx = (MXRecord) record;
					sb.append(mx.getTarget())
						.append(" (priority: ")
						.append(mx.getPriority())
						.append(")\n");
				}
				return new MXLookupResult(true,"MX records found", sb.toString());
			}
			// fallback call : check A record
			Record[] aRecords = new Lookup(domain,Type.A).run();

			if (aRecords!=null && aRecords.length>0){
				return new MXLookupResult(
					true, "No MX records exists (may still accept email",
				"A record fallback"

			);
			}
			return new MXLookupResult(false, "No mail servers found", null);
		}
		catch(Exception e){
			return new MXLookupResult(false,"DNS lookup failed: "+ e.getMessage(), null);
		}
	}

	private static class MXLookupResult{
		boolean hashMX;
		String message;
		String mxRecords;
		MXLookupResult(boolean hashMX, String message, String mxRecords){
			this.hashMX=hashMX;
			this.message=message;
			this.mxRecords=mxRecords;
		}
	}

}	