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
 * Servlet implementation class MakeBid
 */
public class MakeBid extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MakeBid() {
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
			String airline_id = request.getParameter("airline_id");
			String flight_num = request.getParameter("flightNumber");
			String account_num = request.getParameter("account_num");
			String price = request.getParameter("price");
			
			String query = "{CALL bid(?,?,?,?,?)}";
			CallableStatement stmt = conn.prepareCall(query);
			stmt.setString(1, account_num);
			stmt.setString(2, airline_id);
			stmt.setString(3, flight_num);
			stmt.setString(4, "economy");
			stmt.setString(5, price);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			if(rs.getInt("Ok") == 0) {
				json.put("ok", false);
			} else if(rs.getInt("Ok") == 1) {
				json.put("ok", true);
			} else {
				json.put("ok", false);
			}
		} catch(SQLException e) {
			json.put("ok", false);
			e.printStackTrace();
		} catch(ClassNotFoundException e) {
			json.put("ok", false);
			e.printStackTrace();
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(json);
		response.getWriter().flush();
	}

}
