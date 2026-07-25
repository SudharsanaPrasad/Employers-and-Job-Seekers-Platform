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

    // indexed because job seekers filter by location
    @Indexed
    private String location;

    // free text so a listing can hold a single figure or a range ("60000", "50k-70k")
    private String salary;

    private LocalDate deadline;

    // the employer who owns this listing; indexed for "my jobs"
    @Indexed
    private String employerId;

    // denormalised for display so a listing does not need a second lookup
    private String employerName;

    private Instant createdAt = Instant.now();
}
