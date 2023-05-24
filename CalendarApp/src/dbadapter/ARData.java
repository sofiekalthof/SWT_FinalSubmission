package dbadapter;
import java.sql.Timestamp;
import datatypes.AddressData;
import datatypes.DateData;

public class ARData {
	private int id;
	private String name;
	private String description;
	private AddressData location;
	private int duration;
	private DateData date;
	private Timestamp deadline;
	
	public ARData(int id, String name, String description, AddressData location, int duration, DateData date, Timestamp deadline) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.location = location;
		this.duration = duration;
		this.date = date;
		this.deadline = deadline;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the location
	 */
	public AddressData getLocation() {
		return location;
	}

	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @return the deadline
	 */
	public Timestamp getDeadline() {
		return deadline;
	}

	public DateData getDateData() {
		return date;
	}
}
