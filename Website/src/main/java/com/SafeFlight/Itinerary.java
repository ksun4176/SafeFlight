package com.SafeFlight;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Servlet implementation class Itinerary
 */
public class Itinerary extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Itinerary() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Connection conn;
		JSONObject json = new JSONObject();
		JSONArray jArray = new JSONArray();
		try {
			conn = ConnectionUtils.getMyConnection();
			String resNo = request.getParameter("reservation_id");
			if(resNo == null) {
				throw new IllegalArgumentException("Must give a reservation_id");
			} else if(!resNo.matches("[0-9]+")){
				throw new IllegalArgumentException("Invalid reservation_id");
			} else {
				String query = "{CALL getItinerary(?)}";
				CallableStatement stmt = conn.prepareCall(query);
				stmt.setInt(1, Integer.parseInt(resNo));
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					JSONObject o = new JSONObject();
					String airlineID = rs.getString("AirlineID");
					int flightNo = rs.getInt("FlightNo");
					int legNo = rs.getInt("LegNo");
					String depAirportID = rs.getString("DepAirportID");
					String depTime = rs.getString("DepTime");
					String arrAirportID = rs.getString("ArrAirportID");
					String arrTime = rs.getString("ArrTime");
					boolean onTime = rs.getBoolean("OnTime");
					o.put("AirlineID",airlineID);
					o.put("FlightNo", flightNo);
					o.put("LegNo", legNo);
					o.put("DepAirportID", depAirportID);
					o.put("ArrAirportID", arrAirportID);
					o.put("DepTime", depTime);
					o.put("ArrTime", arrTime);
					o.put("OnTime", onTime);
					jArray.add(o);
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		json.put("Flights", jArray);
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
