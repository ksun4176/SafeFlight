package com.SafeFlight;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
		obj.put("flights", new JSONArray());
		
		
		Connection conn;
		try {
			
			String fromAirport = request.getParameter("fromAirportID");
			String toAirport = request.getParameter("toAirportID");
			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			conn = ConnectionUtils.getMyConnection();
			
				
			String query = "{CALL getFlights()}";
			CallableStatement stmt = conn.prepareCall(query);
			
			ResultSet rs = stmt.executeQuery();
			
			ArrayList<Integer> flightNos = new ArrayList<Integer>();
			
			while(rs.next()) {
				String days2 = rs.getString("DaysOperating");
				int flightNo = rs.getInt("FlightNo");
				if (fromDate != null && toDate != null) {
					System.out.println("filter by date");
					String days = DBUtils.getDaysOfWeek(fromDate, toDate);
					if (!DBUtils.MatchDays(days, days2).equals("0000000")) {
						flightNos.add(flightNo);
					}
				}
				else {
					flightNos.add(flightNo);
				}
			}
			if (fromAirport != null && toAirport != null) {
				System.out.println("filter by airport");
				ArrayList<Integer> test = new ArrayList<Integer>();

				for (int flightNo: flightNos) {
					query = "{CALL getFirstLeg(?)}";
					stmt = conn.prepareCall(query);
					stmt.setInt(1, flightNo);
					
					rs = stmt.executeQuery();
					
					rs.next();
					String depAirport = rs.getString("DepAirportID");
					
					query = "{CALL getLastLeg(?)}";
					stmt = conn.prepareCall(query);
					stmt.setInt(1, flightNo);
					
					rs = stmt.executeQuery();
					
					rs.next();
					String arrAirport = rs.getString("ArrAirportID");
					
					
					if (depAirport.equals(fromAirport) && arrAirport.equals(toAirport)) {
						test.add(flightNo);
					}

				}
				
				flightNos = test;
				
			}
			JSONArray arr = new JSONArray();
			
			for (int flightNo: flightNos) {
				query = "{CALL getFlight(?)}";
				stmt = conn.prepareCall(query);
				stmt.setInt(1, flightNo);
				rs = stmt.executeQuery();
				rs.next();
				
				String airlineID = rs.getString("AirlineID");
				String daysOperating = rs.getString("DaysOperating");
				
				query = "{CALL getFirstLeg(?)}";
				stmt = conn.prepareCall(query);
				stmt.setInt(1, flightNo);
				rs = stmt.executeQuery();
				rs.next();
				
				String depAirportID = rs.getString("DepAirportID");
				Time depTimeDate = rs.getTime("DepTime");
				String depTime = depTimeDate.toString();

				
				query = "{CALL getLastLeg(?)}";
				stmt = conn.prepareCall(query);
				stmt.setInt(1, flightNo);
				rs = stmt.executeQuery();
				rs.next();
				
				String arrAirportID = rs.getString("ArrAirportID");
				Time arrTimeDate = rs.getTime("ArrTime");
				String arrTime = arrTimeDate.toString();
				int legNo = rs.getInt("LegNo");
				
				JSONObject flight = new JSONObject();
				flight.put("airline_id", airlineID);
				flight.put("flightNumber", flightNo);
				flight.put("LegNumber", legNo);
				flight.put("depAirportID", depAirportID);
	        		flight.put("arrAirportID", arrAirportID);
	        		flight.put("depTime", depTime);
	        		flight.put("arrTime", arrTime);
	        		flight.put("daysOfWeek", daysOperating);
	        		
	        		query = "{CALL getFare(?)}";
				stmt = conn.prepareCall(query);
				stmt.setInt(1, flightNo);
				rs = stmt.executeQuery();
				
				JSONObject fare = new JSONObject();
				JSONObject oneway = new JSONObject();
				JSONObject roundtrip = new JSONObject();
				while(rs.next()) {
					if (rs.getString("FareType").equals("one-way")) {
						oneway.put(rs.getString("Class"), rs.getBigDecimal("Fare"));
					}
					else if (rs.getString("FareType").equals("roundtrip")) {
						roundtrip.put(rs.getString("Class"), rs.getBigDecimal("Fare"));
					}
				}
				fare.put("one-way", oneway);
				fare.put("roundtrip", roundtrip);
				flight.put("price", fare);
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
