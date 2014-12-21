package com.fallAlarm.server;

import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.capstone.data.Patient;
import com.fallAlarm.jdbc.PatientDAO;
	    
   public class SendHTMLEmail {
    
       /**
        * Utility method to send simple HTML email
        * @param session
        * @param toEmail
        * @param subject
        * @param body
        */
       public static void sendEmail(Session session, String toEmail, String subject, String body){
           try
           {
             MimeMessage msg = new MimeMessage(session);
             //set message headers
             msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
             msg.addHeader("format", "flowed");
             msg.addHeader("Content-Transfer-Encoding", "8bit");
    
             msg.setFrom(new InternetAddress("no_reply@journaldev.com", "Slip Doctor"));
    
             msg.setReplyTo(InternetAddress.parse("no_reply@journaldev.com", false));
    
             msg.setSubject(subject, "UTF-8");
    
             msg.setText(body, "UTF-8");
    
             msg.setSentDate(new Date());
    
             msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
             System.out.println("Message is ready");
             Transport.send(msg);  
    
             System.out.println("EMail Sent Successfully!!");
           }
           catch (Exception e) {
             e.printStackTrace();
           }
       }
		public static void sendMsgToDoctor(String email,String pid){
	     	
	     	 final String fromEmail = "mvyas85@gmail.com"; 
	         final String password = "*****"; //insert your password !! 
	         final String toEmail = email;
	          
	         System.out.println("TLSEmail Start");
	         Properties props = new Properties();
	         props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
	         props.put("mail.smtp.port", "587"); //TLS Port
	         props.put("mail.smtp.auth", "true"); //enable authentication
	         props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
	          
	         Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
	         	protected PasswordAuthentication getPasswordAuthentication() {
	         	return new PasswordAuthentication(fromEmail,password);
	         	}
	         	});
	         
	         Patient patient;
	         String message_body="Attention Doctor, Following patient has follen down" +
			         		"\n\nPID:"+pid ;
			try {
				patient = PatientDAO.getAllPatientInfo(pid);
				message_body += "\nPatient Name:"+patient.getName()+
			         		"\nPhone:"+patient.getPhone();
			         
			         
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			message_body += "\n\nNeed immidiate Assistance !! ";
	         SendHTMLEmail.sendEmail(session, toEmail,"Patient Fell !!", message_body);
		}
   }
	   
