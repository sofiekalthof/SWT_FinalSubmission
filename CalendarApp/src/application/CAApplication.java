package application;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import datatypes.AddressData;
import dbadapter.ARData;
import dbadapter.GroupCalendar;
import interfaces.GMCmds;

public class CAApplication implements GMCmds {
	private static CAApplication instance;

	public static CAApplication getInstance() {
		if (instance == null) {
			instance = new CAApplication();
		}
		return instance;
	}

	@Override
	public Boolean makeAR(int gid, int uid, String name, String description, AddressData inLocation,
			int duration, String inDate, String inDeadline, String participants) {

		assert preAddAppointment(gid) : "Precondition not satisfied";

		// Create result object
		Boolean okfail = false;

		/* 
		 * Parse inputs to correct datatypes
		 * Multiple dates are entered as a string and divided by a semicolon 
		 * Multiple participants are entered as a string and divided by a comma
		 * With the following functions we parse them to the correct datatypes.
		 */ 

		try {
			String[] dateArray = inDate.split(";");
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			List<Timestamp> dateSQL = new ArrayList<>();
			for(int i = 0; i < dateArray.length;i++) {
				Date date = dateFormat.parse(dateArray[i]);
				long time = date.getTime();
				dateSQL.add(i, new Timestamp(time));
			}
			String[] participantsArray = participants.split(",");
			List<Integer> participantsSQL = new ArrayList<>();
			participantsSQL.add(0, uid);
			for(int i = 0; i < participantsArray.length; i++) {
				participantsSQL.add(i, Integer.parseInt(participantsArray[i]));
			}


			DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			Date deadline = dateFormat2.parse(inDeadline);
			long time2 = deadline.getTime();
			Timestamp deadlineSQL = new Timestamp(time2);
			okfail = GroupCalendar.getInstance().createdAR(gid, uid, name, description, inLocation, duration, dateSQL, deadlineSQL, participantsSQL);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return okfail;
	}
	
	/*
	 *	Application call to check if the Group Calendar with ID "gid" exists
	 */

	private boolean preAddAppointment(int gid) {
		return GroupCalendar.getInstance().checkGroupCalendarById(gid);
	}
	
	/*
	 *	Returns a list of all IDs of appointments the user with "uid" participates in 
	 */
	
	public List<Integer> checkParticipant(int uid) {
		return GroupCalendar.getInstance().checkParticipation(uid);
	}

	/*
	 *	App call to get the calendar data from the database adapter
	 */
	
	@Override
	public ArrayList<ARData> requestCD(int gid) {
		return GroupCalendar.getInstance().requestCal(gid); 
	}

	/*
	 *	App call to add votes to the database via the database adapter
	 */
	
	@Override
	public Boolean createVote(int gid, int did, int uid) {
		return GroupCalendar.getInstance().addVote(gid, did, uid);
	}
	
	/*
	 *	App call to add dates to the database via the database adapter
	 */

	@Override
	public Boolean createDate(int gid, int aid, int uid, String inDate) throws ParseException {

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date date = dateFormat.parse(inDate);
		long time = date.getTime();
		Timestamp dateSQL = new Timestamp(time);

		return GroupCalendar.getInstance().AddDate(gid, aid, uid, dateSQL);
	}
	
	/*
	 *	getDeadline method called by Timer to periodically update the appointments and set them to fixed, etc.
	 */

	public void getDeadline() {
		GroupCalendar.getInstance().markDates();
		GroupCalendar.getInstance().setParticipants();
		GroupCalendar.getInstance().removePreDates();
		
	}


}