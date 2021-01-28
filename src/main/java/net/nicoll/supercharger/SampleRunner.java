package net.nicoll.supercharger;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.nicoll.supercharger.client.SuperchargeWebClient;
import net.nicoll.supercharger.processor.TeslamateQueryBuilder;
import reactor.core.publisher.Flux;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * This is a sample that demonstrates how to use the tool. It generates an insert query
 * for Teslamate for each Supercharger in Belgium.
 * <p>
 * The {@link #determineName(Supercharger)} and {@link #determineFeeKwh(Supercharger)} can
 * be used to tune how the entry is generated.
 * <p>
 * By default, the data comes from a cached copy of {@code supercharge.info} but a web
 * client can be used to fetch live date.
 *
 * @author Stephane Nicoll
 * @see TeslamateQueryBuilder
 */
@Component
public class SampleRunner implements ApplicationRunner {

	private final ObjectMapper mapper;

	private final SuperchargeWebClient client;

	public SampleRunner(ObjectMapper mapper, SuperchargeWebClient client) {
		this.mapper = mapper;
		this.client = client;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		TeslamateQueryBuilder queryBuilder = new TeslamateQueryBuilder(this::determineName, this::determineFeeKwh);
		getAllSuperchargers(this.mapper)
				.filter(SuperchargerPredicates.available().and(SuperchargerPredicates.country("Belgium"))).toStream()
				.map(queryBuilder).forEach(System.out::println);
	}

	/**
	 * Determine the name of the Geo-Fence entry for the given {@link Supercharger}.
	 * @param supercharger a supercharger
	 * @return the name to use for that supercharger
	 */
	private String determineName(Supercharger supercharger) {
		return "Supercharger " + supercharger.getAddress().getCity();
	}

	/**
	 * Determine the charging fee per kWh (taxes included) for the given
	 * {@link Supercharger}.
	 * @param supercharger a supercharger
	 * @return the charging fee to use for that supercharger
	 */
	private Double determineFeeKwh(Supercharger supercharger) {
		return 0.2975d;
	}

	// Online
	private Flux<Supercharger> getAllSuperchargers(SuperchargeWebClient client) {
		return client.getAllSuperchargers();
	}

	// Offline
	private Flux<Supercharger> getAllSuperchargers(ObjectMapper mapper) throws IOException {
		Stream<Supercharger> stream = Arrays.stream(
				mapper.readValue(new ClassPathResource("all-superchargers.json").getURL(), Supercharger[].class));
		return Flux.fromStream(stream);
	}

}
