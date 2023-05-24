package dbadapter;

/**
 * This class is used to declare access data for the SQL server
 * 
 * @author Gruppe 6 SWT Praktikum
 *
 */
public class Configuration {
	
	// Replace these static values with your testing database configuration.
	private static final String SERVER = "localhost";
	private static final String TYPE = "mysql";
	private static final int PORT = 3306;
	private static final String DATABASE = "swtdev";
	private static final String USER = "root";
	
	/**
	 * This password in stored in plain text. We assume that only the owner can
	 * access this file and that all login data is transfered in an encrypted way
	 * (e.g. SSL).
	 */
	private static final String PASSWORD = "1234";

	public static String getServer() {
		return SERVER;
	}

	public static String getType() {
		return TYPE;
	}

	public static int getPort() {
		return PORT;
	}

	public static String getDatabase() {
		return DATABASE;
	}

	public static String getUser() {
		return USER;
	}

	public static String getPassword() {
		return PASSWORD;
	}

}
