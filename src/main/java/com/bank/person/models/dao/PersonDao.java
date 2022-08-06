package com.bank.person.models.dao;

import com.bank.person.models.documents.Person;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PersonDao extends ReactiveMongoRepository<Person, String> {

}
