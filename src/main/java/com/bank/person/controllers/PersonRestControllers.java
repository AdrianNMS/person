package com.bank.person.controllers;

import com.bank.person.handler.ResponseHandler;
import com.bank.person.models.documents.Person;
import com.bank.person.models.services.impl.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final Logger log = LoggerFactory.getLogger(PersonRestControllers.class);

    @PostMapping
    public Mono<ResponseEntity<Object>> create(@Validated @RequestBody Person p) {
        return personService.create(p)
                .doOnNext(person -> log.info(person.toString()))
                .flatMap(person -> Mono.just(ResponseHandler.response("Done", HttpStatus.OK, person)))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)));
    }

    @GetMapping
    public Mono<ResponseEntity<Object>> findAll() {
        return personService.findAll()
                .doOnNext(person -> log.info(person.toString()))
                .flatMap(persons -> Mono.just(ResponseHandler.response("Done", HttpStatus.OK, persons)))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)));

    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Object>> find(@PathVariable String id) {
        return personService.find(id)
                .doOnNext(person -> log.info(person.toString()))
                .flatMap(person -> Mono.just(ResponseHandler.response("Done", HttpStatus.OK, person)))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Object>> update(@PathVariable("id") String id,@Validated @RequestBody Person p) {
        return personService.update(id,p)
                .flatMap(debitCard -> Mono.just(ResponseHandler.response("Done", HttpStatus.OK, debitCard)))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .switchIfEmpty(Mono.just(ResponseHandler.response("Empty", HttpStatus.NO_CONTENT, null)));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> delete(@PathVariable("id") String id) {
        return personService.delete(id)
                .flatMap(o -> Mono.just(ResponseHandler.response("Done", HttpStatus.OK, null)))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .switchIfEmpty(Mono.just(ResponseHandler.response("Error", HttpStatus.NO_CONTENT, null)));
    }
}
