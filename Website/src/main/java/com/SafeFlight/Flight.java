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
import java.util.HashMap;
import java.util.Set;
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
		Connection conn;
		JSONObject obj = new JSONObject();
		JSONArray jArray = new JSONArray();
		
		try {
			conn = ConnectionUtils.getMyConnection();
			String fromAirport = request.getParameter("fromAirportID");
			String toAirport = request.getParameter("toAirportID");
			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");

			if(fromAirport == null || toAirport == null) {
				throw new IllegalArgumentException("Missing parameters");
			}
			
			//Get all flights and check if days matched DaysOperating
			String query = "{CALL getFlights()}";
			CallableStatement stmt = conn.prepareCall(query);
			ResultSet rs = stmt.executeQuery();
			
			//airlineID+flightNo : days+numSeats
			HashMap<String,String> flights = new HashMap<String,String>();
			
			while(rs.next()) {
				String airlineID = rs.getString("AirlineID");
				String flightNo = rs.getString("FlightNo");
				String days = rs.getString("DaysOperating");
				int numSeats = rs.getInt("NoOfSeats");
				query = "{CALL seatsLeft(?,?,?)}";
				stmt = conn.prepareCall(query);
				stmt.setString(1, airlineID);
				stmt.setString(2, flightNo);
				stmt.setInt(3, numSeats);
				ResultSet rs2 = stmt.executeQuery();
				rs2.next();
				if(rs2.getInt("SeatsLeft") != 0) {
					if (fromDate != null) {
						int fromDays = DBUtils.getDaysOfWeek(fromDate);
						if (days.charAt(fromDays) == '1') {
							flights.put(airlineID.concat(flightNo),days);
						}
					}
					else {
						flights.put(airlineID.concat(flightNo),days);
					}
				}
			}
			
			query = "{CALL getLegs()}";
			stmt = conn.prepareCall(query);
			rs = stmt.executeQuery();
			
			//airlineID+flightNo : legNo : {depAirport,arrAirport, depTime, arrTime}
			HashMap<String,HashMap<Integer,ArrayList<String>>> flights2 = new HashMap<String,HashMap<Integer,ArrayList<String>>>();
			
			String temp = null;
			int tempNo = 0;
			boolean done = false;
			while(rs.next()) {
//				System.out.println("new loop");
//				for(String k : flights2.keySet()) {
//					System.out.print(k+"\t");
//				}
//				System.out.println("");
				String airlineID = rs.getString("AirlineID");
				String flightNo = rs.getString("FlightNo");
				int legNo = rs.getInt("LegNo");
				String depAirport = rs.getString("DepAirportID");
				String arrAirport = rs.getString("ArrAirportID");
				String depTime = rs.getString("DepTime");
				String arrTime = rs.getString("ArrTime");
				if(temp != null && legNo == 1) {
					if(flights2.containsKey(temp)) {
						boolean start = false;
						boolean end = false;
						if(toDate != null && flights2.get(temp).get(tempNo).get(3).substring(0,8).compareTo(toDate) > 0) {
							flights2.remove(temp);
						} else {
							for(ArrayList<String> a : flights2.get(temp).values()) {
								if(a.get(0).equals(fromAirport)) {
									start = true;
								}
								if(start == true && a.get(1).equals(toAirport)) {
									end = true;
									break;
								}
							}
							if(start == false || end == false) {
								flights2.remove(temp);
							}
						}
					}
				}
				if(flights.containsKey(airlineID.concat(flightNo))) {
					temp = airlineID.concat(flightNo);
					if(depAirport.equals(fromAirport)) {
						if(fromDate != null && legNo == 1) {
							if(depTime.compareTo(fromDate) > 0) {
								if(!flights2.containsKey(temp)) {
									flights2.put(temp, new HashMap<Integer,ArrayList<String>>());
								}
								flights2.get(temp).put(legNo, new ArrayList<String>());
								flights2.get(temp).get(legNo).add(depAirport);
								flights2.get(temp).get(legNo).add(arrAirport);
								flights2.get(temp).get(legNo).add(depTime);
								flights2.get(temp).get(legNo).add(arrTime);
								tempNo = legNo;
								if(arrAirport.equals(toAirport)) {
									done = true;
								} else {
									done = false;
								}
							}
						} else {
							if(!flights2.containsKey(temp)) {
								flights2.put(temp, new HashMap<Integer,ArrayList<String>>());
							}
							flights2.get(temp).put(legNo, new ArrayList<String>());
							flights2.get(temp).get(legNo).add(depAirport);
							flights2.get(temp).get(legNo).add(arrAirport);
							flights2.get(temp).get(legNo).add(depTime);
							flights2.get(temp).get(legNo).add(arrTime);
							tempNo = legNo;
							if(arrAirport.equals(toAirport)) {
								done = true;
							} else {
								done = false;
							}
						}
					} else if(arrAirport.equals(toAirport)) {
						if(!flights2.containsKey(temp)) {
							flights2.put(temp, new HashMap<Integer,ArrayList<String>>());
						}
						flights2.get(temp).put(legNo, new ArrayList<String>());
						flights2.get(temp).get(legNo).add(depAirport);
						flights2.get(temp).get(legNo).add(arrAirport);
						flights2.get(temp).get(legNo).add(depTime);
						flights2.get(temp).get(legNo).add(arrTime);	
						tempNo = legNo;
						done = true;
					} else if(legNo > 1 && flights2.containsKey(temp) && !done) {
						flights2.get(temp).put(legNo, new ArrayList<String>());
						flights2.get(temp).get(legNo).add(depAirport);
						flights2.get(temp).get(legNo).add(arrAirport);
						flights2.get(temp).get(legNo).add(depTime);
						flights2.get(temp).get(legNo).add(arrTime);
						tempNo = legNo;
					}
				}
			}
			if(temp != null) {
				if(flights2.containsKey(temp)) {
					boolean start = false;
					boolean end = false;
					if(toDate != null && flights2.get(temp).get(tempNo).get(3).substring(0,8).compareTo(toDate) > 0) {
						flights2.remove(temp);
					} else {
						for(ArrayList<String> a : flights2.get(temp).values()) {
							if(a.get(0).equals(fromAirport)) {
								start = true;
							}
							if(start == true && a.get(1).equals(toAirport)) {
								end = true;
								break;
							}
						}
						if(start == false || end == false) {
							flights2.remove(temp);
						}
					}
				}
			}

			//airlineID+flightNo : fareType : {economy$,first$}
			HashMap<String,HashMap<String,ArrayList<Float>>> fares = new HashMap<String,HashMap<String,ArrayList<Float>>>(); 
			query = "{CALL getFares()}";
			stmt = conn.prepareCall(query);
			rs = stmt.executeQuery();
			while(rs.next()) {
				String tempName = rs.getString("AirlineID").concat(rs.getString("FlightNo"));
				if(flights2.containsKey(tempName)) {
					String fareType = rs.getString("FareType");
					if(!fares.containsKey(tempName)) {
						fares.put(tempName, new HashMap<String,ArrayList<Float>>());
					}
					if(!fares.get(tempName).containsKey(fareType)) {
						fares.get(tempName).put(fareType, new ArrayList<Float>());
					}
					fares.get(tempName).get(fareType).add(rs.getFloat("Fare"));
				}
			}
			
			
			Set<String> keys = flights2.keySet();
			for(String key : keys) {
				//add info from flights hashmap
				JSONObject flight = new JSONObject();
				flight.put("airline_id",key.substring(0, 2));
				flight.put("flightNumber", key.substring(2));
        			flight.put("daysOfWeek", flights.get(key));
        			
        			//add info from flights2 hashmap
        			JSONObject legs = new JSONObject();
        			HashMap<Integer,ArrayList<String>> temp2 = flights2.get(key);
        			for(int key2 : temp2.keySet()) {
        				JSONObject legsInfo = new JSONObject();
        				legsInfo.put("depAirportID",temp2.get(key2).get(0));
        				legsInfo.put("arrAirportID", temp2.get(key2).get(1));
        				legsInfo.put("depTime", temp2.get(key2).get(2));
        				legsInfo.put("arrTime", temp2.get(key2).get(3));
        				legs.put(key2, legsInfo);
        			}
        			flight.put("legs", legs);
        			
        			//add info from fares hashmap
    				JSONObject fare = new JSONObject();
    				HashMap<String,ArrayList<Float>> temp3 = fares.get(key);
    				for(String key2 : temp3.keySet()) {
    					JSONObject faresInfo = new JSONObject();
    					faresInfo.put("economy", temp3.get(key2).get(0));
    					faresInfo.put("first", temp3.get(key2).get(1));
    					fare.put(key2, faresInfo);
    				}
    				flight.put("prices", fare);
    				jArray.add(flight);
			}
	        
	        obj.put("flights", jArray);
	        
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			obj.put("flights", null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			obj.put("flights", null);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			obj.put("flights", null);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			obj.put("flights", null);
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
