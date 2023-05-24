package testing;

import org.junit.Before;
import org.junit.Test;

import net.sourceforge.jwebunit.junit.WebTester;

public class GroupMemberGUITest {

	private WebTester tester;
	
	/*
	 * Tables appointments, dates, dates2participants have to be empty
	 */
	
	@Before
	public void prepare() throws Exception {
		tester = new WebTester();
		tester.setBaseUrl("http://localhost:8080/CalendarApp");
	}

	@Test
	public void test() {
		tester.beginAt("index");
		
		tester.assertTitleEquals("Group Calendar - Welcome");
		tester.assertFormPresent();
		tester.assertTextPresent("GID");
		tester.assertFormElementPresent("gid");
		tester.assertButtonPresent("SelectGID");
		tester.setTextField("gid", "1");
		tester.clickButton("SelectGID");
		
		// gmgui
		
		tester.assertTitleEquals("Group Calendar - Group Calendar Overview");
		tester.assertButtonPresentWithText("Add Appointment");
		tester.assertButtonPresentWithText("Show Calendar");
		tester.clickButtonWithText("Show Calendar");
		
		// show calendar
		
		tester.assertTitleEquals("Group Calendar - Calendar");
		tester.assertTablePresent("table");
		String[][] tableHeadings = { { "AID","Name", "Description", "Hand To", "Street", "Town", "ZIP", "Country", "Duration", "Date", "Deadline" } };
		tester.assertTableEquals("table", tableHeadings);
		tester.assertButtonPresentWithText("Back");
		tester.clickButtonWithText("Back");
		
		// gmgui
		
		tester.assertButtonPresentWithText("Add Appointment");
		tester.clickButtonWithText("Add Appointment");
		
		// add appointment
		tester.assertTitleEquals("Group Calendar - Add Appointment");
		tester.assertFormPresent();
		tester.assertTextPresent("Required Information");
		tester.assertFormElementPresent("name");
		tester.assertFormElementPresent("description");
		tester.assertFormElementPresent("locationHandTo");
		tester.assertFormElementPresent("locationStreet");
		tester.assertFormElementPresent("locationTown");
		tester.assertFormElementPresent("locationZip");
		tester.assertFormElementPresent("locationCountry");
		tester.assertFormElementPresent("duration");
		tester.assertFormElementPresent("date");
		tester.assertFormElementPresent("deadline");
		tester.assertFormElementPresent("participants");
		tester.assertButtonPresent("submit");
		tester.assertButtonPresentWithText("Back");
		tester.clickButtonWithText("Back");
		
		// gmgui
		tester.assertButtonPresentWithText("Answer AR");
		tester.clickButtonWithText("Answer AR");
		
		// answer ar
		tester.assertTitleEquals("Group Calendar - Answer AR");
		tester.assertTablePresent("table");
		String[][] answerARTableHeadings = { { "AID","Name", "Description", "Hand To", "Street", "Town", "ZIP", "Country", "Duration", "Date", "Deadline" , "Actions"} };
		tester.assertTableEquals("table", answerARTableHeadings);
		tester.assertButtonPresentWithText("Back");
		tester.clickButtonWithText("Back");
		
	}

}
