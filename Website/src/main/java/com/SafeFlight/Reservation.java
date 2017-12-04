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

import java.util.Date;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Servlet implementation class Reservation
 */
public class Reservation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Reservation() {
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
		JSONArray reservations = new JSONArray();
		try {
			conn = ConnectionUtils.getMyConnection();
			String airlineId = request.getParameter("airline_id");
			String flightNumber  = request.getParameter("flightNumber");
			String account_id = request.getParameter("account_id");
			String reservation_id = request.getParameter("reservation_id");
			if(flightNumber != null && airlineId != null) {
				String query = "{CALL getReservationF(?, ?)}";
				CallableStatement stmt = conn.prepareCall(query);
				stmt.setString(1, airlineId);
				stmt.setString(2, flightNumber);
				
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					JSONObject o = new JSONObject();
		        		int ResNo = rs.getInt("ResNo");
		        		Date ResDate = rs.getDate("ResDate");
		        		double BookingFee = rs.getDouble("BookingFee");
		        		double TotalFare = rs.getDouble("TotalFare");
		        		int RepID = rs.getInt("RepID");
		        		int AccountNo = rs.getInt("AccountNo");

		        		o.put("reservation_id", ResNo);
		        		o.put("ResDate", ResDate.toString());
		        		o.put("bookingFee", BookingFee);
		        		o.put("totalFare", TotalFare);
		        		o.put("customer_rep_id", RepID);
		        		o.put("account_id", AccountNo);
		        		reservations.add(o);
				}
			} else if (account_id != null) {
				String query = "{CALL getReservationC(?)}";
				CallableStatement stmt = conn.prepareCall(query);
				stmt.setString(1, account_id);
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					JSONObject o = new JSONObject();
		        		int ResNo = rs.getInt("ResNo");
		        		Date ResDate = rs.getDate("ResDate");
		        		double BookingFee = rs.getDouble("BookingFee");
		        		double TotalFare = rs.getDouble("TotalFare");
		        		int RepID = rs.getInt("RepID");
		        		int AccountNo = rs.getInt("AccountNo");
		        		o.put("reservation_id", ResNo);
		        		o.put("ResDate", ResDate.toString());
		        		o.put("bookingFee", BookingFee);
		        		o.put("totalFare", TotalFare);
		        		o.put("customer_rep_id", RepID);
		        		o.put("account_id", AccountNo);
		        		reservations.add(o);		
				}
			} else if(reservation_id != null) {
				//Reservation_id
				String query = "{CALL getReservationR(?)}";
				CallableStatement stmt = conn.prepareCall(query);
				stmt.setString(1, reservation_id);
				ResultSet rs = stmt.executeQuery();
				JSONObject o = new JSONObject();
				if(rs.next()) {
			     	int ResNo = rs.getInt("ResNo");
			     	Date ResDate = rs.getDate("ResDate");
			    		double BookingFee = rs.getDouble("BookingFee");
			        	double TotalFare = rs.getDouble("TotalFare");
			    		int RepID = rs.getInt("RepID");
			    		int AccountNo = rs.getInt("AccountNo");
	
			    		o.put("reservation_id", ResNo);
			        	o.put("ResDate", ResDate.toString());
			    		o.put("bookingFee", BookingFee);
			    		o.put("totalFare", TotalFare);
			    		o.put("customer_rep_id", RepID);
		     		o.put("account_id", AccountNo);
		     		reservations.add(o);
				}
			}else {
				throw new IllegalArgumentException("Missing Parameters");
			}
			ConnectionUtils.close(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		json.put("reservations", reservations);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(json);
		response.getWriter().flush();
	}
}

