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
public class SignupEmployee extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignupEmployee() {
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
		JSONObject employee = new JSONObject();
		try {
			conn = ConnectionUtils.getMyConnection();
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String address = request.getParameter("address");
			String ssn = request.getParameter("ssn");
			String hourlyRate = request.getParameter("hourlyRate");
			//get StartDate
			
			String city = request.getParameter("city");
			String state = request.getParameter("state");
			String zip = request.getParameter("zip");

			//assume isManager is always 0
			if (username == null || password == null || firstName == null || lastName == null 
			|| address == null || ssn == null || hourlyRate == null || 
			city == null || state == null || zip == null) {
				throw new IllegalArgumentException("Fields are not filled");
				
			}

			if(zip != null && zip != "" && !zip.matches("[0-9]{5}")) {
					throw new IllegalArgumentException("Invalid ZipCode");
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
			String id = rs.getString("id");
			
			String query2 = "{CALL addEmployee(?, ?, ?, ?, ?)}";
			CallableStatement stmt2 = conn.prepareCall(query2);
			stmt2.setString(1, id);
			stmt2.setString(2, ssn);
			stmt2.setString(3, "0");
			stmt2.setDate(4, new java.sql.Date(new java.util.Date().getTime()));
			if (hourlyRate.equals("")) {
				stmt2.setString(5, "0.00");
			}
			else {
				stmt2.setString(5, hourlyRate);
			}
			ResultSet rs2 = stmt2.executeQuery();
			employee.put("employee_id", id);
			
			String query3 = "{CALL addLogin(?, ?, ?, ?)}";
			CallableStatement stmt3 = conn.prepareCall(query3);
			stmt3.setString(1, id);
			stmt3.setString(2, username);
			stmt3.setString(3, password);
			stmt3.setString(4, "employee"); 
			stmt3.executeQuery();
			
			ConnectionUtils.close(conn);
			} catch (ClassNotFoundException e) {
				// onError set return id to -1
				e.printStackTrace();
				employee.put("account_id", -1);
			} catch (SQLException e) {
				// onError set return id to -1
				e.printStackTrace();
				employee.put("account_id", -1);
			} catch (IllegalArgumentException e) {
				//onError set return id to -1
				e.printStackTrace();
				employee.put("account_id", -1);
			}
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(employee);
			response.getWriter().flush();
	}
}
	
	

