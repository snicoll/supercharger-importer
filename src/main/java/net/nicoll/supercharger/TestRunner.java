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
 * @author Stephane Nicoll
 */
@Component
public class TestRunner implements ApplicationRunner {

	private final ObjectMapper mapper;

	private final SuperchargeWebClient client;

	public TestRunner(ObjectMapper mapper, SuperchargeWebClient client) {
		this.mapper = mapper;
		this.client = client;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		TeslamateQueryBuilder queryBuilder = new TeslamateQueryBuilder((supercharger) -> 0.3388d);
		getAllSuperchargers(this.mapper)
				.filter(SuperchargerPredicates.available().and(SuperchargerPredicates.country("Belgium"))).toStream()
				.map(queryBuilder::apply).forEach(System.out::println);
	}

	private Flux<Supercharger> getAllSuperchargers(SuperchargeWebClient client) {
		return client.getAllSuperchargers();
	}

	private Flux<Supercharger> getAllSuperchargers(ObjectMapper mapper) throws IOException {
		Stream<Supercharger> stream = Arrays.stream(
				mapper.readValue(new ClassPathResource("all-superchargers.json").getURL(), Supercharger[].class));
		return Flux.fromStream(stream);
	}

}
