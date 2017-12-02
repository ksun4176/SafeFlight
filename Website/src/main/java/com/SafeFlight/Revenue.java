package com.SafeFlight;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

/**
 * Servlet implementation class Revenue
 */
public class Revenue extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Revenue() {
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
		try {
			conn = ConnectionUtils.getMyConnection();
			String airlineID = request.getParameter("airlineID");
			String flightNo = request.getParameter("flightNumber");
			String toCity = request.getParameter("toCity");
			String account_id = request.getParameter("account_id");
			if(flightNo != null && airlineID != null) {
				if(!flightNo.matches("[0-9]+")) {
					throw new IllegalArgumentException("Invalid FlightNo");
				}
				String query = "{CALL getRevenueF(?,?)}";
				CallableStatement stmt = conn.prepareCall(query);
				stmt.setString(1, airlineID);
				stmt.setString(2, flightNo);
				ResultSet rs = stmt.executeQuery();
				if(rs.next()) {
					float revenue = rs.getFloat("Revenue");
					json.put("Revenue", revenue);
				}
			} else if(toCity != null) {
				String query = "{CALL getRevenueDC(?)}";
				CallableStatement stmt = conn.prepareCall(query);
				stmt.setString(1, toCity);
				ResultSet rs = stmt.executeQuery();
				if(rs.next()) {
					float revenue = rs.getFloat("Revenue");
					json.put("Revenue", revenue);
				}
			} else if(account_id != null) {
				if(!account_id.matches("[0-9]+")) {
					throw new IllegalArgumentException("Invalid AccountID");
				}
				String query = "{CALL getRevenueC(?)}";
				CallableStatement stmt = conn.prepareCall(query);
				stmt.setString(1, account_id);
				ResultSet rs = stmt.executeQuery();
				if(rs.next()) {
					float revenue = rs.getFloat("Revenue");
					json.put("Revenue", revenue);
				}
			} else {
				throw new IllegalArgumentException("Expects an argument");
			}
		} catch(SQLException e) {
			e.printStackTrace();
			json.put("Revenue", -1);
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
			json.put("Revenue", -1);
		} catch(IllegalArgumentException e) {
			e.printStackTrace();
			json.put("Revenue", -1);
		}
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
