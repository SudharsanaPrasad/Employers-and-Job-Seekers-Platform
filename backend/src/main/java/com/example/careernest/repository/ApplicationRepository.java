package com.example.careernest.repository;

import com.example.careernest.document.Application;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ApplicationRepository extends MongoRepository<Application, String> {

    // get all applications of one seeker
    List<Application> findBySeekerIdOrderByAppliedAtDesc(String seekerId);

    // get the applications of one job
    List<Application> findByJobIdOrderByAppliedAtDesc(String jobId);

    // get all applications of one employer
    List<Application> findByEmployerIdOrderByAppliedAtDesc(String employerId);

    // check if the seeker already applied to this job
    boolean existsByJobIdAndSeekerId(String jobId, String seekerId);

    // delete the applications when the job is deleted
    void deleteByJobId(String jobId);
}
