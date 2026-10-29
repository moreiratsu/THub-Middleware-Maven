package com.alvaro.thub.service.impl;

import java.util.List;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

import com.alvaro.thub.service.MailService;

/* Implementacion del servicio de correo usando Apache Commons Email
 * Requiere la libreria Apache Commons Email en el classpath
 * https://commons.apache.org/proper/commons-email/
 */

public class MailServiceApacheImpl implements MailService {
	
	public MailServiceApacheImpl() {
		// Constructor
	}
	
	
	// Metodo para enviar un email
	public void sendEmail(String para, String asunto, String contenido) {
		try {
		Email email = new SimpleEmail();
		email.setHostName("smtp.gmail.com");
		email.setSmtpPort(587);
		email.setStartTLSEnabled(true); // Importante para Gmail
		email.setSSLOnConnect(false); // Usamos STARTTLS, no SSL directo
		email.setFrom("tournamenthub2026@gmail.com", "THUB");
		email.setAuthentication("tournamenthub2026@gmail.com", "dinc giok wtyy axvw");
		email.setSubject(asunto);
		email.setMsg(contenido);
		email.addTo(para);
		email.send();
		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main (String[]args) {
		MailServiceApacheImpl mailService = new MailServiceApacheImpl();
		mailService.sendEmail("joseantoniolp.teacher@gmail.com", "Wellcome", "Probando email");
	}


	@Override
	public void sendEmailMultipleRecipients(List<String> destinatarios, String asunto, String contenido) {
		for (String destinatario : destinatarios) {
			sendEmail(destinatario, asunto, contenido);
		}
		System.out.println("Enviados emails a " + destinatarios.size() + " destinatarios.");
	}
}
