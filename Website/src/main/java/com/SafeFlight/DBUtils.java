package com.SafeFlight;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONObject;


public class DBUtils {

	public static JSONObject findUser(String username, String password) throws SQLException, ClassNotFoundException {
		Connection conn = ConnectionUtils.getMyConnection();
		UserAccount user = null;
		String query = "{CALL findUser(?, ?)}";
		CallableStatement stmt = conn.prepareCall(query);
		
		
		stmt.setString(1, username);
		stmt.setString(2, password);
		
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			int id = rs.getInt("id");
			String role = rs.getString("role");
		   
		   JSONObject o = new JSONObject();
		   o.put("account_id", id);
		   if (role.equals("customer")) {
			   o.put("accountType", 0);
		   }
		   else if (role.equals("employee")) {
			   o.put("accountType", 1);
		   }
		   else if (role.equals("manager")) {
			   o.put("accountType", 2);
		   }
		   
		   return o;
		}
		
		return null;
	}
	
	
}
