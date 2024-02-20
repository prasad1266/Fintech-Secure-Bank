package com.onlinebankingsystem.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;



@Service
public class EmailSenderServiceImpl implements EmailSenderService {

	private final JavaMailSender mailsender;

	public EmailSenderServiceImpl(JavaMailSender mailsender) {

		this.mailsender = mailsender;
	}
	
	//mime message

	@Override
	public void sendEmail(String to, String subject, String message) {
		// TODO Auto-generated method stub

		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

		simpleMailMessage.setFrom("fintech@gmail.com");
		simpleMailMessage.setTo(to);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(message);

		this.mailsender.send(simpleMailMessage);
	}

}
