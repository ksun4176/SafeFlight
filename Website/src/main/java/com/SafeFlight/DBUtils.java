package com.SafeFlight;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.simple.JSONObject;


public class DBUtils {

	public static JSONObject findUser(String username, String password) throws SQLException, ClassNotFoundException {
		Connection conn = ConnectionUtils.getMyConnection();
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
			ConnectionUtils.close(conn);
		   return o;
		}
		
		ConnectionUtils.close(conn);
		return null;
	}
	
	public static int getDaysOfWeek(String fromDate) throws ParseException {
		if(fromDate == null) {
			return 7;
		}
		DateFormat df = new SimpleDateFormat("YYYY-MM-DD");
		Date fDate = df.parse(fromDate);
//		Date tDate = null;
//		int dayOfWeekFrom = 0;
//		int dayOfWeekTo = 0;
		Calendar c = Calendar.getInstance();
		c.setTime(fDate);
		return c.get(Calendar.DAY_OF_WEEK) - 1;
//		if(toDate != null) {
//			tDate = df.parse(toDate);
//			Calendar d = Calendar.getInstance();
//			d.setTime(tDate);
//			dayOfWeekTo = d.get(Calendar.DAY_OF_WEEK);
//		}
//		if (fDate != null && tDate != null && tDate.getTime() - fDate.getTime() < 0 ) {
//			throw new RuntimeException("FromDate is after ToDate");
//		}
		
//		long diff = tDate.getTime() - fDate.getTime();
//		long diffDays = diff / (24 * 60 * 60 * 1000);
		
//		String[] days = new String[] {"0", "0", "0", "0", "0", "0", "0"};
//		
//		
//		if (diffDays >= 7) {
//			days = new String[] {"1", "1", "1", "1", "1", "1", "1"};
//		}
//		else if (dayOfWeekFrom == dayOfWeekTo) {
//			days[dayOfWeekFrom - 1] = "1"; 
//		}
//		else if (dayOfWeekFrom < dayOfWeekTo) {
//			for(int i = dayOfWeekFrom - 1;i < dayOfWeekTo; i++) {
//				days[i] = "1";
//			}
//		}
//		
//		else {
//			for (int i = 0; i < dayOfWeekTo; i++) {
//				days[i] = "1";
//			}
//			for (int i = dayOfWeekFrom - 1; i < days.length; i++) {
//				days[i] = "1";
//			}
//		}
//		
//		return String.join("", days);
	}
	
//	public static String MatchDays(String days, String days2) {
//		String ret = "";
//		
//		for (int i = 0; i < 7; i++) {
//			if (days.charAt(i) == '1' && days2.charAt(i) == '1') {
//				ret += '1';
//			}
//			else {
//				ret += '0';
//			}
//		}
//		
//		return ret;
//	}
	
}
