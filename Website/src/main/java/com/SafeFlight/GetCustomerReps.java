package com.SafeFlight;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Servlet implementation class GetCustomerReps
 */
public class GetCustomerReps extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetCustomerReps() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = new JSONObject();
		JSONArray jArray = new JSONArray();
		
		try {
			Connection conn = ConnectionUtils.getMyConnection();
		        
		        Statement statement = conn.createStatement();
		        String sql = "{CALL getCustomerRepresentatives()}";
		        ResultSet rs = statement.executeQuery(sql);
		        
		        while(rs.next()) {
		        		JSONObject o = new JSONObject();

		        		o.put("ssn", rs.getInt("SSN"));
		        		o.put("startDate", rs.getDate("StartDate").toString());
		        		o.put("hourlyRate", rs.getDouble("HourlyRate"));
		        		o.put("firstName", rs.getString("FirstName"));
		        		o.put("lastName", rs.getString("LastName"));
		        		o.put("address", rs.getString("Address"));
		        		o.put("city", rs.getString("City"));
		        		o.put("state", rs.getString("State"));
		        		o.put("zipCode", rs.getInt("ZipCode"));
		        		o.put("revenue", rs.getDouble("Revenue"));
		        		
		        		jArray.add(o);
		        }
				ConnectionUtils.close(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        json.put("custReps", jArray);
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
