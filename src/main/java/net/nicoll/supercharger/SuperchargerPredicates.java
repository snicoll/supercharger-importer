package net.nicoll.supercharger;

import java.util.function.Predicate;

import net.nicoll.supercharger.Supercharger.Status;

/**
 * Useful {@link Predicate} to filter {@link Supercharger superchargs}.
 *
 * @author Stephane Nicoll
 */
public class SuperchargerPredicates {

	/**
	 * Match superchargers from the specified {@code country}.
	 * @param country the name of the country
	 * @return a predicate that matches superchargers from that country
	 */
	public static Predicate<Supercharger> country(String country) {
		return (supercharger) -> country.equals(supercharger.getAddress().getCountry());
	}

	/**
	 * Match superchargers having the specified {@link Status}.
	 * @param status the expectede status
	 * @return a predicate that matches superchargers with that status
	 */
	public static Predicate<Supercharger> status(Status status) {
		return (supercharger) -> status == supercharger.getStatus();
	}

	/**
	 * Match available superchargers (i.e. those who are {@link Status#OPEN open} or
	 * {@link Status#CLOSED_TEMP closed temporarily}.
	 * @return a predicate that matches available superchargers
	 */
	public static Predicate<Supercharger> available() {
		return status(Status.OPEN).or(status(Status.CLOSED_TEMP));
	}

}
