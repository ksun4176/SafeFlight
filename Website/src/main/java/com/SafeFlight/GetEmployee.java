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
 * Servlet implementation class GetEmployee
 */
public class GetEmployee extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetEmployee() {
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
		JSONArray jArray = new JSONArray();
		try {
			conn = ConnectionUtils.getMyConnection();
			String query = "{CALL getAllEmployees()}";
			CallableStatement stmt = conn.prepareCall(query);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				JSONObject o = new JSONObject();
				int id = rs.getInt("Id");
				String ssn = rs.getString("SSN");
				String start_date = rs.getString("StartDate");
				float hourly_rate = rs.getFloat("HourlyRate");
				String first_name = rs.getString("FirstName");
				String last_name = rs.getString("LastName");
				String address = rs.getString("Address");
				String city = rs.getString("City");
				String state = rs.getString("State");
				String zipcode = rs.getString("ZipCode");
				String username = rs.getString("Username");
				String role = rs.getString("Role");
				
				o.put("id", id);
				o.put("ssn", ssn);
				o.put("start_date", start_date);
				o.put("hourly_rate", hourly_rate);
				o.put("first_name", first_name);
				o.put("last_name", last_name);
				o.put("address", address);
				o.put("city", city);
				o.put("state", state);
				o.put("zipcode", zipcode);
				o.put("username", username);
				if(role.equals("customer")) {
					o.put("account_type", 0);
				} else if(role.equals("employee")) {
					o.put("account_type", 1);
				} else if(role.equals("manager")) {
					o.put("account_type", 2);
				}
				jArray.add(o);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		json.put("employees", jArray);
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
