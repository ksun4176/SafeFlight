package com.SafeFlight;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Servlet implementation class AirportFlights
 */
public class AirportFlights extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AirportFlights() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Servlet is called.");
		JSONObject json = new JSONObject();
		JSONArray jArray = new JSONArray();
		
		try {
			Connection conn = ConnectionUtils.getMyConnection();
			String airport_id = request.getParameter("airport_id");
			String query = "{CALL getFlightsAtAirport(?)}";
			CallableStatement stmt = conn.prepareCall(query);
			stmt.setString(1, airport_id);
			ResultSet rs = stmt.executeQuery();
			
		    while(rs.next()) {
	        		JSONObject o = new JSONObject();
		    		String airline_id = rs.getString("AirlineID");
		        	String flightNo = rs.getString("FlightNo");
		        	String legNumber = rs.getString("LegNo");
		        	String depAirportID = rs.getString("DepAirportID");
		        	String arrAirportID = rs.getString("ArrAirportID");
		        	String depTime = rs.getString("DepTime");
		        	String arrTime = rs.getString("ArrTime");
		        	o.put("airline_id", airline_id);
		        	o.put("flightNumber", flightNo);
		        	JSONObject legsInfo = new JSONObject();
		        	legsInfo.put("depAirportID", depAirportID);
		        	legsInfo.put("arrAirportID", arrAirportID);
		        	legsInfo.put("depTime", depTime);
		        	legsInfo.put("arrTime", arrTime);
		        	JSONObject legs = new JSONObject();
		        	legs.put(legNumber, legsInfo);
		        	o.put("legs", legs);
		        	
		        	query = "{CALL getFlight(?,?)}";
		        	stmt = conn.prepareCall(query);
		        	stmt.setString(1, airline_id);
		        	stmt.setString(2, flightNo);
		        	ResultSet rs2 = stmt.executeQuery();
		        	rs2.next();
		        	o.put("daysOfWeek", rs2.getString("DaysOperating"));
		        	
		        	JSONObject fares = new JSONObject();
	        		JSONObject fareInfo = new JSONObject();
	        		String temp = null;
	        		query = "{CALL getFare(?,?)}";
	        		stmt = conn.prepareCall(query);
	        		stmt.setString(1, airline_id);
	        		stmt.setString(2, flightNo);
	        		rs2 = stmt.executeQuery();
	        		while(rs2.next()) {
	        			String fare_type = rs2.getString("FareType");
	        			if(temp != null && !temp.equals(fare_type)) {
	        				fares.put(temp, fareInfo);
	        				fareInfo = new JSONObject();
	        			}
	        			fareInfo.put(rs2.getString("Class"), rs2.getString("Fare"));
	        			temp = fare_type;
	        		}
	        		if(temp != null) {
	        			fares.put(temp, fareInfo);
	        		}
	        		o.put("prices", fares);
		        	
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
