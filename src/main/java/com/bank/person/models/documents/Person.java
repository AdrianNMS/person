package com.bank.person.models.documents;

import com.bank.person.models.enums.PersonGenre;
import com.bank.person.models.utils.Audit;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Document(collection = "persons")
public class Person extends Audit {

    @Id
    private String id;
    @NotNull(message = "firstName must not be null")
    private String firstName;
    @NotNull(message = "lastName must not be null")
    private String lastName;
    @NotNull(message = "genre must not be null")
    private PersonGenre genre;
    private String documentId;
    private String phoneNumber;
    private String email;
}
