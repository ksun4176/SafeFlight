package com.SafeFlight;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Servlet implementation class Flight
 */
public class Flight extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Flight() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject obj = new JSONObject();
		String fromAirport = request.getParameter("fromAirportID");
		String toAirport = request.getParameter("toAirportID");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		Connection conn;
		try {
			conn = ConnectionUtils.getMyConnection();
	        String query = "{CALL getFlight(?, ?, ?, ?)}";
	        CallableStatement stmt = conn.prepareCall(query);
	        
			stmt.setString(1, fromAirport);
			stmt.setString(2, toAirport);
			stmt.setDate(3, new java.sql.Date(df.parse(fromDate).getTime()));
			stmt.setDate(4, new java.sql.Date(df.parse(toDate).getTime()));
		
			
	        ResultSet rs = stmt.executeQuery();
	        
	        
	        JSONArray arr = new JSONArray();
	        while (rs.next()) {
	        		
	        		JSONObject flight = new JSONObject();
	        		flight.put("airlineID", rs.getString("AirlineID"));
	        		flight.put("flightNo",rs.getInt("FlightNo"));
	        		flight.put("legNo", rs.getInt("LegNo"));
	        		flight.put("depAirportID", rs.getString("DepAirportID"));
	        		flight.put("arrAirportID", rs.getString("ArrAirportID"));
	        		flight.put("depTime", rs.getString("DepTime"));
	        		flight.put("arrTime", rs.getString("ArrTime"));
	        		
	        		arr.add(flight);
	        		
	        }
	        obj.put("flights", arr);
	        
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        System.out.println("returning");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(obj);
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
