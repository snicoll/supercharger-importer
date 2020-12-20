package net.nicoll.supercharger.processor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.function.Function;

import net.nicoll.supercharger.Supercharger;

/**
 * Create an insert query for a Teslamate Geo-Fences entry.
 *
 * @author Stephane Nicoll
 */
public class TeslamateQueryBuilder implements Function<Supercharger, String> {

	private static final DateTimeFormatter SQL_TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private final Function<Supercharger, String> nameProvider;

	private final Function<Supercharger, Double> costProvider;

	public TeslamateQueryBuilder(Function<Supercharger, String> nameProvider,
			Function<Supercharger, Double> costProvider) {
		this.nameProvider = nameProvider;
		this.costProvider = costProvider;
	}

	@Override
	public String apply(Supercharger supercharger) {
		String name = this.nameProvider.apply(supercharger);
		Double fee = this.costProvider.apply(supercharger);
		String timestamp = LocalDateTime.now().format(SQL_TIMESTAMP);
		StringBuilder query = new StringBuilder("INSERT INTO public.geofences (");
		query.append("name, latitude, longitude, radius, inserted_at, updated_at, cost_per_unit, billing_type");
		query.append(") VALUES (");
		query.append(String.format(Locale.US, "%s', %f, %f, 35, '%s', '%s', %f, 'per_kwh'", name,
				supercharger.getLocation().getLatitude(), supercharger.getLocation().getLongitude(), timestamp,
				timestamp, fee));
		query.append(");");
		return query.toString();
	}

}
