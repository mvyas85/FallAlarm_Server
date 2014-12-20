package com.fallAlarm.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.capstone.data.DeviceData;

public class PatientDataDAO {
public static void printPatient(){
		
		try {
			Connection connection=JDBC.getConnection();
			Statement stmt = connection.createStatement();
			String query = "select * from PATIENT";
        	ResultSet res = stmt.executeQuery(query);
			System.out.println("JDBC SQL Query : " + query);
			
			while(res.next()){
				 System.out.println(res.getInt("pid")+" "+res.getString("Name"));
			 }
			 
			 connection.close();
        } 
		catch (Exception ex) {
			ex.printStackTrace(); 
        }
	}
	
	public static int insertRecordIntoPatientData(DeviceData dataMsg, int classRisk){
		int id=0;
		try {
			Connection connection=JDBC.getConnection();
			//Statement stmt = connection.createStatement();
			String query = "INSERT INTO `FallAlarm`.`PATIENT_DATA` " +
					"(`ID`," +
					"`PID`," +
					"`CurrDate`," +
					"`ACCX`," +
					"`ACCY`," +
					"`ACCZ`," +
					"`GYRX`," +
					"`GYRY`," +
					"`GYRZ`," +
					"`LOCX`," +
					"`LOCY`," +
					"`ClassRisk`)" +
					"VALUES (null,?,?,?,?,?,?,?,?,?,?,?);";
			
			PreparedStatement stmt = connection.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);

			        stmt.setString(1,dataMsg.getDeviceID()); 
			        stmt.setTimestamp(2,getCurrentTimeStamp());
			        stmt.setDouble(3,dataMsg.getAccX());
			        stmt.setDouble(4,dataMsg.getAccY());
			        stmt.setDouble(5,dataMsg.getAccZ());
			        stmt.setDouble(6,dataMsg.getGyrX());
			        stmt.setDouble(7,dataMsg.getGyrY());
			        stmt.setDouble(8,dataMsg.getGyrZ());
			        stmt.setDouble(9,dataMsg.getLocX());
			        stmt.setDouble(10,dataMsg.getLocY());
			        stmt.setInt(11, classRisk);
			 
			        stmt.executeUpdate();
			        
			 connection.close();
        } 
		catch (Exception ex) {
			ex.printStackTrace(); 
        }
		
		return id;
	}
	
	private static java.sql.Timestamp getCurrentTimeStamp() {
		 
		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());
	 
	}
}
