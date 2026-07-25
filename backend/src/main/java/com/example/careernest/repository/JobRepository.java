package com.example.careernest.repository;

import com.example.careernest.document.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

// the search query is written in JobService, this file has the simple ones
public interface JobRepository extends MongoRepository<Job, String> {

    Page<Job> findByEmployerId(String employerId, Pageable pageable);
}
