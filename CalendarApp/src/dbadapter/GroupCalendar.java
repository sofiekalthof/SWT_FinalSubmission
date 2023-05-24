package dbadapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import datatypes.AddressData;
import datatypes.DateData;
import interfaces.IGroupCalendar;

public class GroupCalendar implements IGroupCalendar{
	private static GroupCalendar instance;

	private GroupCalendar() {
		try {
			Class.forName("com." + Configuration.getType() + ".jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static GroupCalendar getInstance() {
		if (instance == null) {
			instance = new GroupCalendar();
		}
		return instance;
	}

	public static void setInstance(GroupCalendar groupcalendar) {
		instance = groupcalendar;
	}

	
	/*
	 *	Database adapter call to insert an appointment request and all it's information in the respective tables of the database
	 */
	@Override
	public Boolean createdAR(int gid, int uid, String name, String description, AddressData location, int duration,
			List<Timestamp> date, Timestamp deadline, List<Integer> participants) {

		// Declare necessary SQL queries.
		String sqlInsertAppointment = "INSERT INTO appointments (gid,name,description,locationHandTo,locationStreet,locationTown,locationZIP,"
				+ "locationCountry,duration,deadline) VALUES (?,?,?,?,?,?,?,?,?,?)";
		String sqlInsertDate = "INSERT INTO dates (aid, date, fixed) VALUES(?,?,?)";
		String sqlAIDQuery = "Select distinct aid FROM appointments Where gid=? and name=? and description=? and duration=? and deadline=?";
		String sqlDateQuery = "Select did FROM Dates where aid = ?";
		String sqlInsertDates2Participants = "INSERT INTO dates2participants (uid, did) VALUES(?,?)";
		String sqlInsertParticipants = "INSERT INTO participants (aid, uid) VALUES(?,?)";


		try (Connection connection = DriverManager.getConnection(
				"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
						+ Configuration.getPort() + "/" + Configuration.getDatabase(),
						Configuration.getUser(), Configuration.getPassword())) {
			try (
					PreparedStatement psInsertAppointment = connection.prepareStatement(sqlInsertAppointment);
					PreparedStatement psInsertDate = connection.prepareStatement(sqlInsertDate);
					PreparedStatement psAIDQuery = connection.prepareStatement(sqlAIDQuery);
					PreparedStatement psDateQuery = connection.prepareStatement(sqlDateQuery);
					PreparedStatement psInsertd2Participants = connection.prepareStatement(sqlInsertDates2Participants);
					PreparedStatement psInsertParticipants = connection.prepareStatement(sqlInsertParticipants);
					){

				// set all empty variables to insert a date to the appointment table
				psInsertAppointment.setInt(1, gid);
				psInsertAppointment.setString(2, name);
				psInsertAppointment.setString(3, description);
				psInsertAppointment.setString(4, location.getHandTo());
				psInsertAppointment.setString(5, location.getStreet());
				psInsertAppointment.setString(6, location.getTown());
				psInsertAppointment.setString(7, location.getZip());
				psInsertAppointment.setString(8, location.getCountry());
				psInsertAppointment.setInt(9, duration);
				psInsertAppointment.setTimestamp(10, deadline);
				psInsertAppointment.executeUpdate();

				// set variables to get aid respective to the required input
				psAIDQuery.setInt(1, gid);
				psAIDQuery.setString(2, name);
				psAIDQuery.setString(3, description);
				psAIDQuery.setInt(4, duration);
				psAIDQuery.setTimestamp(5, deadline);
				ResultSet rs = psAIDQuery.executeQuery();

				// set the aid for every date as well as set variables to get the dates of a respective appointment
				for(int i = 0; i < date.size(); i++) {			
					while(rs.next()) {
						psInsertDate.setInt(1, rs.getInt("aid"));
						psDateQuery.setInt(1, rs.getInt("aid"));
						psInsertParticipants.setInt(1, rs.getInt("aid"));
					}
					psInsertDate.setTimestamp(2, date.get(i));
					psInsertDate.setBoolean(3, false);
					psInsertDate.executeUpdate();
				}

				ResultSet drs = psDateQuery.executeQuery();

				// set variables to insert the relation between users and dates to the dates2participants table 
				while(drs.next()){
					psInsertd2Participants.setInt(1, uid);
					psInsertd2Participants.setInt(2, drs.getInt("did"));
					psInsertd2Participants.executeUpdate();
				}
				
				// set variables to insert a participant to an appointment in the particiapants table
				for(int i = 0; i < participants.size(); i++) {
					psInsertParticipants.setInt(2, participants.get(i));
					psInsertParticipants.executeUpdate();
				}

				return true;

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/*
	 *	Database adapter call to insert a date and all it's information in the respective tables of the database
	 */
	@Override
	public Boolean AddDate(int gid, int aid, int uid, Timestamp date) {
		
		// Declare necessary SQL queries.
		String sqlCheckDate = "Select Count(date) as date FROM dates WHERE date = ? and aid = ?";
		String sqlAddDate = "INSERT INTO dates (aid, date, fixed) Values(?, ?, ?)";
		String sqlGetDid = "Select did FROM dates Where aid = ? and date = ?";
		String sqlAddParticipant = "INSERT INTO dates2participants (uid, did) VALUES(?,?)";
		Boolean exists = true;

		try (Connection connection = DriverManager
				.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
								Configuration.getUser(), Configuration.getPassword())) {

			try (
					PreparedStatement psCheckDate = connection.prepareStatement(sqlCheckDate);
					PreparedStatement psAddDate = connection.prepareStatement(sqlAddDate);
					PreparedStatement psGetDid = connection.prepareStatement(sqlGetDid);
					PreparedStatement psAddParticipant = connection.prepareStatement(sqlAddParticipant);
					) {
				
				// set variables to check if the date exists in the database
				psCheckDate.setTimestamp(1, date);
				psCheckDate.setInt(2, aid);
				ResultSet rs = psCheckDate.executeQuery();
				while (rs.next()) {
					exists = (rs.getInt("date") != 0);
				}
				if(exists) {
					return false;
				} else {
					
					// if date exists set variables to insert the information to the tables in the database
					psAddDate.setInt(1, aid);
					psAddDate.setTimestamp(2, date);
					psAddDate.setBoolean(3, false);
					psAddDate.executeUpdate();

					psGetDid.setInt(1, aid);
					psGetDid.setTimestamp(2, date);
					ResultSet drs = psGetDid.executeQuery();
					while (drs.next()) {
						psAddParticipant.setInt(2, drs.getInt("did"));
					}
					psAddParticipant.setInt(1, uid);
					psAddParticipant.executeUpdate();

					return true;
				}

			}catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
		// TODO Auto-generated method stub

	}

	
	/*
	 *	Database adapter call to insert a vote and all it's information in the respective tables of the database
	 */
	@Override
	public Boolean addVote(int gid, int did, int uid) {
		// Declare necessary SQL queries.
		String sqlCheckVote = "Select Count(did) as did FROM dates2participants WHERE did = ? and uid = ?";
		String sqlAddVote = "INSERT INTO dates2participants(did, uid) VALUES(?, ?)";
		Boolean voted = true;

		try (Connection connection = DriverManager
				.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
								Configuration.getUser(), Configuration.getPassword())) {

			try (
					PreparedStatement psCheckVote = connection.prepareStatement(sqlCheckVote);
					PreparedStatement psAddVote = connection.prepareStatement(sqlAddVote);		
					) {
				// check if the date to vote on even exists
				psCheckVote.setInt(1, did);
				psCheckVote.setInt(2, uid);
				ResultSet rs = psCheckVote.executeQuery();
				while (rs.next()) {
					voted = (rs.getInt("did") != 0);
				}
				if(voted) {
					return false;
				} else {
					// if the date exists set required variables to insert the date into the respective tables in the database
					psAddVote.setInt(1, did);
					psAddVote.setInt(2, uid);
					psAddVote.executeUpdate();
					return true;
				}

			}catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/*
	 *	Database adapter call to get the information to display the entire calendar
	 */
	@Override
	public ArrayList<ARData> requestCal(int gid) {
		ArrayList<ARData> ar = new ArrayList<>();
		String sqlRequestCal = "SELECT a.aid, d.did,a.name,a.description,a.locationHandTo, a.locationStreet,a.locationTown,a.locationZIP,"
				+ "a.locationCountry,a.duration,d.date,a.deadline FROM appointments AS a join dates AS d on a.aid = d.aid WHERE  a.gid = "+ gid; 
		try (Connection connection = DriverManager
				.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
								Configuration.getUser(), Configuration.getPassword())) {
			try (PreparedStatement psRequestCal = connection.prepareStatement(sqlRequestCal)) {
				ResultSet rs = psRequestCal.executeQuery();
				while (rs.next()) {
					ar.add(new ARData(rs.getInt("aid"), rs.getString("name"), rs.getString("description"), new AddressData(rs.getString("locationStreet"), 
							rs.getString("locationHandTo"), rs.getString("locationTown"), rs.getString("locationZIP"), rs.getString("locationCountry")),
							rs.getInt("duration"), new DateData(rs.getInt("did"), rs.getTimestamp("date")), rs.getTimestamp("deadline")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ar;
	}

	/*
	 *	Database adapter call to set dates to fixed if they have been voted for and their deadline expired
	 */
	@Override
	public void markDates() {
		String sqlMarkDates = "UPDATE dates SET fixed = 1 WHERE did IN (SELECT did FROM dates WHERE did IN (SELECT p1.did FROM (SELECT did, Count"
				+ "(uid) AS psum FROM dates2participants GROUP BY did) AS p1, (SELECT p2.aid, "
				+ "Max(p2.psum) AS psum FROM (SELECT ad2p.aid, Count(ad2p.uid) AS psum FROM ( "
				+ "SELECT a.aid, d.did, d2p.uid FROM appointments AS a JOIN dates AS d ON a.aid "
				+ "= d.aid JOIN dates2participants AS d2p ON d.did = d2p.did WHERE "
				+ "CURRENT_TIMESTAMP > a.deadline) AS ad2p GROUP BY ad2p.did, ad2p.aid) AS p2 "
				+ "GROUP BY p2.aid) AS p3 WHERE p1.psum = p3.psum AND dates.aid = p3.aid AND "
				+ "dates.did = p1.did))";
		try (Connection connection = DriverManager
				.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
								Configuration.getUser(), Configuration.getPassword())) {
			try (PreparedStatement psMarkDates = connection.prepareStatement(sqlMarkDates)) {
				psMarkDates.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 *	Database adapter call to remove dates that have not received enough votes
	 */
	@Override
	public void removePreDates() {
		String sqlRemovePreDates = "DELETE FROM dates WHERE fixed = 0 AND aid IN (SELECT a.aid FROM appointments AS a WHERE CURRENT_TIMESTAMP > a.deadline)";
		try (Connection connection = DriverManager
				.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
								Configuration.getUser(), Configuration.getPassword())) {
			try (PreparedStatement psRemovePreDates = connection.prepareStatement(sqlRemovePreDates)) {
				psRemovePreDates.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 *	Database adapter call to set participants to actual participants in a respective appointment after it has been set to fixed
	 */
	@Override
	public void setParticipants() {
		String sqlSetParticipants = "UPDATE participants SET actual = 1 WHERE uid IN (SELECT ad2p.uid FROM (SELECT "
				+ "d2p.did, d2p.uid FROM appointments AS a JOIN dates AS d ON a.aid = d.aid "
				+ "JOIN dates2participants AS d2p ON d.did = d2p.did WHERE CURRENT_TIMESTAMP > a.deadline) "
				+ "AS ad2p WHERE ad2p.did = (SELECT did FROM dates WHERE fixed = 1 AND aid "
				+ "= participants.aid))";
		try (Connection connection = DriverManager
				.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
								Configuration.getUser(), Configuration.getPassword())) {
			try (PreparedStatement psSetParticipants = connection.prepareStatement(sqlSetParticipants)) {
				psSetParticipants.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 *	Database adapter call to check if a group calendar with ID "gid" exists
	 */
	public boolean checkGroupCalendarById(int gid) {

		// Declare necessary SQL query.
		String queryGC = "SELECT * FROM groupcalendar WHERE gid=?";

		// query data.
		try (Connection connection = DriverManager
				.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
								Configuration.getUser(), Configuration.getPassword())) {
			try (PreparedStatement psSelect = connection.prepareStatement(queryGC)) {
				psSelect.setInt(1, gid);
				try (ResultSet rs = psSelect.executeQuery()) {
					return rs.next();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 *	Database adapter call to return a list of all appointments a user with ID "uid" is a part of
	 */
	public List<Integer> checkParticipation(int uid) {
		List<Integer> appointments = new ArrayList<>();
		String sqlCheckParticipation = "SELECT dates.aid FROM (SELECT aid FROM participants WHERE uid = ?) AS a JOIN dates ON dates.aid = a.aid WHERE fixed = 0";
		try (Connection connection = DriverManager
				.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
								Configuration.getUser(), Configuration.getPassword())) {
			try (PreparedStatement psCheckParticipation = connection.prepareStatement(sqlCheckParticipation)) {
				psCheckParticipation.setInt(1, uid);
				ResultSet rs = psCheckParticipation.executeQuery();
				while(rs.next()) {
					appointments.add(rs.getInt("aid"));
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appointments;
	}
}
