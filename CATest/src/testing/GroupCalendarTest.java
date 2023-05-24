package testing;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import datatypes.AddressData;
import datatypes.DateData;
import dbadapter.ARData;
import dbadapter.GroupCalendar;

public class GroupCalendarTest {

	// Create a test appointment request and insert it to the database before the tests to work with it 
	@BeforeClass
	public static void setUp() throws Exception {
		int gid = 1;
		int uid = 1;
		ARData testAR = new ARData(1, "Test Name", "Test Description", new AddressData("Test Street", "Test HandTo", "Test Town", "Test ZIP", "Test Country"),
				120, new DateData(0, Timestamp.valueOf("2019-12-31 00:00:00")), Timestamp.valueOf("2019-01-01 00:00:00"));
		List<Timestamp> dates = new ArrayList<>();
		List<Integer> participants = new ArrayList<>();
		dates.add(Timestamp.valueOf("2019-12-31 00:00:00"));
		participants.add(2);
		GroupCalendar.getInstance().createdAR(gid, uid, testAR.getName(), testAR.getDescription(), new AddressData(testAR.getLocation().getStreet(), 
				testAR.getLocation().getHandTo(), testAR.getLocation().getTown(), testAR.getLocation().getZip(), testAR.getLocation().getCountry()), 
				testAR.getDuration(), dates, testAR.getDeadline(), participants);
	}

	
	// Test to verify, that appointment requests can be created and inserted into the database
	@Test
	public void testCreatedAR() {
		int gid = 1;
		int uid = 1;
		ARData testAR = new ARData(1, "Test Name", "Test Description", new AddressData("Test Street", "Test HandTo", "Test Town", "Test ZIP", "Test Country"),
				120, new DateData(0, Timestamp.valueOf("2019-12-31 00:00:00")), Timestamp.valueOf("2019-01-01 00:00:00"));
		List<Timestamp> dates = new ArrayList<>();
		List<Integer> participants = new ArrayList<>();
		dates.add(Timestamp.valueOf("2019-12-31 00:00:00"));
		participants.add(2);
		assertTrue(GroupCalendar.getInstance().createdAR(gid, uid, testAR.getName(), testAR.getDescription(), new AddressData(testAR.getLocation().getStreet(), 
				testAR.getLocation().getHandTo(), testAR.getLocation().getTown(), testAR.getLocation().getZip(), testAR.getLocation().getCountry()), 
				testAR.getDuration(), dates, testAR.getDeadline(), participants));
	}

	// Test to verify if the date received is the same as the inserted data and to check if all required information of the calendar will be returned by the method
	@Test
	public void testRequestCal() {
		int gid = 1;
		ARData testAR = new ARData(1, "Test Name", "Test Description", new AddressData("Test Street", "Test HandTo", "Test Town", "Test ZIP", "Test Country"),
				120,new DateData(0, Timestamp.valueOf("2019-12-31 00:00:00")), Timestamp.valueOf("2019-01-01 00:00:00"));
		List<ARData> resList = new ArrayList<>();
		resList = GroupCalendar.getInstance().requestCal(gid);
		ARData res = resList.get(0);

		assertTrue(res.getName().equals(testAR.getName()));
		assertTrue(res.getDescription().equals(testAR.getDescription()));
		assertTrue(res.getLocation().getHandTo().equals(testAR.getLocation().getHandTo()));
		assertTrue(res.getLocation().getStreet().equals(testAR.getLocation().getStreet()));
		assertTrue(res.getLocation().getTown().equals(testAR.getLocation().getTown()));
		assertTrue(res.getLocation().getZip().equals(testAR.getLocation().getZip()));
		assertTrue(res.getLocation().getCountry().equals(testAR.getLocation().getCountry()));
		assertTrue(res.getDuration() == testAR.getDuration());
		assertTrue(res.getDateData().getDate().equals(testAR.getDateData().getDate()));
		assertTrue(res.getDeadline().equals(testAR.getDeadline()));
	}

	// Test to verify that a date can be added to the database and if the inserted value is correct
	@Test
	public void testAddDate() {
		int gid = 1;
		int uid = 2;
		List<ARData> resList = new ArrayList<>();
		resList = GroupCalendar.getInstance().requestCal(gid);
		ARData res = resList.get(0);

		assertTrue(GroupCalendar.getInstance().AddDate(gid, res.getId(), uid, Timestamp.valueOf("2018-12-31 00:00:00")));
	}

	// Test to verify that a vote can be added to an appointment
	@Test
	public void testAddVote() {
		int gid = 1;
		int uid = 2;
		List<ARData> resList = new ArrayList<>();
		resList = GroupCalendar.getInstance().requestCal(gid);
		ARData res = resList.get(0);
		
		assertTrue(GroupCalendar.getInstance().addVote(gid, res.getDateData().getDid(), uid));
	}
	
	// test to verify that the returned list of appointments has at least one, since an appointment the given user participates in, is added to the database before the execution of this test
	@Test
	public void testCheckParticipation() {
		int uid = 2;
		assertTrue(GroupCalendar.getInstance().checkParticipation(uid).size() > 0);
	}


}
