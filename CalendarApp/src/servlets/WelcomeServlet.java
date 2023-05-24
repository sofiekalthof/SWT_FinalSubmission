package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Gruppe 6 SWT Praktikum
 *
 *         This servlet only contains a small index webpage where the user is
 *         able to choose a Group Calendar.
 */
public class WelcomeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * doGet contains the call for the index webpage
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) {

		// Set page title and nav type
		request.setAttribute("pagetitle", "Welcome");

		// Dispatch request to template engine
		try {
			request.getRequestDispatcher("/templates/index.ftl").forward(
					request, response);
		} catch (ServletException | IOException e) {
			request.setAttribute("errormessage",
					"Template error: please contact the administrator");
			e.printStackTrace();
		}
	}

	/**
	 * doPost will not be called during this flow
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		doGet(request, response);
	}
}