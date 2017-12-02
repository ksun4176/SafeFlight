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
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
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
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		JSONObject json = null;
		
		
		boolean hasError = false;
		String error = "";
		UserAccount user = null;
		
		System.out.println("test");
		
		if (username == null || password == null || username.length() == 0 || password.length() == 0) {
            hasError = true;
            System.out.println("Required username and password!");
        } 
		else {
			System.out.println("test4");
			try {
				json = DBUtils.findUser(username, password);
			} catch (ClassNotFoundException e) {
				hasError = true;
				e.printStackTrace();
			} catch (SQLException e) {
				hasError = true;
				e.printStackTrace();
			}
		}
		
		System.out.println("test5");
		
		if (hasError) {
			json = new JSONObject();
			json.put("account_id", -1);
		}
		
		System.out.println("hello");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(json);
		response.getWriter().flush();
	}

}
