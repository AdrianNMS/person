package com.bank.person;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PersonApplicationTest {

    @Test
    void main() {
        PersonApplication.main(new String[] {});
    }
}