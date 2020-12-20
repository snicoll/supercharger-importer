package net.nicoll.supercharger.client;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.nicoll.supercharger.Supercharger;
import net.nicoll.supercharger.Supercharger.Status;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SuperchargerDeserializer}.
 *
 * @author Stephane Nicoll
 */
@JsonTest
class SuperchargerDeserializerTests {

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void deserializeSupercharger() throws IOException {
		Supercharger supercharger = deserialize("superchargeinfo/test-supercharger.json");
		assertThat(supercharger.getName()).isEqualTo("Test name");
		assertThat(supercharger.getStatus()).isEqualTo(Status.OPEN);
		assertThat(supercharger.getAddress()).satisfies((address) -> {
			assertThat(address).isNotNull();
			assertThat(address.getCountry()).isEqualTo("Test country");
			assertThat(address.getRegion()).isEqualTo("Test region");
			assertThat(address.getCity()).isEqualTo("Test city");
		});
		assertThat(supercharger.getLocation()).satisfies((location) -> {
			assertThat(location).isNotNull();
			assertThat(location.getLatitude()).isEqualTo(0.012d);
			assertThat(location.getLongitude()).isEqualTo(1.120d);
		});
	}

	@Test
	void deserializeSuperchargerWitNoAddress() throws IOException {
		Supercharger supercharger = deserialize("superchargeinfo/test-supercharger-missing-address.json");
		assertThat(supercharger.getAddress()).satisfies((address) -> {
			assertThat(address).isNotNull();
			assertThat(address.getCountry()).isNull();
			assertThat(address.getRegion()).isNull();
			assertThat(address.getCity()).isNull();
		});
	}

	private Supercharger deserialize(String path) throws IOException {
		ClassPathResource resource = new ClassPathResource(path);
		return this.objectMapper.readValue(resource.getURL(), Supercharger.class);
	}

}
