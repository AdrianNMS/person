package com.bank.person.controllers;

import java.util.ArrayList;
import java.util.List;

import com.bank.person.controllers.models.ResponsePerson;
import com.bank.person.controllers.models.ResponsePersonFindAll;
import com.bank.person.models.dao.PersonDao;
import com.bank.person.models.documents.Person;
import com.bank.person.models.enums.PersonGenre;
import org.assertj.core.api.Assertions;
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
    void create()
    {
        Person person = new Person();
        person.setId("1");
        person.setFirstName("Mary");
        person.setLastName("Tito");
        person.setGenre(PersonGenre.FEMALE);
        person.setDocumentId("75007684");

        var personMono = Mono.just(person);

        Mockito.when(dao.save(person)).thenReturn(personMono);

        webTestClient.post()
                .uri("/api/person")
                .contentType(MediaType.APPLICATION_JSON)
                .body(personMono, Person.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ResponsePerson.class)
                .value(responsePerson -> {
                    var personR = responsePerson.getData();
                    Assertions.assertThat(personR.getId()).isEqualTo("1");
                });

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
                .expectBody(ResponsePerson.class)
                .value(responsePerson -> {
                    var personR = responsePerson.getData();
                    Assertions.assertThat(personR.getId()).isEqualTo("1");
                });
    }

    @Test
    void findAll() {
        Person person = new Person();
        person.setId("1");
        person.setFirstName("Mary");
        person.setLastName("Tito");
        person.setGenre(PersonGenre.FEMALE);
        person.setDocumentId("75007684");

        var list = new ArrayList<Person>();
        list.add(person);

        var personFlux = Flux.fromIterable(list);

        Mockito.when(dao.findAll()).thenReturn(personFlux);

        webTestClient.get().uri("/api/person")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ResponsePersonFindAll.class)
                .value(responsePerson -> {
                    var personList = responsePerson.getData();
                    personList.forEach(person1 -> {
                        Assertions.assertThat(person1.getId()).isEqualTo("1");
                    });
                });

    }

    @Test
    void update()
    {
        Person person = new Person();
        person.setId("1");
        person.setFirstName("Mary");
        person.setLastName("Tito");
        person.setGenre(PersonGenre.FEMALE);
        person.setDocumentId("75007684");


        Mono<Boolean> bol = Mono.just(true);
        Mockito.when(dao.existsById("1")).thenReturn(bol);

        Mono<Person> personMono = Mono.just(person);
        Mockito.when(dao.save(person)).thenReturn(personMono);

        webTestClient.put()
                .uri("/api/person/{id}","1")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(person), Person.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ResponsePerson.class)
                .value(responsePerson -> {
                    var personR = responsePerson.getData();
                    Assertions.assertThat(personR.getId()).isEqualTo("1");
                    Assertions.assertThat(personR.getFirstName()).isEqualTo("Mary");
                });


    }



    @Test
    void delete() {

        Mono<Boolean> bol = Mono.just(true);
        Mockito.when(dao.existsById("1")).thenReturn(bol);

        Mono<Void> empty  = Mono.empty();
        Mockito.when(dao.deleteById("1")).thenReturn(empty);

        webTestClient.delete().uri("/api/person/{id}","1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ResponsePerson.class)
                .value(responsePerson -> {
                    Assertions.assertThat(responsePerson.getStatus()).isEqualTo("OK");
                });

    }
}