package application.components;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {
	
	private String pass;
	private String user;
	private String to;
	private String subject;
	private String messageText = System.getProperty("user.name") + " write ->";
	private Properties props = System.getProperties();
	
	public void setCredentials(String username,String password,String to) {
		if(!username.contains("@gmail"))
			throw(new IllegalArgumentException("solo account gmail"));
		this.user=username;
		this.to=to;
		this.pass=password;

	}
	public Email() {
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.required", "true");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setText(String text) {
		this.messageText = text;
	}


	public synchronized void send() throws MessagingException {

		Session mailSession = Session.getDefaultInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, pass);
			}
		});

		mailSession.setDebug(false);
		Message msg = new MimeMessage(mailSession);
		msg.setFrom(new InternetAddress(user));
		InternetAddress[] address = { new InternetAddress(to) };
		msg.setRecipients(Message.RecipientType.TO, address);
		msg.setSubject(subject); // i recipents
		msg.setSentDate(new Date());
		msg.setText(messageText);
		Transport transport = mailSession.getTransport("smtp");
		transport.connect("smtp.gmail.com", user, pass);
		transport.sendMessage(msg, msg.getAllRecipients());
		transport.close();
	}
}
