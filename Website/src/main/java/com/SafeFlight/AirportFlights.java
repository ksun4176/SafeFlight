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
		JSONArray flights = new JSONArray();
		
		try {
			Connection conn = ConnectionUtils.getMyConnection();
			String airport_id = request.getParameter("airport_id");
			 System.out.println("Get connection " + conn);
		       
		     // Testing SQL
			String query = "{CALL getFlightsAtAirport(?)}";
			CallableStatement stmt = conn.prepareCall(query);
			stmt.setString(1, airport_id);
			ResultSet rs = stmt.executeQuery();
			
		    while(rs.next()) {
		    		String airline_id = rs.getString("AirlineID");
		    		System.out.println(airline_id);
		        	int flightNo = rs.getInt("FlightNo");
		        	int legNumber = rs.getInt("LegNo");
		        		
		        	JSONObject o = new JSONObject();
		        	o.put("airline_id", airline_id);
		        	o.put("flightNumber", flightNo);
		        	o.put("legNumber", legNumber);
		        	flights.add(o);
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        json.put("flights", flights);
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
