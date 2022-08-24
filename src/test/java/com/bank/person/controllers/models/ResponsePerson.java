package com.bank.person.controllers.models;

import lombok.Data;

@Data
public class ResponsePerson <T>
{
    private T data;

    private String message;

    private String status;
}
