package com.onlinebankingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.onlinebankingsystem.resource.EmailMessage;
import com.onlinebankingsystem.service.EmailSenderServiceImpl;

@RestController
public class EmailController {
	
private final EmailSenderServiceImpl emailSenderService;

@Autowired
public EmailController(EmailSenderServiceImpl emailSenderService) {
	this.emailSenderService = emailSenderService;
}

	
	@PostMapping("send-email")
	public ResponseEntity sendEmail(@RequestBody EmailMessage emailMessage) {
		
		this.emailSenderService.sendEmail(emailMessage.getTo(), emailMessage.getSubject(), emailMessage.getMessage());
		
		return ResponseEntity.ok("Success");
	}
}
