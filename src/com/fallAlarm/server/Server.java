package com.fallAlarm.server;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import com.capstone.data.DeviceData;
import com.fallAlarm.jdbc.PatientDataDAO;

class CloseWindowAndExit1 extends WindowAdapter {
   public void windowClosing( WindowEvent e )
   {
      System.exit( 0 );
   }
}

public class Server extends Frame {
   private TextArea display;
   private double ax,ay,az;
   private double a_norm;
   private int counter=1;
   
   
   public Server()
   {
      super( "Server" );
      display = new TextArea( "\n", 0, 0,
                    TextArea.SCROLLBARS_VERTICAL_ONLY );
      add( display, BorderLayout.CENTER );
      setSize( 300, 150 );
      setVisible( true );
   }

   public void runServer()
   {
      ServerSocket server;
      Socket connection;
      ObjectInputStream input;

      try {
         server = new ServerSocket( 5000, 100 );

         while ( true ) {
            connection = server.accept();

            display.append( "Connection " + counter + " received from: " +
               connection.getInetAddress().getHostName() );

            input = new ObjectInputStream(connection.getInputStream() );
            
            DeviceData dataMsg = (DeviceData) input.readObject();

            display.append( "Connection successful\"\n" );

            ax = dataMsg.getAccX();
            ay = dataMsg.getAccY();
            az = dataMsg.getAccZ();

            int classRisk = CalculateClassRisk(ax,ay,az) ;
            
            PatientDataDAO.insertRecordIntoPatientData(dataMsg,classRisk);
            display.append( "Client message: \n");
            display.append("Acceleration Values: ");
            display.append("\n"+dataMsg.getAccX());
            display.append("\t\t"+dataMsg.getAccY());
            display.append("\t\t"+dataMsg.getAccZ());
            display.append("\nGyroscope Values:");
            display.append("\n"+dataMsg.getGyrX());
            display.append("\t\t"+dataMsg.getGyrY());
            display.append("\t\t"+dataMsg.getGyrZ());
            display.append("\nLocation Value:");
            display.append("\n"+dataMsg.getLocX());
            display.append("\t\t"+dataMsg.getLocY());
        
            if(classRisk == 5){
            	display.append("\n\n!!!!!!!!!!!!! Fall !!!!!!!!!!!!!!!\n\n");
            	SendHTMLEmail.sendMsgToDoctor("4087079708@txt.att.net",dataMsg.getDeviceID());
            	SendHTMLEmail.sendMsgToDoctor("mvyas85@gmail.com",dataMsg.getDeviceID());	
            }

           display.append( "\nTransmission complete. Closing socket.\n\n" );
            connection.close();
            counter++;
         }
      }
      catch ( IOException e ) {
         e.printStackTrace();
      }catch (ClassNotFoundException e) {
			e.printStackTrace();
      }
   }
   
	private int CalculateClassRisk(double ax2, double ay2, double az2) {
		 a_norm=Math.sqrt(ax*ax+ay*ay+az*az);
		 int classRisk ;
		 
		 if( a_norm<7){
			 classRisk = 5;
		 }else if(a_norm>=7 && a_norm<8.0){
			 classRisk = 4;
		 }else if(a_norm>=8.0 && a_norm<8.9){
			 classRisk = 3;
		 }else if(a_norm>=8.9 && a_norm<9.8){
			 classRisk = 2;
		 }else if(a_norm>=9.8 && a_norm<50){
			 classRisk = 1;
		 }else{	
			 classRisk = 1;
		 }
	     return classRisk;  
	}

   public static void main( String args[] )
   {
      Server s = new Server();

      s.addWindowListener(new WindowAdapter(){  
          public void windowClosing(WindowEvent wevt){  
              System.exit(0);  
          }  
       });
      s.runServer();
   }
}