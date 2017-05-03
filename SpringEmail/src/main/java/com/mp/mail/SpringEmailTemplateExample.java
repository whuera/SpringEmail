package com.mp.mail;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.app.conn.conexionOracle;
import com.app.modelo.Contacto;

public class SpringEmailTemplateExample {
	@Autowired
	private static conexionOracle conn;
	// @Autowired
	// private static Contacto contacto;

	public static void main(String[] args) {
		 conn = new conexionOracle();
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-beans.xml");
		Mailer mailer = (Mailer) context.getBean("mailer");

		// conn = new conexionOracle();
		Connection conexion = conn.getConnection();
		Contacto contacto = new Contacto();
		ArrayList<Contacto> listContacto = new ArrayList<Contacto>();
/*
		contacto.setPrimer_nombre("William");
		contacto.setPrimer_apellido("Huera");
		contacto.setEmail_primario("whuera@gmail.com");

		listContacto.add(contacto);

		Contacto contacto2 = new Contacto();
		contacto2.setPrimer_nombre("Eduardo");
		contacto2.setPrimer_apellido("Huera");
		contacto2.setEmail_primario("eduardohuera@gmail.com");

		listContacto.add(contacto2);
		*/
		
		try {
			Statement st = conexion.createStatement();
			ResultSet rs = st.executeQuery("select * from (select c.*, rownum r from STG_CONTACT c ) where email_primario is not null and r BETWEEN 1000 AND 2000" );
			 while (rs.next())
			 {
				 Contacto contactoBdd = new Contacto();
				 contactoBdd.setPrimer_nombre(rs.getString("PRIMER_NOMBRE"));
				 contactoBdd.setSegundo_nombre(rs.getString("SEGUNDO_NOMBRE"));
				 contactoBdd.setPrimer_apellido(rs.getString("PRIMER_APELLIDO"));
				 contactoBdd.setSegundo_apellido(rs.getString("SEGUNDO_APELLIDO"));
				 contactoBdd.setEmail_primario(rs.getString("EMAIL_PRIMARIO").toLowerCase());
				 listContacto.add(contactoBdd);
			 }
			 rs.close();
	         st.close();
	         conexion.close();
	         System.out.println(listContacto.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		Mail mail = new Mail();
		/*
		mail.setMailFrom("publicidadmobilpymes@gmail.com");
		mail.setMailBcc("whuera@gmail.com");
		mail.setMailTo("freddy.lojan@etafashion.com");
		mail.setMailSubject("Promociones Mobilpymes.com");
		mail.setTemplateName("emailtemplate.vm");
		*/

		for (Contacto contactoFinal : listContacto) {
			mail.setMailFrom("publicidad@mobilpymes.com");
			mail.setMailTo(contactoFinal.getEmail_primario().toString().trim());
			mail.setMailSubject("Promociones Mobilpymes.com");
			mail.setTemplateName("emailtemplate.vm");
			try {
				System.out.println("envia email: "+contactoFinal.getEmail_primario()+"\n");
				mailer.sendMail(mail, contactoFinal.getPrimer_nombre(), contactoFinal.getPrimer_apellido());
				
				
					  Thread.sleep(5000);
					
				
			} catch (ResourceNotFoundException e) {

				e.printStackTrace();
			} catch (ParseErrorException e) {

				e.printStackTrace();
			} catch (MethodInvocationException e) {

				e.printStackTrace();
			} 
			catch (IOException e) {

				e.printStackTrace();
			}
			 catch (InterruptedException ie) {
				    //Handle exception
				}
			contactoFinal = null;
		}

	}

}
