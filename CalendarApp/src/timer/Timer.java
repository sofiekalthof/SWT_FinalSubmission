package timer;

import application.CAApplication;

/**
 * Timer class to call the method getDeadline() in the application. Main method
 * can be executed in a scheduled way.
 * 
 * @author Gruppe 6 SWT Praktikum
 *
 */
public class Timer {

	public static void main(String[] args) {
		CAApplication CaApp = new CAApplication();
		CaApp.getDeadline();
		System.out.println("Checked all Deadlines.");
	}
}
