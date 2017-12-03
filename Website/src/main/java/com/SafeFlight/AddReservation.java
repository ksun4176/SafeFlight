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

import org.json.simple.JSONObject;

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
			String reservation_id = request.getParameter("reservation_id");
			String rep_id = request.getParameter("customer_rep_id");
			String airline_id = request.getParameter("airline_id");
			String flightNum = request.getParameter("flightNumber");
			String legNumber = request.getParameter("legNumber");
			String flightFare = request.getParameter("flightFare");
			String date = request.getParameter("date");
			
			if(reservation_id != null) {
				String query = "{CALL recordReservationOld(?, ?, ?, ?, ?, ?)}";
				CallableStatement stmt = conn.prepareCall(query);
				stmt.setString(1, reservation_id);
				stmt.setString(2, airline_id);
				stmt.setString(3, flightNum);
				stmt.setString(4, legNumber);
				stmt.setString(5, flightFare);
				stmt.setString(6, date);
				stmt.executeQuery();
			} else {
				String query = "{CALL recordReservationNew(?, ?, ?, ?, ?, ?, ?)}";
				CallableStatement stmt = conn.prepareCall(query);
				stmt.setString(1, account_id);
				stmt.setString(2, rep_id);
				stmt.setString(3, airline_id);
				stmt.setString(4, flightNum);
				stmt.setString(5, legNumber);
				stmt.setString(6, flightFare);
				stmt.setString(7, date);
				stmt.executeQuery();
			}
			json.put("ok", true);
		} catch(IllegalArgumentException e){
			e.printStackTrace();
			json.put("ok", false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.put("ok", false);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.put("ok", false);
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(json);
		response.getWriter().flush();
	}

}
