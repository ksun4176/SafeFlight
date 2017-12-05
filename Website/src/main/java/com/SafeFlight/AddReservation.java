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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Servlet implementation class AddReservation
 */
public class AddReservation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddReservation() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Connection conn;
		JSONObject json = new JSONObject();
		try {
			conn = ConnectionUtils.getMyConnection();
			String account_id = request.getParameter("account_id");
			String rep_id = request.getParameter("customer_rep_id");
			String reservation_id = request.getParameter("reservation_id");
			String airline_id = request.getParameter("airline_id");
			String flightNum = request.getParameter("flightNumber");
			String tempLegNum = request.getParameter("legNumber");
			String flightFare = request.getParameter("flightFare");
			String date = request.getParameter("date");
			String people = request.getParameter("persons");
			String flightClass = request.getParameter("flightClass");
			String[] legNums = tempLegNum.split(" ");
			
			for(String l : legNums) {
				if(reservation_id != null) {
					String query = "{CALL recordReservationOld(?, ?, ?, ?, ?, ?)}";
					CallableStatement stmt = conn.prepareCall(query);
					stmt.setString(1, reservation_id);
					stmt.setString(2, airline_id);
					stmt.setString(3, flightNum);
					stmt.setString(4, l);
					stmt.setString(5, flightFare);
					stmt.setString(6, date);
					stmt.executeQuery();
				} else {
					// Get rep id from customer info
					if (rep_id == null) {
						String query = "{CALL getRepByCustId(?)}";
						CallableStatement stmt = conn.prepareCall(query);
						stmt.setString(1, account_id);
						
						ResultSet rs = stmt.executeQuery();
						if(!rs.next()) {
							throw new SQLException("Rep Not Found");
						}
						rep_id = rs.getString("RepId");
					}
					
					// if still not found, assign employee with least reservation
					if(rep_id == null) {
						String query = "{CALL getOpenEmployee()}";
						CallableStatement stmt = conn.prepareCall(query);
						ResultSet rs = stmt.executeQuery();
						if(!rs.next()) {
							throw new SQLException("Rep Not Found");
						}
						rep_id = rs.getString("Id");
					}
					String query = "{CALL recordReservationNew(?, ?, ?, ?, ?, ?, ?)}";
					CallableStatement stmt = conn.prepareCall(query);
					stmt.setString(1, account_id);
					stmt.setString(2, rep_id);
					stmt.setString(3, airline_id);
					stmt.setString(4, flightNum);
					stmt.setString(5, l);
					stmt.setString(6, flightFare);
					stmt.setString(7, date);
					ResultSet rs = stmt.executeQuery();
					if(!rs.next()) {
						throw new SQLException("Reservation Not Made");
					}
					reservation_id = rs.getString("ResNo");
				}
			}
			if(people != null) {
				JSONParser parser = new JSONParser();
				JSONArray peopleArray = (JSONArray) parser.parse(people.substring(11,people.length()-1));
				for(Object temp : peopleArray) {
					JSONObject o = (JSONObject) temp;
					Long tempId = (Long) o.get("account_id");
					System.out.println(tempId);
					String query;
					CallableStatement stmt;
					ResultSet rs;
					if(tempId == null) {
						String fname = (String) o.get("first_name");
						String lname = (String) o.get("last_name");
						String address = (String) o.get("address");
						String city = (String) o.get("city");
						String state = (String) o.get("state");
						String zip = (String) o.get("zip");
						query = "{CALL addPerson(?,?,?,?,?,?)}";
						stmt = conn.prepareCall(query);
						stmt.setString(1, fname);
						stmt.setString(2, lname);
						stmt.setString(3, address);
						stmt.setString(4, city);
						stmt.setString(5, state);
						stmt.setString(6, zip);
						rs = stmt.executeQuery();
						rs.next();
						tempId = rs.getLong("Id");
					}
					query = "{CALL addPassenger(?,?)}";
					stmt = conn.prepareCall(query);
					stmt.setLong(1, tempId);
					stmt.setString(2, account_id);
					stmt.executeQuery();
					query = "{CALL addRPassenger(?,?,?,?,'')}";
					stmt = conn.prepareCall(query);
					stmt.setString(1, reservation_id);
					stmt.setLong(2, tempId);
					stmt.setString(3, account_id);
					stmt.setString(4, flightClass);
					stmt.executeQuery();
				}
			}
			json.put("reservation_id", Integer.parseInt(reservation_id));
			ConnectionUtils.close(conn);
		} catch(IllegalArgumentException e){
			e.printStackTrace();
			json.put("reservation_id", -1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.put("reservation_id", -1);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.put("reservation_id", -1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.put("reservation_id", -1);
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(json);
		response.getWriter().flush();
	}

}
