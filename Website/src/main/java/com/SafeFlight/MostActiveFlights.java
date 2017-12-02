package com.SafeFlight;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Servlet implementation class Popular
 */
public class MostActiveFlights extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MostActiveFlights() {
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
			 System.out.println("Get connection " + conn);
		        
		        // Testing SQL
		        Statement statement = conn.createStatement();
		        String sql = "{CALL getMostActiveFlights}";
		        ResultSet rs = statement.executeQuery(sql);
		        
		        while(rs.next()) {
		        		String airlineId = rs.getString("AirlineId");
		        		int flightNo = rs.getInt("FlightNo");
		        		int Amount = rs.getInt("Amount");
		        		
		        		JSONObject o = new JSONObject();
		        		o.put("airline_id", airlineId);
		        		o.put("flightNumber", flightNo);
		        		o.put("Amount", Amount);
		        		flights.add(o);
		        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        json.put("Popular", flights);
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
