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

    // one account per email; the unique index enforces it at the database level
    @Indexed(unique = true)
    private String email;

    private String password;

    // phone in E.164 form (e.g. +9198...) - used for Twilio SMS notifications
    private String phone;

    private Role role;
}
