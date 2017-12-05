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
 * Servlet implementation class FlightCustomers
 */
public class FlightCustomers extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FlightCustomers() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Servlet is called.");
		JSONObject customers = new JSONObject();
		JSONArray customer = new JSONArray();
		
		try {
			Connection conn = ConnectionUtils.getMyConnection();
			String airline_id = request.getParameter("airlineId");
			String flightNumber = request.getParameter("flightNumber");
			 System.out.println("Get connection " + conn);
		       
		     // Testing SQL
			String query = "{CALL getReservationF(?, ?)}";
			CallableStatement stmt = conn.prepareCall(query);
			stmt.setString(1, airline_id);
			stmt.setString(2, flightNumber);
			ResultSet rs = stmt.executeQuery();
		    while(rs.next()) {
		    		JSONObject person = new JSONObject();
		    		int account_id = rs.getInt("AccountNo");
				String query2 = "{CALL getCustomer(?)}";
				Connection conn2 = ConnectionUtils.getMyConnection();
				CallableStatement stmt2 = conn2.prepareCall(query2);
				stmt2.setString(1, String.valueOf(account_id));
				ResultSet rs2 = stmt2.executeQuery();
				if (rs2.next()) {
					String id = rs2.getString("Id");
					String firstName = rs2.getString("firstName");
					String lastName = rs2.getString("lastName");
					String address = rs2.getString("Address");
					String city = rs2.getString("City");
					String state = rs2.getString("State");
					String zip = rs2.getString("ZipCode");
					String email = rs2.getString("Email");
					
					person.put("account_id", account_id);
					person.put("firstName", firstName);
					person.put("lastName", lastName);
					person.put("address", address);
					person.put("city", city);
					person.put("state",state);
					person.put("zip", zip);
					person.put("email", email);
					customer.add(person);
				}
				ConnectionUtils.close(conn2);
		    }
			ConnectionUtils.close(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        customers.put("customers", customer);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(customers);
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
