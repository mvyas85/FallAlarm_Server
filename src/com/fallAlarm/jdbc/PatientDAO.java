package com.fallAlarm.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import com.capstone.data.DeviceData;
import com.capstone.data.Patient;


public class PatientDAO {

	public static int insertPatientData(Patient patient) throws SQLException{
		int id=0;
			Connection connection=JDBC.getConnection();
			String query = "INSERT INTO `FallAlarm`.`PATIENT`"+
					" (`PID`, `NAME`, `Gender`, `DOB`, `EMAIL`, `PHONE`, `ADDRESS`, `Zip`) " +
					"VALUES (?,?,?,?,?,?,?,?)";

			java.util.Date myUtilDate = patient.getDate();  
			java.sql.Date mySqlDate = new java.sql.Date(myUtilDate.getTime());  
			
			PreparedStatement stmt = connection.prepareStatement(query);
			
			        stmt.setString(1,patient.getPid()); 
			        stmt.setString(2,patient.getName());//(java.sql.Date) mySqlDate
			        stmt.setString(3,patient.getGender());
			        stmt.setDate(4,(java.sql.Date)mySqlDate);
			        stmt.setString(5,patient.getEmail());
			        stmt.setString(6,patient.getPhone());
			        stmt.setString(7,patient.getAddress());
			        stmt.setString(8,patient.getZip());
			 
			        stmt.executeUpdate();
			        
			 connection.close();
		return id;
	}
	
	public static Patient getAllPatientInfo(String pid) throws SQLException{
		Patient patients = null;
		try {
			Connection connection=JDBC.getConnection();
			
			Statement stmt = connection.createStatement();
			String query = "SELECT * FROM `FallAlarm`.`PATIENT` where PID ='" + pid +"';";
			
        	ResultSet res = stmt.executeQuery(query);
			
			while(res.next()){
				patients = new Patient(res.getString("PID"),
						res.getString("NAME"),
						res.getString("Gender"),
						res.getDate("DOB"),
						res.getString("EMAIL"),
						res.getString("PHONE"),
						res.getString("ADDRESS"),
						res.getString("Zip"));
				
				
			 }
			 connection.close();
        } 
		catch (Exception ex) {
			ex.printStackTrace(); 
        }
		return patients;
	}
	public static ArrayList<String> getAllPatientIDs() throws SQLException{
			ArrayList<String> Ids = new ArrayList<String>();
			try {
				Connection connection=JDBC.getConnection();
				
				Statement stmt = connection.createStatement();
				String query = "SELECT PID FROM `FallAlarm`.`PATIENT`;";
	        	ResultSet res = stmt.executeQuery(query);
				
				while(res.next()){
					 Ids.add(res.getString("pid"));
				 }
				 connection.close();
	        } 
			catch (Exception ex) {
				ex.printStackTrace(); 
	        }
			return Ids;
	}
	
//	public static void insertPatient(String pid, String name, String gender,
//			Date date, String email, String phone, String address,String zip){
//		try {
//			Connection connection=JDBC.getConnection();
//			
//
//			java.util.Date dob_temp = date;  
//			java.sql.Date dob = new java.sql.Date(dob_temp.getTime()); 
//			
//			Statement stmt = connection.createStatement();
//			String query = "INSERT INTO `FallAlarm`.`PATIENT`"
//					+ " (`PID`, `NAME`, `Gender`, `DOB`, `EMAIL`, `PHONE`, `ADDRESS`, `Zip`) "
//					+ "VALUES ('"+pid+"', '"+name+"', '"+gender+"', '"+dob+"', "
//							+ "'"+email+"', '"+phone+"', '"+address+"', '"+zip+"')";
//
//			stmt.executeUpdate(query);
//			System.out.println("insertPatient : " + query);
//			connection.close();
//	    } 
//		catch (Exception ex) {
//			ex.printStackTrace(); 
//	    }
//
//	}
}
