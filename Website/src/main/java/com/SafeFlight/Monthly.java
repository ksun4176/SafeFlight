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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Servlet implementation class Monthly
 */
public class Monthly extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Monthly() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Servlet is called.");
		JSONObject monthly = new JSONObject();
		JSONArray reservations = new JSONArray();
		
		try {
			Connection conn = ConnectionUtils.getMyConnection();
			System.out.println("Get connection " + conn);
			String month  = request.getParameter("month");
			String year = request.getParameter("year");
			if(!month.matches("(0[1-9])|(1[0-2])")) {
				throw new IllegalArgumentException("Invalid month");
			} 
			if(!year.matches("\\d{4}")) {
				throw new IllegalArgumentException("Invalid year");
			} 
		    // Testing SQL
		    Statement statement = conn.createStatement();
			String query = "{CALL getMonthlySalesReport(?, ?)}";
			CallableStatement stmt = conn.prepareCall(query);
			stmt.setString(1, month);
			stmt.setString(2, year);
			ResultSet rs = stmt.executeQuery();
			
		    while(rs.next()) {
		        	JSONObject o = new JSONObject();
		        	//return all reservations in that month
		        	int ResNo = rs.getInt("ResNo");
		        	Date ResDate = rs.getDate("ResDate");
		        	double BookingFee = rs.getDouble("BookingFee");
		        	double TotalFare = rs.getDouble("TotalFare");
		        	int RepID = rs.getInt("RepID");
		        	int AccountNo = rs.getInt("AccountNo");
	
		        	o.put("resveration_id", ResNo);
		        	o.put("ResDate", ResDate.toString());
		        	o.put("bookingFee", BookingFee);
		        	o.put("totalFare", TotalFare);
		        	o.put("customer_rep_id", RepID);
		        	o.put("account_id", AccountNo);
		        	reservations.add(o);
		     }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        monthly.put("reservations", reservations);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(monthly);
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
