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
 * Servlet implementation class FlightSuggestion
 */
public class FlightSuggestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FlightSuggestion() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn;
		JSONObject obj = new JSONObject();
		JSONArray jArray = new JSONArray();
		
		try {
			conn = ConnectionUtils.getMyConnection();
			
			String accountId = request.getParameter("account_id");
			if (accountId != null ) {
				String query = "{CALL getFlightSuggestions(?)}";
				CallableStatement stmt = conn.prepareCall(query);
				stmt.setString(1, accountId);
				
				ResultSet rs = stmt.executeQuery();
				
				while(rs.next()) {
					JSONObject o = new JSONObject();
	        			String airline_id = rs.getString("AirlineId");
	        			String flightNo = rs.getString("FlightNo");
	        			
		        		o.put("airline_id", airline_id);
		        		o.put("flightNumber", flightNo);
		        		o.put("daysOfWeek", rs.getString("DaysOperating"));
		        		
		        		JSONObject legs = new JSONObject();
		        		query = "{CALL getLeg(?,?)}";
		        		stmt = conn.prepareCall(query);
		        		stmt.setString(1, airline_id);
		        		stmt.setString(2, flightNo);
		        		
		        		ResultSet rs2 = stmt.executeQuery();
		        		
		        		while(rs2.next()) {
		        			JSONObject legInfo = new JSONObject();
		        			legInfo.put("depAirportID", rs2.getString("DepAirportID"));
		        			legInfo.put("arrAirportID", rs2.getString("ArrAirportID"));
		        			legInfo.put("depTime", rs2.getString("DepTime"));
		        			legInfo.put("arrTime", rs2.getString("ArrTime"));
		        			legs.put(rs2.getString("LegNo"), legInfo);
		        		}
		        		o.put("legs", legs);
		        		
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
		        				 if(temp.equals("hiddenfare")) {
		        					 o.put("hasAuction", true);
		        					 query = "{CALL getAuction(?,?)}";
		        					 stmt = conn.prepareCall(query);
		        					 stmt.setString(1, airline_id);
		        					 stmt.setString(2, flightNo);
		        					 ResultSet rs3 = stmt.executeQuery();
		        					 JSONArray bidArray = new JSONArray();
		        					 while(rs3.next()) {
		        						 JSONObject bidInfo = new JSONObject();
		        						 bidInfo.put("account_id", rs3.getString("AccountNo"));
		        						 bidInfo.put("bid",rs3.getString("NYOP"));
		        						 bidArray.add(bidInfo);
		        					 }
		        					 o.put("bidHistory", bidArray);
		        					 } else {
		        						 fares.put(temp, fareInfo);
		        						 fareInfo = new JSONObject();
		        					 }
		        			}
		        			fareInfo.put(rs2.getString("Class"), rs2.getString("Fare"));
		        			temp = fare_type;
		        		}
		        		if(temp != null) {
		        			fares.put(temp, fareInfo);
		        		}
		        		o.put("prices", fares);
		        		if(o.get("hasAuction") == null) {
		        			 o.put("hasAuction", false);
		        		}
		        		jArray.add(o);

				}
				
				obj.put("flights", jArray);
				
			}
			else {
				obj.put("error", "account_id not found");
			}
			
			ConnectionUtils.close(conn);
		} catch (ClassNotFoundException e) {

			obj.put("error", e.toString());
			e.printStackTrace();
		} catch (SQLException e) {

			obj.put("error", e.toString());
			e.printStackTrace();
		}
		
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
