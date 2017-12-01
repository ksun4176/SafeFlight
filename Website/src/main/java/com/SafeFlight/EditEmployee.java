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
 * Servlet implementation class EditEmployee
 */
public class EditEmployee extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditEmployee() {
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
			String accountId = request.getParameter("account_id");
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String address = request.getParameter("address");
			String city = request.getParameter("city");
			String state = request.getParameter("state");
			String zipCode = request.getParameter("zip");
			String hourlyRate = request.getParameter("hourlyRate");
			if (accountId == null || firstName == null || lastName == null || address == null || city == null || state == null 
					|| zipCode == null || hourlyRate == null){
				throw new IllegalArgumentException("Missing Parameters");
	        }
			if(!accountId.matches("[0-9]+")) {
				throw new IllegalArgumentException("Invalid AccountId");
			}
			if(!zipCode.matches("[0-9]{5}")) {
				throw new IllegalArgumentException("Invalid ZipCode");
			}
			if(!hourlyRate.matches("[0-9]+\\.?[0-9]{0,2}")) {
				throw new IllegalArgumentException("Invalid Hourly Rate");
			}
			String query = "SELECT E.Id FROM Employee E WHERE E.Id = ?";
			CallableStatement stmt = conn.prepareCall(query);
			stmt.setString(1, accountId);
			ResultSet rs = stmt.executeQuery();
			if(rs.next() == false) {
				throw new IllegalArgumentException("No account with this id");
			}
			
			query = "{CALL editEmployee(?, ?, ?, ?, ?, ?, ?, ?)}";
			stmt = conn.prepareCall(query);
			stmt.setString(1, accountId);
			stmt.setString(2, firstName);
			stmt.setString(3, lastName);
			stmt.setString(4, address);
			stmt.setString(5, city);
			stmt.setString(6, state);
			stmt.setString(7, zipCode);
			stmt.setString(8, hourlyRate);
			rs = stmt.executeQuery();

			json.put("ok",true);
		} catch(IllegalArgumentException e){
			e.printStackTrace();
			json.put("ok",false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.put("ok",false);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.put("ok",false);
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(json);
		response.getWriter().flush();
	}

}
