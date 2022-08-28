package com.bank.person.models.services;

import com.bank.person.models.documents.Person;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IPersonService {
    Mono<List<Person>> findAll();
    Mono<Person> find(String id);
    Mono<Person> create(Person person);
    Mono<Person> update(String id, Person person);
    Mono<Object> delete(String id);
}
