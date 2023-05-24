package dbadapter;

public class Participants {
	private int id;
	private boolean actual;
	
	public Participants(int id, boolean actual) {
		this.id = id;
		this.actual = actual;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return the actual
	 */
	public boolean isActual() {
		return actual;
	}
}
