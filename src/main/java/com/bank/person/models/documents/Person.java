package com.bank.person.models.documents;

import com.bank.person.models.enums.PersonGenre;
import com.bank.person.models.utils.Audit;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "persons")
public class Person extends Audit {

    @Id
    private String id;
    private String firstname;
    private String lastName;
    private PersonGenre genre;
    private String documentId;
    private String phoneNumber;
    private String email;
}
