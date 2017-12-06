package com.SafeFlight;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

/**
 * Servlet implementation class Signup
 */
public class Signup extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Signup() {
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
		
		Connection conn;
		JSONObject o = new JSONObject();
		try {
			conn = ConnectionUtils.getMyConnection();
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String address = request.getParameter("address");
			String city = request.getParameter("city");
			String state = request.getParameter("state");
			String zip = request.getParameter("zip");
			String email = request.getParameter("email");
			String ccnum = request.getParameter("creditCardNo");
			
			String rep_id = request.getParameter("customer_rep_id");
			
			if(zip != null && !zip.matches("[0-9]{5}")) {
				throw new IllegalArgumentException("Invalid ZipCode");
			}
			if(ccnnum != null && !ccnum.matches("[0-9]{16}")) {
				throw new IllegalArgumentException("Invalid Credit Card Number");
			}
			
			if (rep_id == null) {

				String query = "{CALL getOpenEmployee()}";
				CallableStatement stmt = conn.prepareCall(query);
				ResultSet rs = stmt.executeQuery();
				if(!rs.next()) {
					throw new SQLException("Rep Not Found");
				}
				rep_id = rs.getString("Id");
				

			}
			
			String query = "{CALL addPerson(?, ?, ?, ?, ?, ?)}";
			CallableStatement stmt = conn.prepareCall(query);
			stmt.setString(1, firstName);
			stmt.setString(2, lastName);
			stmt.setString(3, address);
			stmt.setString(4, city);
			stmt.setString(5, state);
			stmt.setString(6, zip);
			ResultSet rs = stmt.executeQuery();
			
			rs.next();
			int id = rs.getInt("id");
			
			query = "{CALL addLogin(?, ?, ?, ?)}";
			stmt = conn.prepareCall(query);
			stmt.setInt(1, id);
			stmt.setString(2, username);
			stmt.setString(3, password);
			// Assuming this route only signs up customer
			stmt.setString(4, "customer"); 
			stmt.executeQuery();
			
			query = "{CALL addCustomer(?, ?, ?, ?, ?)}";
			stmt = conn.prepareCall(query);
			stmt.setInt(1, id);
			stmt.setString(2, ccnum);
			stmt.setString(3, email);
			stmt.setDate(4, new java.sql.Date(new java.util.Date().getTime()));
			stmt.setString(5, rep_id);
			stmt.executeQuery();
			
			o.put("account_id", id);
			
			ConnectionUtils.close(conn);
		} catch (ClassNotFoundException e) {
			// onError set return id to -1
			e.printStackTrace();
			o.put("account_id", -1);
		} catch (SQLException e) {
			// onError set return id to -1
			e.printStackTrace();
			o.put("account_id", -1);
		} catch (IllegalArgumentException e) {
			//onError set return id to -1
			e.printStackTrace();
			o.put("account_id", -1);
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(o);
		response.getWriter().flush();
	}

}
