package com.example.careernest.document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

// one application per (job, seeker). The unique index stops a seeker applying to
// the same job twice, even under a race.
@Document(collection = "applications")
@CompoundIndex(name = "uk_job_seeker", def = "{'jobId': 1, 'seekerId': 1}", unique = true)
@Getter
@Setter
@NoArgsConstructor
public class Application {

    @Id
    private String id;

    @Indexed
    private String jobId;

    private String jobTitle;

    @Indexed
    private String seekerId;

    private String seekerName;

    // the employer who owns the job; indexed so an employer can list all
    // applications across their postings
    @Indexed
    private String employerId;

    private ApplicationStatus status = ApplicationStatus.APPLIED;

    // optional note the seeker sends with the application
    private String coverLetter;

    private Instant appliedAt = Instant.now();
}
