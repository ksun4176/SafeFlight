package com.SafeFlight;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Servlet implementation class HelloServlet
 */

public class Flights extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Flights() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		JSONObject json = new JSONObject();
		JSONArray jArray = new JSONArray();
		
		try {
			Connection conn = ConnectionUtils.getMyConnection();
			 System.out.println("Get connection " + conn);
		        
		        // Testing SQL
		        Statement statement = conn.createStatement();
		        String sql = "{CALL getFlights()}";
		        ResultSet rs = statement.executeQuery(sql);
		        
		        while(rs.next()) {
		        		String airlineId = rs.getString("AirlineID");
		        		int flightNo = rs.getInt("FlightNo");
		        		int noOfSeats = rs.getInt("NoOfSeats");
		        		String depAirportId = rs.getString("DaysOperating");
		        		int minLengthOfStay = rs.getInt("MinLengthOfStay");
		        		int maxLengthOfStay = rs.getInt("MaxLengthOfStay");
		        		
		        		JSONObject o = new JSONObject();
		        		o.put("airline_id", airlineId);
		        		o.put("flightNumber", flightNo);
		        		o.put("NoOfSeats", noOfSeats);
		        		o.put("minLengthOfStay", minLengthOfStay);
		        		o.put("maxLengthOfStay", maxLengthOfStay);
		        	
		        		
		        		
		        		jArray.add(o);
		        }
        		ConnectionUtils.close(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
        json.put("flights", jArray);
	    
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(json);
		response.getWriter().flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	
	
}
