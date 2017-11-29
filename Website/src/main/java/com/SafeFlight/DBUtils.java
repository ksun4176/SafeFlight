package com.SafeFlight;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DBUtils {

	public static UserAccount findUser(Connection conn, String username, String password) throws SQLException {
		UserAccount user = null;
		String query = "{CALL findCustomer(?, ?)}";
		CallableStatement stmt = conn.prepareCall(query);
		
		
		stmt.setString(1, username);
		stmt.setString(2, password);
		
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
		  
		   String email = rs.getString("Email");
		   int rating = rs.getShort("Rating");
		   String fname = rs.getString("FirstName");
		   String lname = rs.getString("LastName");
		   String address = rs.getString("Address");
		   String city = rs.getString("City");
		   String state = rs.getString("State");
		   int zipcode = rs.getInt("Zipcode");
		   
		   user = new UserAccount(email, rating, fname, lname, address, city, state, zipcode);
		   break;
		}
		
		return user;
	}
	
	
}
