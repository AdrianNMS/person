package com.bank.person.controllers.models;

import com.bank.person.models.documents.Person;
import lombok.Data;

import java.util.List;

@Data
public class ResponsePersonFindAll
{
    private List<Person> data;

    private String message;

    private String status;
}
