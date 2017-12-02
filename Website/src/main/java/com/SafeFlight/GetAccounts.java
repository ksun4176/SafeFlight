package com.SafeFlight;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Servlet implementation class GetAccounts
 */
public class GetAccounts extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetAccounts() {
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
			String account_num = request.getParameter("account_num");
			if(account_num == null) {
				String query = "{CALL getAllCustomers()}";
				CallableStatement stmt = conn.prepareCall(query);
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					JSONObject o = new JSONObject();
					int accid = rs.getInt("Id");
					int accnum = rs.getInt("AccountNo");
					String ccnum = rs.getString("CreditCardNo");
					String email = rs.getString("Email");
					Timestamp createdate = rs.getTimestamp("CreationDate");
					int rating = rs.getInt("Rating");
					String fname = rs.getString("FirstName");
					String lname = rs.getString("LastName");
					String address = rs.getString("Address");
					String city = rs.getString("City");
					String state = rs.getString("State");
					String zipcode = rs.getString("ZipCode");
					String uname = rs.getString("Username");
					String role = rs.getString("Role");
					
					o.put("account_id", accid);
					o.put("accout_num", accnum);
					o.put("credit_card_num", ccnum);
					o.put("email", email);
					o.put("creation_date", createdate);
					o.put("rating", rating);
					o.put("first_name", fname);
					o.put("last_name", lname);
					o.put("address", address);
					o.put("city", city);
					o.put("state", state);
					o.put("zipcode", zipcode);
					o.put("username", uname);
					if(role.equals("customer")) {
						o.put("account_type", 0);
					} else if(role.equals("employee")) {
						o.put("account_type", 1);
					} else if(role.equals("manager")) {
						o.put("account_type", 2);
					}
					
					jArray.add(o);
				}
			} else {
				String query = "{CALL getCustomer(?)}";
				CallableStatement stmt = conn.prepareCall(query);
				stmt.setString(1, account_num);
				ResultSet rs = stmt.executeQuery();
				if(!rs.next()) {
					throw new IllegalArgumentException("No account with this account_num");
				}
				JSONObject o = new JSONObject();
				int accid = rs.getInt("Id");
				int accnum = rs.getInt("AccountNo");
				String ccnum = rs.getString("CreditCardNo");
				String email = rs.getString("Email");
				Timestamp createdate = rs.getTimestamp("CreationDate");
				int rating = rs.getInt("Rating");
				String fname = rs.getString("FirstName");
				String lname = rs.getString("LastName");
				String address = rs.getString("Address");
				String city = rs.getString("City");
				String state = rs.getString("State");
				String zipcode = rs.getString("ZipCode");
				String uname = rs.getString("Username");
				String role = rs.getString("Role");
				
				o.put("account_id", accid);
				o.put("account_num", accnum);
				o.put("credit_card_num", ccnum);
				o.put("email", email);
				o.put("creation_date", createdate);
				o.put("rating", rating);
				o.put("first_name", fname);
				o.put("last_name", lname);
				o.put("address", address);
				o.put("city", city);
				o.put("state", state);
				o.put("zipcode", zipcode);
				o.put("username", uname);
				if(role.equals("customer")) {
					o.put("account_type", 0);
				} else if(role.equals("employee")) {
					o.put("account_type", 1);
				} else if(role.equals("manager")) {
					o.put("account_type", 2);
				}
				
				jArray.add(o);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		json.put("accounts", jArray);
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
