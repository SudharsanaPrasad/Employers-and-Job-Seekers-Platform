package com.example.careernest.repository;

import com.example.careernest.document.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

// keyword/location search is built with MongoTemplate in JobService (flexible
// optional filters); this interface covers the plain finders.
public interface JobRepository extends MongoRepository<Job, String> {

    Page<Job> findByEmployerId(String employerId, Pageable pageable);
}
