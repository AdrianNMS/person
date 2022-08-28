package com.bank.person.models.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Audit {
    @CreatedDate
    @JsonIgnore
    private LocalDateTime createdDate;

    @LastModifiedDate
    @JsonIgnore
    //@JsonSerialize(using= LocalDateTimeSerializer.class)
    //@JsonDeserialize(using= LocalDateTimeDeserializer.class)
    private LocalDateTime updateDate;
}
