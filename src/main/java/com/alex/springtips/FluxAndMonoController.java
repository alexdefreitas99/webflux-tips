package com.alex.springtips;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class FluxAndMonoController {
    @GetMapping("/flux")
    public Flux<Integer> getFlux() {
        return Flux.just(1, 2, 3, 4)
                .delayElements(Duration.ofSeconds(5))
                .log("Buscando dados");
    }

    @GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Long> getFluxStream(){
        return Flux.interval(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping(value = "/enum")
    public Mono<OverdraftCancelSituation> aa() {
        return Mono.just(OverdraftCancelSituation.PENDING_PAYMENTS);
    }

    @PostMapping(value = "/test-nome")
    public Mono<TestSpringSerialization> nome(@RequestBody TestSpringSerialization testSpringSerialization) {
        return Mono.just(testSpringSerialization);
    }

    @GetMapping(value = "/getDate")
    public Mono<MinhaDataCla> getDate() {
        return Mono.just(new MinhaDataCla());
    }

    public static class MinhaDataCla {
        private LocalDateTime minhaDataTime = LocalDateTime.now();
        private LocalDate minhaDataSemTime = LocalDate.now();
        private Double CET = 1.0;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        public LocalDateTime getMinhaDataTime() {
            return minhaDataTime;
        }

        public void setMinhaDataTime(LocalDateTime minhaDataTime) {
            this.minhaDataTime = minhaDataTime;
        }

        public LocalDate getMinhaDataSemTime() {
            return minhaDataSemTime;
        }

        public void setMinhaDataSemTime(LocalDate minhaDataSemTime) {
            this.minhaDataSemTime = minhaDataSemTime;
        }

        @JsonProperty("CET")
        public Double getCET() {
            return CET;
        }

        public void setCET(Double CET) {
            this.CET = CET;
        }

        @Override
        public String toString() {
            return "MinhaDataCla{" +
                    "minhaDataTime=" + minhaDataTime +
                    ", minhaDataSemTime=" + minhaDataSemTime +
                    ", CET='" + CET + '\'' +
                    '}';
        }
    }
}
