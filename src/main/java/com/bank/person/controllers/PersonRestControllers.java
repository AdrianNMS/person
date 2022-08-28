package com.bank.person.controllers;

import com.bank.person.handler.ResponseHandler;
import com.bank.person.models.documents.Person;
import com.bank.person.models.services.impl.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/person")
public class PersonRestControllers {

    @Autowired
    private PersonService personService;

    @Autowired
    private ReactiveRedisTemplate<String, Person> redisTemplate;
    private static final Logger log = LoggerFactory.getLogger(PersonRestControllers.class);

    /*@PostMapping("/redis")
    public Mono<Boolean> put(@RequestBody Person per) {

        return redisTemplate.opsForValue().set("111", per);
    }*/



    @PostMapping
    public Mono<ResponseEntity<Object>> Create(@Validated @RequestBody Person p) {
        return personService.Create(p)
                .doOnNext(person -> log.info(person.toString()))
                .map(person -> ResponseHandler.response("Done", HttpStatus.OK, person))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)));
    }

    @GetMapping
    public Mono<ResponseEntity<Object>> FindAll() {
        return personService.FindAll()
                .doOnNext(person -> log.info(person.toString()))
                .map(persons -> ResponseHandler.response("Done", HttpStatus.OK, persons))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)));

    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Object>> Find(@PathVariable String id) {
        return personService.Find(id)
                .doOnNext(person -> log.info(person.toString()))
                .map(person -> ResponseHandler.response("Done", HttpStatus.OK, person))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Object>> Update(@PathVariable("id") String id,@Validated @RequestBody Person p) {
        return personService.Update(id,p)
                .flatMap(debitCard -> Mono.just(ResponseHandler.response("Done", HttpStatus.OK, debitCard)))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .switchIfEmpty(Mono.just(ResponseHandler.response("Empty", HttpStatus.NO_CONTENT, null)));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> Delete(@PathVariable("id") String id) {
        return personService.Delete(id)
                .flatMap(o -> Mono.just(ResponseHandler.response("Done", HttpStatus.OK, null)))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .switchIfEmpty(Mono.just(ResponseHandler.response("Error", HttpStatus.NO_CONTENT, null)));
    }
}
