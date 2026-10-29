package com.alvaro.thub.service;

import java.util.List;


public interface MailService {
	
	/* Metodo para enviar un email a un destinatario */
	public void sendEmail(String para, String asunto, String contenido)throws MailException;
	
	/* Envia un email a multiples destinatarios */
	public void sendEmailMultipleRecipients(List<String> destinatarios, String asunto, String contenido)throws MailException;
	
	}
