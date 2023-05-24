package datatypes;

import java.sql.Timestamp;

public class DateData {

	private int did;
	private Timestamp date;

	public DateData(int did, Timestamp date) {
		this.did = did;
		this.date = date;
	}

	public int getDid() {
		return did;
	}

	public Timestamp getDate() {
		return date;
	}
}