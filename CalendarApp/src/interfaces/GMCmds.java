package interfaces;

import java.text.ParseException;
import java.util.ArrayList;

import dbadapter.ARData;
import datatypes.AddressData;

/**
 * Interface for all methodes that provide the required functionality of the App.
 * 
 * @author Gruppe 6 SWT Praktikum
 *
 */

public interface GMCmds {
	public Boolean makeAR(int gid, int uid, String name, String description, AddressData location, int duration, String date, String deadline, String participants);
	public Boolean createDate(int gid, int aid, int uid, String date) throws ParseException;
	public Boolean createVote(int gid, int did, int uid);
	public ArrayList<ARData> requestCD(int gid);
}