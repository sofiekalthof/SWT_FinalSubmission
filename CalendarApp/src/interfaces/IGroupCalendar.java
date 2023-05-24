package interfaces;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import datatypes.AddressData;
import dbadapter.ARData;

/**
 * Interface for all methodes used to let the App communicate with the database.
 * 
 * @author Gruppe 6 SWT Praktikum
 *
 */

public interface IGroupCalendar {
	public Boolean createdAR(int gid, int uid, String name, String description, AddressData location, int duration, List<Timestamp> date, Timestamp deadline, List<Integer> participants);
	public Boolean AddDate(int gid, int aid, int uid, Timestamp date);
	public Boolean addVote(int gid, int did, int uid);
	public ArrayList<ARData> requestCal(int gid);
	public void markDates();
	public void removePreDates();
	public void setParticipants();
}
