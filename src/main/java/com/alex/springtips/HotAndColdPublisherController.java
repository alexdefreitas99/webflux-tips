package com.alex.springtips;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class HotAndColdPublisherController {
    @GetMapping("/hot")
    public Flux<String> hotPublisher() throws InterruptedException {
//        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F").delayElements(Duration.ofSeconds(1));
//        stringFlux.subscribe(s -> System.out.println("Subscribe 1 " + s ));
//        Thread.sleep(2000);
//        stringFlux.subscribe(s -> System.out.println("Subscribe 2 " + s )); //Emit values from beginning
//        Thread.sleep(6000);


        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F").delayElements(Duration.ofSeconds(1));
        ConnectableFlux<String> connectableFlux = stringFlux.publish();
        connectableFlux.connect();
        return connectableFlux.flatMap(i -> stringFlux.doOnNext(s -> System.out.println("Subscribe 1 " + s)))
                .doOnNext(i -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                })
                .flatMap(i -> stringFlux.doOnNext(s -> System.out.println("Subscribe 2 " + s)))
                .doOnNext(i -> {
                    try {
                        Thread.sleep(6000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
    }
}
