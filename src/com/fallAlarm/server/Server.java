package com.fallAlarm.server;

/*------------------------------------------------
Connection 1 received from: localhost
Got I/O streams
Sending message "Connection successful"
Client message: Thank you.
Transmission complete. Closing socket.
------------------------------------------------
// Fig. 16.3: Server.java
// Set up a Server that will receive a connection
// from a client, send a string to the client,
// and close the connection.
*/import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

import com.capstone.data.DeviceData;
import com.fallAlarm.jdbc.PatientDAO;

class CloseWindowAndExit1 extends WindowAdapter {
   public void windowClosing( WindowEvent e )
   {
      System.exit( 0 );
   }
}

public class Server extends Frame {
   private TextArea display;
   public double ax,ay,az;
   public double a_norm;
   public int i=0;
   static int BUFF_SIZE=50;
   static public double[] window = new double[BUFF_SIZE];
   double sigma=0.5,th=10,th1=5,th2=2;
   public static String curr_state,prev_state;
   
   
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
      int counter = 1;

      try {
         // Step 1: Create a ServerSocket.
         // Change the port number from 5000 to other number
         // if you encounter JVM_bind error
         // when running this program.
         server = new ServerSocket( 5000, 100 );

         while ( true ) {
            //Step 2: Wait for a connection.
            connection = server.accept();

     //       display.append( "Connection " + counter + " received from: " +
      //         connection.getInetAddress().getHostName() );

            //Step 3: Get input and output streams.
            input = new ObjectInputStream(connection.getInputStream() );
            //output = new DataOutputStream(connection.getOutputStream() );
            
            DeviceData dataMsg = (DeviceData) input.readObject();

//            display.append( "\nGot I/O streams\n" );
//            display.append( "Connection successful\"\n" );
            //output.writeUTF( "Connection successful" );

            
            ax = dataMsg.getAccX();
            ay = dataMsg.getAccY();
            az = dataMsg.getAccZ();

            int classRisk = CalculateClassRisk(ax,ay,az) ;
            
            PatientDAO.insertRecordIntoPatientData(dataMsg,classRisk);
//            display.append( "Client message: \n");
//            display.append("\nAcceleration Values: ");
//            display.append("\n"+dataMsg.getAccX());
//            display.append("\n"+dataMsg.getAccY());
//            display.append("\n"+dataMsg.getAccZ());
//            display.append("\n\nGyroscope Values:");
//            display.append("\n"+dataMsg.getGyrX());
//            display.append("\n"+dataMsg.getGyrY());
//            display.append("\n"+dataMsg.getGyrZ());
//            display.append("\n\nLocation Value:");
//            display.append("\n"+dataMsg.getLocX());
//            display.append("\n"+dataMsg.getLocY());
            
            

            AddData(ax,ay,az);
            posture_recognition(window,ay);
            String str = SystemState(curr_state,prev_state);
            display.append("State is "+str);
            
            if(!prev_state.equalsIgnoreCase(curr_state)){
            	prev_state=curr_state;
            }
           
            if(Math.sqrt(ax*ax+ay*ay+az*az) < 8.8){
            	//display.append("\n!!!!!!! Fall !!!!!!");
            }
           
            // Step 5: Close connection.
//            display.append( "\nTransmission complete. " +
  //                          "Closing socket.\n\n" );
            connection.close();
            ++counter;
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
		 }else{			// (a_norm>=50){
			 classRisk = 1;
		 }
	     return classRisk;  
	}
	

	 private void initialize() {
		// TODO Auto-generated method stub
		 for(i=0;i<BUFF_SIZE;i++){
	    	 window[i]=0;
	     }
		 prev_state="none";
		 curr_state="none";
	            
	    
	}
	 

	private void posture_recognition(double[] window2,double ay2) {
		// TODO Auto-generated method stub
		int zrc=compute_zrc(window2);
		if(zrc==0){
			
			if(Math.abs(ay2)<th1){
				curr_state="sitting";
			}else{
				curr_state="standing";
			}
				
		}else{
			
			if(zrc>th2){
				curr_state="walking";
			}else{
				curr_state="none";
			}
				
		}
			
		
		
	}
	private int compute_zrc(double[] window2) {
		// TODO Auto-generated method stub
		int count=0;
		for(i=1;i<=BUFF_SIZE-1;i++){
			
			if((window2[i]-th)<sigma && (window2[i-1]-th)>sigma){
				count=count+1;
			}
			
		}
		return count;
	}
	private String SystemState(String curr_state1,String prev_state1) {
		// TODO Auto-generated method stub
			String state="";
	        	//Fall !!
             if(!prev_state1.equalsIgnoreCase(curr_state1)){
           	  if(curr_state1.equalsIgnoreCase("fall")){
           		 // m1_fall.start();
           		state = "FALL";
           	  }
           	  if(curr_state1.equalsIgnoreCase("sitting")){
           		  //m2_sit.start();
             		state = "SITTING";
           	  }
           	  if(curr_state1.equalsIgnoreCase("standing")){
           		  //m3_stand.start();
             		state = "STANDING";
           	  }
           	  if(curr_state1.equalsIgnoreCase("walking")){
           		  //m4_walk.start();
             		state = "WALKING";
           	  }
             }
             
             return state;
	 	 
	 	
	}
	private void AddData(double ax2, double ay2, double az2) {
		// TODO Auto-generated method stub
		 a_norm=Math.sqrt(ax*ax+ay*ay+az*az);
		 for(i=0;i<=BUFF_SIZE-2;i++){
	    	window[i]=window[i+1];
	     }
	     window[BUFF_SIZE-1]=a_norm;
	       
	}
	
//	public void exit_app(View view){
//		   finish();
//	      
//		   
//	}
	   /////////////

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