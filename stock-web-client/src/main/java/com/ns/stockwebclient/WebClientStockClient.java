package com.ns.stockwebclient;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.IOException;

@Log4j2
public class WebClientStockClient  {
    private final WebClient webClient;

    public WebClientStockClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<StockPrice> pricesFor(String symbol) {
        return webClient.get()
                        .uri("http://localhost:8081/stocks/{symbol}", symbol)
                        .retrieve()
                        .bodyToFlux(StockPrice.class)
                        .doOnError(IOException.class, e -> log.error(e.getMessage()));
    }
}
