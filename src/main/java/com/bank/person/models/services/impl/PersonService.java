package com.bank.person.models.services.impl;

import com.bank.person.models.dao.PersonDao;
import com.bank.person.models.documents.Person;
import com.bank.person.models.services.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class PersonService implements IPersonService {
    @Autowired
    private PersonDao dao;

    @Autowired
    private ReactiveRedisTemplate<String, Person> redisTemplate;

    @Override
    public Mono<List<Person>> FindAll() {

        return dao.findAll().map(person -> {
                    person.setFirstName(person.getFirstName().toUpperCase());
                    person.setLastName(person.getLastName().toUpperCase());
                    return person;
                })
                .collectList();
    }
    @Override
    public Mono<Person> Find(String id) {
        return redisTemplate.opsForValue().get(id)
                .switchIfEmpty(dao.findById(id)
                        .doOnNext(per -> redisTemplate.opsForValue()
                                .set(per.getId(), per)
                                .subscribe(aBoolean -> {
                                    redisTemplate.expire(id, Duration.ofMinutes(1)).subscribe();
                                })));
    }

    @Override
    public Mono<Person> Create(Person person) {
        person.setCreatedDate(LocalDateTime.now());

        return dao.save(person)
                .doOnNext(per -> redisTemplate.opsForValue()
                .set(per.getId(), per)
                .subscribe(aBoolean -> {
                    redisTemplate.expire(per.getId(), Duration.ofMinutes(1)).subscribe();
                }));
    }

    @Override
    public Mono<Person> Update(String id, Person person) {
        return dao.existsById(id).flatMap(check ->
        {
            if (check)
            {
                redisTemplate.opsForValue().delete(id).subscribe();
                return dao.save(person)
                        .doOnNext(per -> redisTemplate.opsForValue()
                                .set(per.getId(), per)
                                .subscribe(aBoolean -> {
                                    redisTemplate.expire(id, Duration.ofMinutes(1));
                                }));
            }
            else
                return Mono.empty();

        });
    }

    @Override
    public Mono<Object> Delete(String id) {
        return dao.existsById(id).flatMap(check -> {
            if (check)
            {
                redisTemplate.opsForValue().delete(id).subscribe();
                return dao.deleteById(id).then(Mono.just(true));
            }
            else
                return Mono.empty();
        });
    }
}
