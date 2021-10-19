package com.alex.springtips;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Playground {

    @Test
    void testIntStream() {
        var possibleShapes = IntStream.range(1, 9).mapToObj(index -> "FRAME_SHAPE_0" + index).collect(Collectors.toList());
        possibleShapes.contains("");
        System.out.println(possibleShapes);
    }

    @Test
    void fluxConcatenation() {
        Flux.merge(Flux.empty(), Flux.interval(Duration.ofMillis(30)).zipWith(Flux.just("Mergeado interval"), (i, msg) -> msg), Flux.just("Mergeado 2", "Mergeado 3"))
                .doOnNext(System.out::println)
                .blockLast();

        Flux.interval(Duration.ofMillis(3000)).zipWith(Flux.just("Concatenado", "Concatenado 2"), (i, msg) -> msg).concatWith(Flux.just("a"))
                .doOnNext(System.out::println)
                .blockLast();
    }

    @Test
    void fluxMerge() {
        Mono.zip(Mono.just("A"), Mono.just("B"), Mono.empty().switchIfEmpty(Mono.just("C")))
                .doOnNext(System.out::println)
                .block();
        var b = Flux.just("a", "b", "v")
                .filter(i -> !i.equals("b"));

        var dois = Flux.just("2", "3", "5")
                .filter(i -> !i.equals("2"));

        Flux.merge(b, dois)
                .doOnNext(System.out::println)
                .subscribe();
    }

    @Test
    void a() {
        var data = LocalDateTime.parse("2020-07-06T12:17:02.332", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
        var data1 = LocalDateTime.parse("", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"));
        System.out.println(data);
        System.out.println(data1);
    }

    @Test
    void justWithThen() {
        Mono.just("Primeiro just")
                .flatMap(i -> Mono.error(new Exception("Errouuu")))
                .then(Mono.just("Segundo just do then"))
                .doOnNext(System.out::println)
                .subscribe();
    }

    @Test
    void bigDecimalAccuracy() {
        var b = new BigDecimal(1.0 / 30.0);
        var a = BigDecimal.valueOf(1.0 / 30.0);

        BigDecimal bd1 = new BigDecimal(0.01);
        BigDecimal bd2 = new BigDecimal("0.01");
        System.out.println("bd1 = " + a);
        System.out.println("bd2 = " + b);
        BigDecimal.valueOf(1);
    }

    @Test
    void append() {
        System.out.println(new StringBuilder().append(1).append("A Polícia Federal atua como orgão de trabalho judiciario e atua nas esferaas de todos os ramos da união, tem como principal objetivo previnir e combater crimes como tráfico de drogas e assaltado a bancos").append("cccccccc"));
        System.out.println(new StringBuilder().append(1).append("a").append("c"));
    }

    @Test
    void modelWrapper_PropertiesCopy() {
        var model = new Model();
        model.setName("Alex");
        var map = Map.of("name", "José", "age", 25);

        var model2 = new ObjectMapper().convertValue(map, Model.class);
//         new ObjectMapper().
        System.out.println("Model before copyProperties " + model);
        System.out.println("Map before copyProperties " + map);

        System.out.println("Copying properties");
        BeanWrapper modelBeanWrapper = new BeanWrapperImpl(model);
        modelBeanWrapper.setPropertyValues(map);


//         beanWrapper.setPropertyValues();
        BeanUtils.copyProperties(map, model);
        ReflectionUtils.shallowCopyFieldState(model2, model);
//         BeanUtils.copyProperties();
//         BeanWrapperImpl.

        System.out.println("Properties copied");

        System.out.println("Map after copyProperties " + map);
        System.out.println("Model after copyProperties " + model);
    }

    @Test
    void localDateTimeVsInstant() {
        System.out.println(Instant.now());
        System.out.println(LocalDateTime.now());
    }

    @Test
    void arrayOfEnumVsEnumInOrOperator() {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {

//            if(List.of("ENUM1", "ENUM2").contains("ENUM1")) // It's slower than the form below
            if ("ENUM1".equals("ENUM1") || "ENUM2".equals("ENUM1")) {
                if (i == 0) {
                    System.out.println("Começou!");
                }
                if (i == 5000) {
                    System.out.println("Chegou em 5 mil");
                }

                if (i == 9999) {
                    System.out.println("Terminou!");
                }
            }
        }

        long endTime = System.currentTimeMillis();

        System.out.println("That took " + (endTime - startTime) + " milliseconds");
    }

    @Test
    void testReactorExceptions() {
        Flux.just("Proposta 1", "Proposta 2")
                .flatMap(i -> Mono.just("Cancelamento")
                        .flatMap(u -> {
                            if ("Proposta 1".equals(i)) {
                                return Mono.error(new Exception());
                            }
                            return Mono.just("Cancelado!");
                        })
                )
                .flatMap(i -> Mono.just("Simulação"))
                .flatMap(i -> Mono.just("Contratação"))
                .doOnNext(i -> System.out.println("Feito teste 1"))
                .blockLast()
        ;

        Flux.just("Proposta 1", "Proposta 2")
                .flatMap(i -> Mono.just("Cancelamento"))
                .flatMap(i -> Mono.just("Simulação").flatMap(u -> Mono.error(new Exception())))
                .flatMap(i -> Mono.just("Contratação"))
                .doOnNext(i -> System.out.println("Feito teste 2"))
                .blockLast()
        ;

        Flux.just("Proposta 1", "Proposta 2")
                .flatMap(i -> Mono.just("Cancelamento"))
                .flatMap(i -> Mono.just("Simulação"))
                .flatMap(i -> Mono.just("Contratação").flatMap(u -> Mono.error(new Exception())))
                .doOnNext(i -> System.out.println("Feito teste 3"))
                .blockLast()
        ;

        Flux.just("Proposta 1", "Proposta 2")
                .flatMap(i -> Mono.just("Cancelamento"))
                .flatMap(i -> Mono.just("Simulação"))
                .flatMap(i -> Mono.just("Contratação"))
                .doOnNext(i -> System.out.println("Feito teste 4"))
                .blockLast()
        ;
    }

    @Test
    void testDelayElementVsDelaySubscription() {
        Mono.just("Mono do delay")
                .doOnNext(i -> System.out.println(1 + i))
//                .delayElement(Duration.ofSeconds(3))
                .doOnNext(i -> System.out.println(2 + i))
                .flatMap(i -> Mono.just("Mono dps delay").doOnNext(System.out::println).delaySubscription(Duration.ofSeconds(3)))
                .flatMap(i -> Mono.just("Segundo mono").doOnNext(System.out::println))
                .block()
        ;
    }

    @Test
    void testReactorZip() {
        var proposalTopic = Mono.just(true)
//                .doOnNext(i -> System.out.println("Topico proposal"))
//                .doOnRequest(i -> System.out.println("RQ Topico proposal"))
                .doOnSuccess(i -> System.out.println("Sucess topico proposal"))
                .doOnCancel(() -> System.out.println("Proposal cancelado"))
                .then();
        var acquistionTopic = Mono.just(true)
//                .doOnNext(i -> System.out.println("Topico aquisição"))
//                .doOnRequest(i -> System.out.println("RQ Topico aquisição"))
                .doOnSuccess(i -> System.out.println("Sucess topico aquisição"))
                .doOnCancel(() -> System.out.println("Aquisition cancelado"))
                .then();

        Mono.zip(proposalTopic, acquistionTopic)
//        proposalTopic.zipWith(acquistionTopic)
                .doOnNext(System.out::println)
                .block();
    }

    @Test
    void exerciseTestReactor() {
        var org = Mono.just("Org");
        var resources = Flux.just("Resource", "Resource 2");
        var executionDay = 1;
        var recurrence = 30;

        var oneJanuary = LocalDateTime.of(2021, 1, 28, 1, 1);
        var nextExecution = oneJanuary.plusDays(recurrence).withDayOfMonth(executionDay);
        System.out.println(nextExecution);


        System.out.println(LocalDateTime.now());
        var a = LocalDateTime.now().plusDays(60).withDayOfMonth(31);
        System.out.println(a);

//        .nextExecution(model.getDate().plusDays(model.getRecurrence()).withDayOfMonth(model.getExecutionDay()))

        resources.flatMap(resource -> org.zipWith(Mono.just(resource)))
                .doOnNext(System.out::println)
                .blockLast();
    }

    @Test
    void testBigDecimalRoundingScaleAndMathContext() {
        var precision = 2;
        var roundingMode = RoundingMode.HALF_UP;

        MathContext mc = new MathContext(4, roundingMode);

        BigDecimal itemValue = BigDecimal.valueOf(136);
        BigDecimal basePercentage = BigDecimal.valueOf(100);
        BigDecimal aliquot = BigDecimal.valueOf(18);

        BigDecimal baseValue = itemValue.multiply(basePercentage).divide(BigDecimal.valueOf(100), mc);
        BigDecimal result = baseValue.multiply(aliquot).divide(BigDecimal.valueOf(100), mc);
//        baseValue.multiply(aliquot).divide()

        System.out.println("Com mathContext:" + result);


        BigDecimal baseValue02 = itemValue.multiply(basePercentage).divide(BigDecimal.valueOf(100), precision, roundingMode);
        BigDecimal result02 = baseValue02.multiply(aliquot).divide(BigDecimal.valueOf(100), precision, roundingMode);

        System.out.println("sem mathContext:" + result02);

        var a = new BigDecimal("32.000000000001").precision();
//                .round(new MathContext(6, RoundingMode.HALF_UP));

// 2.
        var b = new BigDecimal("3.53456").setScale(4, RoundingMode.HALF_UP);

        System.out.println(a);
        System.out.println(b);
    }

    @Test
    void testeGradeCurricularCC() {
        List<String> cc = List.of(GradesFacul.getCCGrade().split("\\r?\\n"));
        List<String> si = List.of(GradesFacul.getSiGrade().split("\\r?\\n"));
        List<String> ads = List.of(GradesFacul.getAdsGrade().split("\\r?\\n"));

        var ccThatExistInSi = si.stream().filter(cc::contains).collect(Collectors.toList());
        var ccThatNotExistInSi = si.stream().filter(i -> !cc.contains(i)).collect(Collectors.toList());

        System.out.println("CC that exist in si " + ccThatExistInSi);
        System.out.println("Total de matérias exatamente iguais em CC e SI " + ccThatExistInSi.size());
        System.out.println("CC that NOT exists in si " + ccThatNotExistInSi);
        System.out.println("Total de matérias exatamente diferentes em CC e SI " + ccThatNotExistInSi.size());

        System.out.println("################################################################");

        var ccThatExistInAds = ads.stream().filter(cc::contains).collect(Collectors.toList());
        var ccThatNotExistInAds = ads.stream().filter(i -> !cc.contains(i)).collect(Collectors.toList());

        System.out.println("CC that exist in ads " + ccThatExistInAds);
        System.out.println("Total de matérias exatamente iguais em CC e ADS " + ccThatExistInAds.size());
        System.out.println("CC that NOT exists in ads " + ccThatNotExistInAds);
        System.out.println("Total de matérias exatamente diferentes em CC e ADS " + ccThatNotExistInAds.size());
    }

    @Test
    void testEnumWithFlux() {
        Flux.empty().next().subscribe();
//        Flux.fromIterable(List.of("", "a", "b", "c", "j"))
//                .flatMap(i -> {
//                    switch (i) {
//                        case "a":
//                            return Flux.fromStream(Stream.of("Flux from stream 1", "Flux from stream 2"));
//                        case "b":
//                            return Flux.fromStream(Stream.of("Flux from stream 3"));
//                        case "c":
//                            return Mono.just("Moninho");
//                        default:
//                            return Mono.empty();
//                    }
//                }).collectList().doOnNext(i -> System.out.printf("Lista " + i))
//                .subscribe();
        var merge = Flux.just("testes");

        Flux.fromIterable(List.of("", "a", "b", "c", "j"))
                .flatMap(i -> {
                    switch (i) {
                        case "a":
                            merge.mergeWith(Flux.fromStream(Stream.of("Flux from stream 1", "Flux from stream 2")));
                        case "b":
                            merge.mergeWith(Flux.fromStream(Stream.of("Flux from stream 3")));
                        case "c":
                            merge.mergeWith(Mono.just("Moninho"));
                    }
                    return merge;
                })
                .collectList()
                .doOnNext(i -> System.out.printf("Lista " + i))
                .subscribe();
//        System.out.println(TestEnum.toEnum("PIX").getId());
    }

    @Test
    void streamOfWithPartition() {
        Map<Boolean, List<String>> a = Stream.of("a", "b", "b", "a", "b", "a")
                .collect(Collectors.partitioningBy(p -> p.equals("a")));

        System.out.println(a);
    }

    @Test
    void testContainsWithObject() {
        List<MinhaModelDeTeste> mm = List.of(new MinhaModelDeTeste("1234", "Alex"), new MinhaModelDeTeste("Andressa"));
        System.out.println(mm.contains(new MinhaModelDeTeste("1234", "Alex")));
    }

    @Test
    void TestThreads() {
        Mono.zip(test(1), test(2), test(3), test(4), test(5))
                .then()
                .subscribe();

        Mono.zip(test2(1), test2(2), test2(3), test2(4), test2(5))
                .then()
                .subscribe();

        test3()
                .doOnNext(i -> System.out.println("Acertoooooouuuuuuuuuuuu"))
                .doOnError(i -> System.out.println("Errouuuuuuuuuuuuuuuuu"))
                .subscribe();

    }

    @Test
    void testMonoVoid() {
        Mono.just(" Freitas").then(Mono.zip(Mono.just("Alex"), Mono.just(" Siqueira"), Mono.just(" Lindo"), Mono.just(" Maravilhoso")))
                .doOnNext(i -> System.out.println(i.getT1() + i.getT2() + i.getT3() + i.getT4()))
                .subscribe(i -> System.out.println(i.getT1() + i.getT2() + i.getT3() + i.getT4()));
    }

    private Mono<Integer> test2(Integer value) {
        return Mono.fromCallable(() -> value)
//                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(e -> System.out.println(e + " is running on thread " + Thread.currentThread().getId()));
    }

    private Mono<Integer> test(Integer value) {
        return Mono.just(value)
//                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(e -> System.out.println(e + " is running on thread " + Thread.currentThread().getId()));
    }

    //
    private Mono<Integer> test3() {
        return Mono.fromSupplier(() -> {
                    try {
                        return throwsAS();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                })
//                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(e -> System.out.println(e + " is running on thread " + Thread.currentThread().getId()));
    }

    private Integer throwsAS() throws Exception {
        throw new Exception();
    }
}
