package net.nicoll.supercharger;

/**
 * A representation of a supercharger.
 *
 * @author Stephane Nicoll
 */
public final class Supercharger {

	private final String name;

	private final Status status;

	private final Address address;

	private final Location location;

	public Supercharger(String name, Status status, Address address, Location location) {
		this.name = name;
		this.status = status;
		this.address = address;
		this.location = location;
	}

	public String getName() {
		return this.name;
	}

	public Status getStatus() {
		return this.status;
	}

	public Address getAddress() {
		return this.address;
	}

	public Location getLocation() {
		return this.location;
	}

	public enum Status {

		CLOSED_PERM,

		CLOSED_TEMP,

		CONSTRUCTION,

		OPEN,

		PERMIT

	}

	public static class Address {

		private final String country;

		private final String region;

		private final String city;

		public Address(String country, String region, String city) {
			this.country = country;
			this.region = region;
			this.city = city;
		}

		public String getCountry() {
			return this.country;
		}

		public String getRegion() {
			return this.region;
		}

		public String getCity() {
			return this.city;
		}

	}

	public static class Location {

		private final double latitude;

		private final double longitude;

		public Location(double latitude, double longitude) {
			this.latitude = latitude;
			this.longitude = longitude;
		}

		public double getLatitude() {
			return this.latitude;
		}

		public double getLongitude() {
			return this.longitude;
		}

	}

}
