package com.example.careernest.document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    private String id;

    private String name;

    // email should not repeat
    @Indexed(unique = true)
    private String email;

    private String password;

    // phone number for sms, like +9198...
    private String phone;

    private Role role;
}
