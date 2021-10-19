package com.alex.springtips;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

class HotAndColdPublisher {

    @Test
    void hotPublisher() throws InterruptedException {
        Flux<String> flux = Flux.just("Julie", "Joe", "Simon", "Bob");

        final ConnectableFlux<String> cflux = flux.delayElements(Duration.ofMillis(500)).publish();

        cflux.subscribe(v -> System.out.println("First receveid " + v), Throwable::printStackTrace);

        cflux.connect();

        Thread.sleep(1000);

        cflux.subscribe(v -> System.out.println("Second receveid " + v), Throwable::printStackTrace);

        Thread.sleep(6000);
    }

    @Test
    void coldPublisherTest() throws InterruptedException {
        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F").delayElements(Duration.ofSeconds(1));
        stringFlux.subscribe(s -> System.out.println("Subscribe 1 " + s));
        Thread.sleep(2000);
        stringFlux.subscribe(s -> System.out.println("Subscribe 2 " + s)); //Emit values from beginning
        Thread.sleep(6000);
    }

    @Test
    void hotPublisherTest() throws InterruptedException {
        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F").delayElements(Duration.ofSeconds(1));
        ConnectableFlux<String> connectableFlux = stringFlux.publish();
        connectableFlux.connect();
        connectableFlux.subscribe(s -> System.out.println("Subscribe 1 " + s));
        Thread.sleep(3000);
        connectableFlux.subscribe(s -> System.out.println("Subscribe 2 " + s)); //Don't emit values from beginning
        Thread.sleep(4000);
    }
}
