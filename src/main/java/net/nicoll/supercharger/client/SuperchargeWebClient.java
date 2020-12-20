package net.nicoll.supercharger.client;

import net.nicoll.supercharger.Supercharger;
import reactor.core.publisher.Flux;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Retrieve known superchargers using {@code supercharge.info}.
 *
 * @author Stephane Nicoll
 */
@Component
public class SuperchargeWebClient {

	private final WebClient webClient;

	public SuperchargeWebClient(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.build();
	}

	public Flux<Supercharger> getAllSuperchargers() {
		return this.webClient.get().uri("https://supercharge.info/service/supercharge/allSites")
				.accept(MediaType.APPLICATION_JSON).retrieve().bodyToFlux(Supercharger.class);
	}

}
