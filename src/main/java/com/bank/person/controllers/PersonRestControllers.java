package com.bank.person.controllers;

import com.bank.person.handler.ResponseHandler;
import com.bank.person.models.dao.PersonDao;
import com.bank.person.models.documents.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/person")
public class PersonRestControllers {

    @Autowired
    private PersonDao dao;

    private static final Logger log = LoggerFactory.getLogger(PersonRestControllers.class);

    @PostMapping
    public Mono<ResponseEntity<Object>> Create(@Validated @RequestBody Person p) {
        p.setCreatedDate(LocalDateTime.now());
        return dao.save(p)
                .doOnNext(person -> log.info(person.toString()))
                .map(person -> ResponseHandler.response("Done", HttpStatus.OK, person))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)));
    }

    @GetMapping
    public Mono<ResponseEntity<Object>> FindAll() {

        return dao.findAll().map(person -> {
                    person.setFirstName(person.getFirstName().toUpperCase());
                    person.setLastName(person.getLastName().toUpperCase());
                    return person;
                })
                .doOnNext(person -> log.info(person.toString()))
                .collectList().map(persons -> ResponseHandler.response("Done", HttpStatus.OK, persons))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)));

    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Object>> Find(@PathVariable String id) {
        return dao.findById(id)
                .doOnNext(person -> log.info(person.toString()))
                .map(person -> ResponseHandler.response("Done", HttpStatus.OK, person))
                .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Object>> Update(@PathVariable("id") String id,@Validated @RequestBody Person p) {
        return dao.existsById(id).flatMap(check -> {
            if (check){
                p.setUpdateDate(LocalDateTime.now());
                return dao.save(p)
                        .doOnNext(person -> log.info(person.toString()))
                        .map(person -> ResponseHandler.response("Done", HttpStatus.OK, person))
                        .onErrorResume(error -> Mono.just(ResponseHandler.response(error.getMessage(), HttpStatus.BAD_REQUEST, null)));
            }
            else
                return Mono.just(ResponseHandler.response("Not found", HttpStatus.NOT_FOUND, null));

        });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> Delete(@PathVariable("id") String id) {

        return dao.existsById(id).flatMap(check -> {
            if (check)
                return dao.deleteById(id).then(Mono.just(ResponseHandler.response("Done", HttpStatus.OK, null)));
            else
                return Mono.just(ResponseHandler.response("Not found", HttpStatus.NOT_FOUND, null));
        });
    }
}
