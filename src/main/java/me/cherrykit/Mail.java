package me.cherrykit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Mail {

	private static Connection conn;
	
	//Connects to database
	public static void getConnection() {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/mails?useSSL=false", "sqluser", "sqluserpw");
			System.out.println("Connected to database");
		} catch (Exception e) {
			System.out.println("Error wile connecting: ");
		}
	}
	
	//Adds mail to database in addressee's table
	public static void saveMail(String addressee, String message, String sender) {
		PreparedStatement ps;
		
		//Creates table for addressee if it doesn't exist yet
		if (getMail(addressee).isEmpty()) {
			try {
				String query = "create table " + addressee + " (message varchar(300), sender varchar(16))";
				ps = conn.prepareStatement(query);
				
				ps.executeUpdate();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		
		//Inserts mail into table
		try {
			String query = "insert into " + addressee + " (message, sender) values (?, ?)";
			ps = conn.prepareStatement(query);
			ps.setString(1, message);
			ps.setString(2, sender);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}	
	}
	
	//Returns players mail - returns empty ArrayList if table doesn't exist
	public static ArrayList<String> getMail(String pname) {
		ArrayList<String> mails = new ArrayList<String>();
		
		try {
			String query = "select * from " + pname;
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			//While there is still mail:
			while (rs.next()) {
				String message = rs.getString("message");
				String sender = rs.getString("sender");
				mails.add("Message from " + sender + ": " + message);
			}
			
			//Adds no mail message if no messages are stored
			if (mails.isEmpty()) {
				mails.add("You have no new messages");
			}
			
			return mails;
			
		} catch (Exception e) {
			System.out.println(e);
			return mails;
		}
	}
	
	//Deletes a players mail
	public static void deleteMail(String pname) {
		try {
			String query = "delete from " + pname;
			PreparedStatement ps = conn.prepareStatement(query);
			ps.executeUpdate();
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	//Disconnects from database
	public static void closeConnection() {
		try {
			conn.close();
		} catch (Exception e) {
			System.out.println("Failed to disconnect: " + e);
		}
	}
	
}
