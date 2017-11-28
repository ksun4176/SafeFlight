package com.SafeFlight;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

 
public class ConnectionUtils {
 
    public static Connection getMyConnection() throws SQLException,
            ClassNotFoundException {
    		
       return MySQLConnUtils.getMySQLConnection();
    }
    
    
}