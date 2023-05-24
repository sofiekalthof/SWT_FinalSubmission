package datatypes;

public class AddressData {

	private String street;
	private String handTo;
	private String town;
	private String zip;
	private String country;

	public AddressData(String street, String handTo, String town, String zip, String country) {
		this.street = street;
		this.handTo = handTo;
		this.town = town;
		this.zip = zip;
		this.country = country;
		
	}

	public String getStreet() {
		return street;
	}

	public String getHandTo() {
		return handTo;
	}	
	
	public String getTown() {
		return town;
	}

	public String getZip() {
		return zip;
	}

	public String getCountry() {
		return country;
	}

}
