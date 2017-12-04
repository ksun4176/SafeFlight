package com.SafeFlight;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

 
public class ConnectionUtils {
	
	 public static final String ATT_NAME_CONNECTION = "ATTRIBUTE_FOR_CONNECTION";
	 
	 private static final String ATT_NAME_USER_NAME = "ATTRIBUTE_FOR_STORE_USER_NAME_IN_COOKIE";
 
    public static Connection getMyConnection() throws SQLException,
            ClassNotFoundException {
    		
       return MySQLConnUtils.getMySQLConnection();
    }
    
    public static void close(Connection conn) {
    		try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    }
 
}