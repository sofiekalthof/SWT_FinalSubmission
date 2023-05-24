package servlets;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import application.CAApplication;

import datatypes.AddressData;
import dbadapter.ARData;




public class GroupMemberGUI extends HttpServlet{

	/**
	 * @author Gruppe 6 SWT Praktikum
	 * 
	 * This is the main servlet that provides almost the entire functionality of the app. 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String gid;
	int uid = 2;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		String action = (request.getParameter("action") == null) ? "" : request.getParameter("action");		
		gid = (request.getParameter("gid") == null) ? "" : request.getParameter("gid"); 

		/*
		 *	ADD APPOINTMENT
		 */

		if (action.equals("addAppointment")) {
			// Set request attributes
			request.setAttribute("pagetitle", "Add Appointment");
			request.setAttribute("gid", request.getParameter("gid"));

			// Dispatch request to template engine
			try {
				request.getRequestDispatcher("/templates/addAppointment.ftl").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}

			/*
			 *	SHOW CALENDAR
			 */

		} else if (action.equals("showCalendar")){

			// Set request attributes
			request.setAttribute("pagetitle", "Calendar");
			request.setAttribute("gid", request.getParameter("gid"));

			List<ARData> ar = new ArrayList<>();
			ar = CAApplication.getInstance().requestCD(Integer.parseInt(gid));

			request.setAttribute("appointmentRequests", ar);



			// Dispatch request to template engine
			try {
				request.getRequestDispatcher("/templates/showCalendar.ftl").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			} 


			/*
			 *  ANSWER AR	
			 */

		} else if (action.equals("selectAR")) {
			request.setAttribute("pagetitle", "Answer AR");
			request.setAttribute("gid", request.getParameter("gid"));

			List<Integer> appointments = CAApplication.getInstance().checkParticipant(uid);

			List<ARData> ar = new ArrayList<>();
			ar = CAApplication.getInstance().requestCD(Integer.parseInt(gid));

			List<ARData> arOut = new ArrayList<>();
			
			for(ARData a: ar) {
				if(appointments.contains(a.getId())) {
					arOut.add(a);
				}
			}
			
			request.setAttribute("appointmentRequests", arOut);

			try {
				request.getRequestDispatcher("/templates/selectAR.ftl").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/*
		 *	ADD VOTE
		 */

		else if (action.equals("addvote")) {

			request.setAttribute("pagetitle", "Add Vote");
			request.setAttribute("gid", request.getParameter("gid"));
			request.setAttribute("did",request.getParameter("did"));


			try {
				request.getRequestDispatcher("/templates/showVote.ftl").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/*
		 *	ADD DATE
		 */

		else if (action.equals("adddate")) {

			request.setAttribute("pagetitle", "Add Date");
			request.setAttribute("gid", request.getParameter("gid"));
			request.setAttribute("aid",request.getParameter("aid"));


			try {
				request.getRequestDispatcher("/templates/showDate.ftl").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		else {

			// Set request attributes
			request.setAttribute("pagetitle", "Group Calendar Overview");
			if (request.getParameter("gid").equals(null)) {
				request.setAttribute("gid2", 0);
			} else {
				request.setAttribute("gid2", gid);
			}
			// Dispatch request to template engine
			try {
				request.getRequestDispatcher("/templates/defaultWebpage.ftl").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		
		/*
		 *	ADD CALENDAR -> createAR
		 */

		if (request.getParameter("action").equals("createAR")) {
			// Decide whether booking was successful or not
			if (CAApplication.getInstance().makeAR(Integer.parseInt(gid),1, request.getParameter("name"), request.getParameter("description"), new AddressData(request.getParameter("locationStreet"), 
					request.getParameter("locationHandTo"), request.getParameter("locationTown"), request.getParameter("locationZip"), request.getParameter("locationCountry")), 
					Integer.parseInt(request.getParameter("duration")), request.getParameter("date"), request.getParameter("deadline"), request.getParameter("participants"))) {

				// Set request attributes
				request.setAttribute("pagetitle", "Successfully added Appointment");
				request.setAttribute("message",
						"Appointment successfully created.");

				request.setAttribute("gid", request.getParameter("gid"));

				// Dispatch to template engine
				try {
					request.getRequestDispatcher("/templates/okRepresentation.ftl").forward(request, response);
				} catch (ServletException | IOException e) {
					e.printStackTrace();
				}

				// Catch booking error and print an error on the gui
			} else {

				request.setAttribute("pagetitle", "Failure");
				request.setAttribute("message", "Adding Appointment failed");
				request.setAttribute("gid", request.getParameter("gid"));

				try {
					request.getRequestDispatcher("/templates/okRepresentation.ftl").forward(request, response);
				} catch (ServletException | IOException e) {
					e.printStackTrace();
				}
			}
		} else if(request.getParameter("action").equals("SelectGID")){
			gid = request.getParameter("gid");
			request.setAttribute("gid2", gid);
			request.removeAttribute("action");

			doGet(request, response);
		}

		/*
		 * 	ADD VOTE -> createVote
		 */

		else if(request.getParameter("action").equals("createVote")) {
			if(CAApplication.getInstance().createVote( Integer.parseInt(request.getParameter("gid")) , Integer.parseInt(request.getParameter("did")) , uid)) {

				request.setAttribute("pagetitle", "Successfully voted");
				request.setAttribute("message", "Successfully voted");
				request.setAttribute("gid", request.getParameter("gid"));

				try {
					request.getRequestDispatcher("/templates/okRepresentation.ftl").forward(request, response);
				} catch (ServletException | IOException e) {
					e.printStackTrace();
				}
			} else {

				request.setAttribute("pagetitle", "Failure");
				request.setAttribute("message", "Voting failed");
				request.setAttribute("gid", request.getParameter("gid"));

				try {
					request.getRequestDispatcher("/templates/okRepresentation.ftl").forward(request, response);
				} catch (ServletException | IOException e) {
					e.printStackTrace();
				}
			}

		}

		/*
		 * 		ADD DATE -> createDate
		 */

		else if(request.getParameter("action").equals("createDate")) {
			try {
				if(CAApplication.getInstance().createDate(Integer.parseInt(request.getParameter("gid")), Integer.parseInt(request.getParameter("aid")), uid, request.getParameter("date"))) {

					request.setAttribute("pagetitle", "Successfully added Date");
					request.setAttribute("message", "Successfully added Date");
					request.setAttribute("gid", request.getParameter("gid"));

					try {
						request.getRequestDispatcher("/templates/okRepresentation.ftl").forward(request, response);
					} catch (ServletException | IOException e) {
						e.printStackTrace();
					}				
				} else {

					request.setAttribute("pagetitle", "Failure");
					request.setAttribute("message", "Adding date failed");
					request.setAttribute("gid", request.getParameter("gid"));

					try {
						request.getRequestDispatcher("/templates/okRepresentation.ftl").forward(request, response);
					} catch (ServletException | IOException e) {
						e.printStackTrace();
					}
				}
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

}

