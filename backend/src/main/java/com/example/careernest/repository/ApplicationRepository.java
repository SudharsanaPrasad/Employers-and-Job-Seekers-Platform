package com.example.careernest.repository;

import com.example.careernest.document.Application;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ApplicationRepository extends MongoRepository<Application, String> {

    // a seeker's own applications, newest first
    List<Application> findBySeekerIdOrderByAppliedAtDesc(String seekerId);

    // applications for a single job posting
    List<Application> findByJobIdOrderByAppliedAtDesc(String jobId);

    // every application across an employer's postings
    List<Application> findByEmployerIdOrderByAppliedAtDesc(String employerId);

    // dedupe guard: has this seeker already applied to this job?
    boolean existsByJobIdAndSeekerId(String jobId, String seekerId);

    // remove a job's applications when the job itself is deleted (no orphans)
    void deleteByJobId(String jobId);
}
