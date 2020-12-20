package net.nicoll.supercharger.client;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import net.nicoll.supercharger.Supercharger;
import net.nicoll.supercharger.Supercharger.Address;
import net.nicoll.supercharger.Supercharger.Location;
import net.nicoll.supercharger.Supercharger.Status;

import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.jackson.JsonObjectDeserializer;

/**
 * A {@link Supercharger} deserializer that works against the {@code supercharger.info}
 * dataset.
 *
 * @author Stephane Nicoll
 */
@JsonComponent
class SuperchargerDeserializer extends JsonObjectDeserializer<Supercharger> {

	@Override
	protected Supercharger deserializeObject(JsonParser jsonParser, DeserializationContext context, ObjectCodec codec,
			JsonNode tree) {
		String name = nullSafeValue(tree.get("name"), String.class);
		Status status = Enum.valueOf(Status.class, nullSafeValue(tree.get("status"), String.class));
		return new Supercharger(name, status, deserializeAddress(tree.get("address")),
				deserializeLocation(tree.get("gps")));

	}

	private Address deserializeAddress(JsonNode address) {
		return new Address(nullSafeValue(address.get("country"), String.class),
				nullSafeValue(address.get("region"), String.class), nullSafeValue(address.get("city"), String.class));
	}

	private Location deserializeLocation(JsonNode gps) {
		return new Location(nullSafeValue(gps.get("latitude"), Double.class),
				nullSafeValue(gps.get("longitude"), Double.class));
	}

}
