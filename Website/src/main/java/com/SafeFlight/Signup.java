package com.SafeFlight;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

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
		System.out.println("Servlet is called.");
		// TODO Auto-generated method stub
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String address = request.getParameter("address");
		String city = request.getParameter("city");
		String state = request.getParameter("state");
		String zip = request.getParameter("zip");
		String telephone = request.getParameter("telephone");
		String email = request.getParameter("email");
		String creditCardNo = request.getParameter("creditCardNo");
		int id = -1;

		try {
			Connection conn = ConnectionUtils.getMyConnection();
			 System.out.println("Get connection " + conn);
		        
		        // Testing SQL
		        Statement statement = conn.createStatement();
		        String query = "{Call addPerson(?, ?, ?, ?, ?, ?)}";
		        	CallableStatement stmt = conn.prepareCall(query);
		        	stmt.setString(1, firstName);
		        	stmt.setString(2, lastName);
		        	stmt.setString(3, address);
		        	stmt.setString(4, city);
		        	stmt.setString(5, state);
		        	stmt.setString(6, zip);
		        	stmt.executeQuery();
		        	query = "SELECT P.Id FROM Person P WHERE P.FirstName = ? AND P.LastName = ? AND P.Address = ? AND "
		        			+ "P.City = ? AND P.State = ? AND P.ZipCode = ?";
		        	stmt = conn.prepareCall(query);
		        	stmt.setString(1, firstName);
		        	stmt.setString(2, lastName);
		        	stmt.setString(3, address);
		        	stmt.setString(4, city);
		        	stmt.setString(5, state);
		        	stmt.setString(6, zip);
		        ResultSet rs = stmt.executeQuery();
		        while(rs.next()) {
		        		id = rs.getInt("Id");
		        }

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response.setContentType("text");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(id);
		response.getWriter().flush();
	}

}
