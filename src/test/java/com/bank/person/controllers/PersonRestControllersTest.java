package com.bank.person.controllers;

import com.bank.person.controllers.models.ResponsePerson;
import com.bank.person.models.documents.Person;
import com.bank.person.models.enums.PersonGenre;
import com.bank.person.models.services.impl.PersonService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@WebFluxTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonRestControllersTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    @Autowired
    private PersonService personService;

    @MockBean
    private Person person;
    @BeforeEach
    public void setupMock()
    {
        person = Person.builder()
                .id("1")
                .firstName("Mary")
                .lastName("Tito")
                .genre(PersonGenre.FEMALE)
                .documentId("75007684")
                .phoneNumber("965000000")
                .email("test@gmail.com")
                .build();

        var personFlux = Flux.just(person);
        var personMono = Mono.just(person);

        Mockito.when(personService.FindAll())
                .thenReturn(personFlux.collectList());
        Mockito.when(personService.Find("1"))
                .thenReturn(personMono);
        Mockito.when(personService.Create(person))
                .thenReturn(personMono);
        Mockito.when(personService.Update("1",person))
                .thenReturn(personMono);
        Mockito.when(personService.Delete("1"))
                .thenReturn(Mono.just(true));
    }

    @Test
    void create() {
        webTestClient.post()
                .uri("/api/person")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(person), Person.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<ResponsePerson<Person>>(){})
                .value(responsePerson -> {
                    var personR = responsePerson.getData();
                    Assertions.assertThat(personR.getId()).isEqualTo("1");
                });

    }

    @Test
    void find() {
        webTestClient.get().uri("/api/person/{id}","1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<ResponsePerson<Person>>(){})
                .value(responsePerson -> {
                    var personR = responsePerson.getData();
                    Assertions.assertThat(personR.getId()).isEqualTo("1");
                });
    }

    @Test
    void findAll() {
        webTestClient.get().uri("/api/person")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<ResponsePerson<List<Person>>>(){})
                .value(responsePerson -> {
                    var personList = responsePerson.getData();
                    personList.forEach(person1 -> {
                        Assertions.assertThat(person1.getId()).isEqualTo("1");
                    });
                });

    }

    @Test
    void update() {
        webTestClient.put()
                .uri("/api/person/{id}","1")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(person), Person.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<ResponsePerson<Person>>(){})
                .value(responsePerson -> {
                    var personR = responsePerson.getData();
                    Assertions.assertThat(personR.getId()).isEqualTo("1");
                    Assertions.assertThat(personR.getFirstName()).isEqualTo("Mary");
                });


    }

    @Test
    void delete() {
        webTestClient.delete().uri("/api/person/{id}","1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<ResponsePerson<Person>>(){})
                .value(responsePerson -> {
                    Assertions.assertThat(responsePerson.getStatus()).isEqualTo("OK");
                });

    }
}