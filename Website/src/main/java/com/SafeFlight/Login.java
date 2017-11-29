package com.SafeFlight;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		
		boolean hasError = false;
		String error = "";
		UserAccount user = null;
		
		if (username == null || password == null || username.length() == 0 || password.length() == 0) {
            hasError = true;
            error = "Required username and password!";
        } 
		else {
			try {
				Connection conn = ConnectionUtils.getMyConnection();
				user = DBUtils.findUser(conn, username, password);
				
				if (user == null) {
					hasError = true;
					error = "Username or Password is invalid.";
				}
				
			} catch (ClassNotFoundException e) {
				hasError = true;
				e.printStackTrace();
				error = "Class Not Found Exception";
			} catch (SQLException e) {
				hasError = true;
				error = "SQL Error";
				e.printStackTrace();
			}
		}
		
		if (hasError) {
			System.out.println(error);
		}
		else {
			 HttpSession session = request.getSession();
			 ConnectionUtils.storeLoginedUser(session, user);
			 
			 // Redirect user back
			 response.sendRedirect(request.getRequestURI());
		}
	}

}
