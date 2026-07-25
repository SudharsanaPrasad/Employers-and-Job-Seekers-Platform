package com.example.careernest.document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;

@Document(collection = "jobs")
@Getter
@Setter
@NoArgsConstructor
public class Job {

    @Id
    private String id;

    @Indexed
    private String title;

    private String description;

    // used to search jobs by location
    @Indexed
    private String location;

    // salary is a text like 50000 or 50k-70k
    private String salary;

    private LocalDate deadline;

    // the employer who posted this job
    @Indexed
    private String employerId;

    // employer name so we don't have to load the user again
    private String employerName;

    private Instant createdAt = Instant.now();
}
