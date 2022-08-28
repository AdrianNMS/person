package com.bank.person.models.services;

import com.bank.person.models.documents.Person;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IPersonService {
    Mono<List<Person>> FindAll();
    Mono<Person> Find(String id);
    Mono<Person> Create(Person person);
    Mono<Person> Update(String id, Person person);
    Mono<Object> Delete(String id);
}
