package com.mp.mail;

import java.io.IOException;
import java.io.StringWriter;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class Mailer {
	private VelocityEngine velocityEngine;

	private JavaMailSender mailSender;

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public void sendMail(Mail mail, String nombre, String apellido)
			throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, IOException {

		MimeMessage message = mailSender.createMimeMessage();
		Template template = null;

		try {

			template = velocityEngine.getTemplate("./templates/" + mail.getTemplateName());

		} catch (ResourceNotFoundException e) {

			e.printStackTrace();
		} catch (ParseErrorException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}

		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put("firstName", nombre);
		velocityContext.put("lastName", apellido);
		velocityContext.put("location", "UIO");
		velocityContext.setAllowRendering(true);
		StringWriter stringWriter = new StringWriter();

		template.merge(velocityContext, stringWriter);

		try {

			message.setSubject(mail.getMailSubject());
			MimeMessageHelper helper;
			helper = new MimeMessageHelper(message, true);
			helper.setFrom(mail.getMailFrom());
			helper.setTo(mail.getMailTo());
			helper.setText(stringWriter.toString(), true);
			mailSender.send(message);

		} catch (Exception ex) {
			System.out.println(ex);
		}

	}
}