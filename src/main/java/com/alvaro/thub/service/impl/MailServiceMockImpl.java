package com.alvaro.thub.service.impl;

import java.util.List;
import com.alvaro.thub.service.MailService;

/* Implementacion mock del servicio de correo
 * Solo imprime en consola los detalles del email que se "envia"
 */
public class MailServiceMockImpl implements MailService {
	
	public MailServiceMockImpl() {
		// Constructor
	}
	
	
	@Override
	public void sendEmail(String para, String asunto, String contenido) {
		System.out.println("Enviado email a " + para+": Asunto: " + asunto);
		System.out.println(contenido);
		System.out.println("Enviado.");
	}

	@Override
	public void sendEmailMultipleRecipients(List<String> destinatarios, String asunto, String contenido) {
		for (String destinatario : destinatarios) {
			sendEmail(destinatario, asunto, contenido);
		}
		System.out.println("Enviados emails a " + destinatarios.size() + " destinatarios.");
	}


	

	
	
}
