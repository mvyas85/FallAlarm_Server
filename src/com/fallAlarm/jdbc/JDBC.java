package com.fallAlarm.jdbc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC {
	private static String DB_DRIVER_CLASS="com.mysql.jdbc.Driver";
	private static String DB_URL="jdbc:mysql://localhost/FallAlarm";
	private static String DB_USERNAME="root";
	private static String DB_PASSWORD="1234567890";
	
	 public static Connection getConnection(){
		
		 Connection conn = null;
		   try{
		      Class.forName(DB_DRIVER_CLASS);

		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);

		   }catch(SQLException se){
		      se.printStackTrace();
		   }catch(Exception e){
		      e.printStackTrace();
		   }
		   return conn;
	}
}
	