package com.SafeFlight;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

/**
 * Servlet implementation class AccountDelete
 */
public class AccountDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AccountDelete() {
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
		Connection conn;
		JSONObject o = new JSONObject();

		try {
			conn = ConnectionUtils.getMyConnection();
			
			String accountId = request.getParameter("account_id");
			if (accountId != null ) {
				String query = "{CALL deletePerson(?)}";
				CallableStatement stmt = conn.prepareCall(query);
				stmt.setString(1, accountId);
				
				stmt.executeQuery();
				
				o.put("ok", true);
				
			}
			else {
				o.put("ok", false);
				o.put("error", "account_id not found");
			}
			
			ConnectionUtils.close(conn);
		} catch (ClassNotFoundException e) {

			o.put("ok", false);
			o.put("error", e.toString());
			e.printStackTrace();
		} catch (SQLException e) {
			o.put("ok", false);
			o.put("error", e.toString());
			e.printStackTrace();
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(o);
		response.getWriter().flush();

	}

}
