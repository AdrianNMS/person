package com.bank.person.controllers.models;

import com.bank.person.models.documents.Person;
import lombok.Data;

@Data
public class ResponsePerson
{
    private Person data;

    private String message;

    private String status;
}
