package com.bank.person.services.impl;

import com.bank.person.models.dao.PersonDao;
import com.bank.person.models.documents.Person;
import com.bank.person.services.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PersonService implements IPersonService {
    @Autowired
    private PersonDao dao;

    @Override
    public Mono<List<Person>> FindAll() {
        return dao.findAll().map(person -> {
                                    person.setFirstName(person.getFirstName().toUpperCase());
                                    person.setLastName(person.getLastName().toUpperCase());
                                    return person;
                    })
                .collectList();
    }
    //@Cacheable(value = "persons")
    @Override
    public Mono<Person> Find(String id) {
        return dao.findById(id);
    }

    @Override
    public Mono<Person> Create(Person person) {
        person.setCreatedDate(LocalDateTime.now());
        return dao.save(person);
    }

    @Override
    public Mono<Person> Update(String id, Person person) {
        return dao.existsById(id).flatMap(check ->
        {
            if (check)
            {
                return dao.save(person);
            }
            else
                return Mono.empty();

        });
    }

    @Override
    public Mono<Object> Delete(String id) {
        return dao.existsById(id).flatMap(check -> {
            if (check)
                return dao.deleteById(id).then(Mono.just(true));
            else
                return Mono.empty();
        });
    }
}
