package com.safetynet.safetynet_alerts.dto;

import com.safetynet.safetynet_alerts.models.Person;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonDTO {

    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private int zip;
    private String phone;
    private String email;

    public Person toPerson() {
        return new Person(
            this.firstName,
            this.lastName,
            this.address,
            this.city,
            this.zip,
            this.phone,
            this.email
        );
    }
}
