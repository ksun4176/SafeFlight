package com.SafeFlight;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
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
import java.util.Collections;
import java.util.Date;

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
			String all = request.getParameter("all");
			String from = request.getParameter("fromAirport");
			String to = request.getParameter("toAirport");
			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			String tempSeats = request.getParameter("seats");
			boolean roundtrip = Boolean.parseBoolean(request.getParameter("roundtrip"));
			String backBeforeDate = request.getParameter("backBeforeDate");
			
			if(all != null && Boolean.parseBoolean(all)) {
				String query = "{CALL getFlights()}";
				CallableStatement stmt = conn.prepareCall(query);
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
		        		JSONObject o = new JSONObject();
		        		String airline_id = rs.getString("AirlineId");
		        		String flight_no = rs.getString("FlightNo");
		        		o.put("airline_id", airline_id);
		        		o.put("flightNumber", flight_no);
		        		o.put("daysOfWeek", rs.getString("DaysOperating"));
		        		
	        			JSONObject legs = new JSONObject();
		        		query = "{CALL getLeg(?,?)}";
		        		stmt = conn.prepareCall(query);
		        		stmt.setString(1, airline_id);
		        		stmt.setString(2, flight_no);
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
		        		stmt.setString(2, flight_no);
		        		rs2 = stmt.executeQuery();
		        		while(rs2.next()) {
		        			String fare_type = rs2.getString("FareType");
		        			if(temp != null && !temp.equals(fare_type)) {
		        				if(temp.equals("hiddenfare")) {
		        					o.put("hasAuction", true);
		        					query = "{CALL getAuction(?,?)}";
		        					stmt = conn.prepareCall(query);
		        					stmt.setString(1, airline_id);
		        					stmt.setString(2, flight_no);
		        					ResultSet rs3 = stmt.executeQuery();
		        					JSONArray bidArray = new JSONArray();
		        					JSONObject bidInfo = new JSONObject();
		        					while(rs3.next()) {
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
		        		if(o.get("hasAuction") == null) {
		        			o.put("hasAuction", false);
		        		}
		        		o.put("prices", fares);
		        		jArray.add(o);
		        }
	
		        obj.put("flights", jArray);
				ConnectionUtils.close(conn);
			} else {
				if(from == null || to == null) {
					throw new IllegalArgumentException("Missing parameters");
				}
				int seats = Integer.parseInt(tempSeats);
				//Get all airports at from, to
				String[] tempLoc = from.split(",");
				String query = "{CALL getAirportAtCity(?,?)}";
				CallableStatement stmt = conn.prepareCall(query);
				stmt.setString(1, tempLoc[0]);
				stmt.setString(2, tempLoc[1]);
				ResultSet rs = stmt.executeQuery();
				ArrayList<String> fromAirports = new ArrayList<String>();
				while(rs.next()) {
					fromAirports.add(rs.getString("Id"));
				}
				tempLoc = to.split(",");
				query = "{CALL getAirportAtCity(?, ?)}";
				stmt = conn.prepareCall(query);
				stmt.setString(1, tempLoc[0]);
				stmt.setString(2, tempLoc[1]);
				rs = stmt.executeQuery();
				ArrayList<String> toAirports = new ArrayList<String>();
				while(rs.next()) {
					toAirports.add(rs.getString("Id"));
				}
				
				//Get all flights and check if days matched DaysOperating
				query = "{CALL getFlights()}";
				stmt = conn.prepareCall(query);
				rs = stmt.executeQuery();
				
				//airlineID+flightNo : days+numSeats
				HashMap<String,String> flights = new HashMap<String,String>();
				int fromDays = 7;
				if(fromDate != null) {
					fromDays = DBUtils.getDaysOfWeek(fromDate);
				}
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
					if(rs2.getInt("SeatsLeft") >= seats) {
						if (fromDays != 7 ) {
							DateFormat df = new SimpleDateFormat("YYYY-MM-DD");
							Date fDate = df.parse(fromDate);
							Date tDate;
							if(roundtrip && backBeforeDate != null) {
								tDate = df.parse(backBeforeDate);
							} else if(toDate != null) {
								tDate = df.parse(toDate);
							} else {
								tDate = fDate;
							}
							long diff = (tDate.getTime() - fDate.getTime())/(24*60*60*1000);
							for(int i = 0;i <= diff;i++) {
								if(days.charAt((fromDays+i)%7) == '1') {
									flights.put(airlineID.concat(flightNo),days);
									break;
								}
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
							if(toDate != null && flights2.get(temp).get(tempNo).get(3).substring(0,10).compareTo(toDate) > 0) {
								flights2.remove(temp);
							} else {
								for(ArrayList<String> a : flights2.get(temp).values()) {
									if(fromAirports.contains(a.get(0))) {
										start = true;
									}
									if(start == true && toAirports.contains(a.get(1))) {
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
						if(fromAirports.contains(depAirport)) {
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
									if(toAirports.contains(arrAirport)) {
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
								if(toAirports.contains(arrAirport)) {
									done = true;
								} else {
									done = false;
								}
							}
						} else if(toAirports.contains(arrAirport)) {
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
								if(fromAirports.contains(a.get(0))) {
									start = true;
								}
								if(start == true && toAirports.contains(a.get(1))) {
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
	
				HashMap<String,HashMap<Integer,ArrayList<String>>> flights3 = new HashMap<String,HashMap<Integer,ArrayList<String>>>();
				if(roundtrip) {
					rs.beforeFirst();
					temp = null;
					tempNo = 0;
					done = false;
					while(rs.next()) {
						String airlineID = rs.getString("AirlineID");
						String flightNo = rs.getString("FlightNo");
						int legNo = rs.getInt("LegNo");
						String depAirport = rs.getString("DepAirportID");
						String arrAirport = rs.getString("ArrAirportID");
						String depTime = rs.getString("DepTime");
						String arrTime = rs.getString("ArrTime");
						if(temp != null && legNo == 1) {
							if(flights3.containsKey(temp)) {
								boolean start = false;
								boolean end = false;
								if(backBeforeDate != null && flights3.get(temp).get(tempNo).get(3).substring(0,10).compareTo(backBeforeDate) > 0) {
									flights3.remove(temp);
								} else {
									for(ArrayList<String> a : flights3.get(temp).values()) {
										if(toAirports.contains(a.get(0))) {
											start = true;
										}
										if(start == true && fromAirports.contains(a.get(1))) {
											end = true;
											break;
										}
									}
									if(start == false || end == false) {
										flights3.remove(temp);
									}
								}
							}
						}
						if(flights.containsKey(airlineID.concat(flightNo))) {
							temp = airlineID.concat(flightNo);
							if(toAirports.contains(depAirport)) {
								if(!flights3.containsKey(temp)) {
									flights3.put(temp, new HashMap<Integer,ArrayList<String>>());
								}
								flights3.get(temp).put(legNo, new ArrayList<String>());
								flights3.get(temp).get(legNo).add(depAirport);
								flights3.get(temp).get(legNo).add(arrAirport);
								flights3.get(temp).get(legNo).add(depTime);
								flights3.get(temp).get(legNo).add(arrTime);
								tempNo = legNo;
								if(fromAirports.contains(arrAirport)) {
									done = true;
								} else {
									done = false;
								}
							} else if(fromAirports.contains(arrAirport)) {
								if(!flights3.containsKey(temp)) {
									flights3.put(temp, new HashMap<Integer,ArrayList<String>>());
								}
								flights3.get(temp).put(legNo, new ArrayList<String>());
								flights3.get(temp).get(legNo).add(depAirport);
								flights3.get(temp).get(legNo).add(arrAirport);
								flights3.get(temp).get(legNo).add(depTime);
								flights3.get(temp).get(legNo).add(arrTime);	
								tempNo = legNo;
								done = true;
							} else if(legNo > 1 && flights3.containsKey(temp) && !done) {
								flights3.get(temp).put(legNo, new ArrayList<String>());
								flights3.get(temp).get(legNo).add(depAirport);
								flights3.get(temp).get(legNo).add(arrAirport);
								flights3.get(temp).get(legNo).add(depTime);
								flights3.get(temp).get(legNo).add(arrTime);
								tempNo = legNo;
							}
						}
					}
					if(temp != null) {
						if(flights3.containsKey(temp)) {
							boolean start = false;
							boolean end = false;
							if(backBeforeDate != null && flights3.get(temp).get(tempNo).get(3).substring(0,8).compareTo(backBeforeDate) > 0) {
								flights3.remove(temp);
							} else {
								for(ArrayList<String> a : flights3.get(temp).values()) {
									if(toAirports.contains(a.get(0))) {
										start = true;
									}
									if(start == true && fromAirports.contains(a.get(1))) {
										end = true;
										break;
									}
								}
								if(start == false || end == false) {
									flights3.remove(temp);
								}
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
					} else if(flights3.containsKey(tempName)) {
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
				
	
				for(String key : flights2.keySet()) {
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
	    					if(key2.equals("hiddenfare")) {
		        				flight.put("hasAuction", true);
		        				query = "{CALL getAuction(?,?)}";
	        					stmt = conn.prepareCall(query);
	        					stmt.setString(1, key.substring(0,2));
	        					stmt.setString(2, key.substring(2));
	        					ResultSet rs3 = stmt.executeQuery();
	        					JSONArray bidArray = new JSONArray();
	        					JSONObject bidInfo = new JSONObject();
	        					while(rs3.next()) {
	        						bidInfo.put("account_id", rs3.getString("AccountNo"));
	        						bidInfo.put("bid",rs3.getString("NYOP"));
	        						bidArray.add(bidInfo);
	        					}
	        					flight.put("bidHistory", bidArray);
	    					}
	    					JSONObject faresInfo = new JSONObject();
	    					faresInfo.put("economy", temp3.get(key2).get(0));
	    					faresInfo.put("first", temp3.get(key2).get(1));
	    					fare.put(key2, faresInfo);
	    				}
	    				flight.put("prices", fare);
	    				if(flight.get("hasAuction") == null) {
	    					flight.put("hasAuction", false);
	    				}
	    				if(roundtrip) {
		    				if(flight.get("hasAuction") != null) {
		    					flight.put("hasAuction", false);
		    				}
	    					for(String key2 : flights3.keySet()) {
		        				HashMap<Integer,ArrayList<String>> temp4 = flights3.get(key2);
	    						if(temp2.get(Collections.max(temp2.keySet())).get(3).compareTo(temp4.get(Collections.min(temp4.keySet())).get(2)) < 0) {
	    							flight.put("airline_id2",key2.substring(0,2));
	    							flight.put("flightNumber2", key2.substring(2));
	    							flight.put("daysOfWeek2", flights.get(key));
	    							JSONObject legs2 = new JSONObject();
	    		        				for(int key3 : temp4.keySet()) {
		    		        				JSONObject legsInfo = new JSONObject();
		    		        				legsInfo.put("depAirportID",temp4.get(key3).get(0));
		    		        				legsInfo.put("arrAirportID", temp4.get(key3).get(1));
		    		        				legsInfo.put("depTime", temp4.get(key3).get(2));
		    		        				legsInfo.put("arrTime", temp4.get(key3).get(3));
		    		        				legs2.put(key3, legsInfo);
		    		        			}
		    		        			flight.put("legs2", legs2);
		    		        			
		    		        			//add info from fares hashmap
		    		    				JSONObject fare2 = new JSONObject();
		    		    				HashMap<String,ArrayList<Float>> temp5 = fares.get(key);
		    		    				for(String key3 : temp3.keySet()) {
		    		    					JSONObject faresInfo = new JSONObject();
		    		    					faresInfo.put("economy", temp5.get(key3).get(0));
		    		    					faresInfo.put("first", temp5.get(key3).get(1));
		    		    					fare.put(key3, faresInfo);
		    		    				}
		    		    				flight.put("prices2", fare);
		    		    				jArray.add(flight);
	    						}
	    					}
	    				} else {
	    					jArray.add(flight);
	    				}
				}
		        
		        obj.put("flights", jArray);
				ConnectionUtils.close(conn);
			}
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
