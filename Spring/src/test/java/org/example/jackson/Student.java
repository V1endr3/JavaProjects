package org.example.jackson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Student {

    public String firstName;

    public String lastName;

    public transient String fullName;

    public Integer age;

    public Character gender;

}
