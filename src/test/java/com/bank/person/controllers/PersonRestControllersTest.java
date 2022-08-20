package com.bank.person.controllers;

import java.util.ArrayList;
import java.util.List;

import com.bank.person.models.dao.PersonDao;
import com.bank.person.models.documents.Person;
import com.bank.person.models.enums.PersonGenre;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.times;

@WebFluxTest
public class PersonRestControllersTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    PersonDao dao;

    @Test
    void create() {
        Person person = new Person();
        person.setId("1");
        person.setFirstName("Mary");
        person.setLastName("Tito");
        person.setGenre(PersonGenre.FEMALE);
        person.setDocumentId("75007684");

        Mono<Person> personMono = Mono.just(person);
        Mockito.when(dao.save(person)).thenReturn(personMono);

        webTestClient.post()
                .uri("/api/person")
                .contentType(MediaType.APPLICATION_JSON)
                .body(personMono, Person.class)
                .exchange()
                .expectStatus()
                .isOk();

        Mockito.verify(dao, times(1)).save(person);
    }

    @Test
    void findAll() {
        Person person = new Person();
        person.setId("1");
        person.setFirstName("Mary");
        person.setLastName("Tito");
        person.setGenre(PersonGenre.FEMALE);
        person.setDocumentId("75007684");
        List<Person> list = new ArrayList<Person>();
        list.add(person);
        Flux<Person> personFlux = Flux.fromIterable(list);

        Mockito.when(dao.findAll()).thenReturn(personFlux);

        webTestClient.get().uri("/api/person")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Person.class)
                .returnResult();

        Mockito.verify(dao, times(1)).findAll();
    }

    @Test
    void find() {
        Person person = new Person();
        person.setId("1");
        person.setFirstName("Mary");
        person.setLastName("Tito");
        person.setGenre(PersonGenre.FEMALE);
        person.setDocumentId("75007684");

        Mono<Person> personMono = Mono.just(person);
        Mockito.when(dao.findById("1")).thenReturn(personMono);

        webTestClient.get().uri("/api/person/{id}","1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Person.class)
                .returnResult();

        Mockito.verify(dao, times(1)).findById("1");
    }

    @Test
    void update() {

    }

    @Test
    void delete() {

    }
}