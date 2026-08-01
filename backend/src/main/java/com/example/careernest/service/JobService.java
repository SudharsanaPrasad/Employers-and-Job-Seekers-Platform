package com.example.careernest.service;

import com.example.careernest.document.Job;
import com.example.careernest.document.User;
import com.example.careernest.dto.JobRequest;
import com.example.careernest.dto.JobResponse;
import com.example.careernest.exception.ResourceNotFoundException;
import com.example.careernest.repository.ApplicationRepository;
import com.example.careernest.repository.JobRepository;
import com.example.careernest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    public JobResponse create(String employerId, JobRequest request) {
        User employer = userRepository.findById(employerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", employerId));

        Job job = new Job();
        apply(job, request);
        job.setEmployerId(employerId);
        job.setEmployerName(employer.getName());
        job.setCreatedAt(Instant.now());
        return toResponse(jobRepository.save(job));
    }

    public JobResponse update(String employerId, String jobId, JobRequest request) {
        Job job = getOwnedJob(employerId, jobId);
        apply(job, request);
        return toResponse(jobRepository.save(job));
    }

    public void delete(String employerId, String jobId) {
        Job job = getOwnedJob(employerId, jobId);
        // remove the applications to this job first so none are left orphaned
        applicationRepository.deleteByJobId(jobId);
        jobRepository.delete(job);
    }

    public JobResponse findById(String jobId) {
        return toResponse(getJob(jobId));
    }

    public Page<JobResponse> findMyJobs(String employerId, Pageable pageable) {
        return jobRepository.findByEmployerId(employerId, pageable).map(this::toResponse);
    }

    // browse/search with optional location and keyword filters. Keyword matches the
    // title or the description, case-insensitive.
    public Page<JobResponse> search(String location, String keyword, Pageable pageable) {
        Query query = new Query();
        if (location != null && !location.isBlank()) {
            query.addCriteria(Criteria.where("location").regex(location.trim(), "i"));
        }
        if (keyword != null && !keyword.isBlank()) {
            String kw = keyword.trim();
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("title").regex(kw, "i"),
                    Criteria.where("description").regex(kw, "i")));
        }

        long total = mongoTemplate.count(query, Job.class);
        query.with(pageable);
        List<JobResponse> jobs = mongoTemplate.find(query, Job.class).stream()
                .map(this::toResponse)
                .toList();
        return new PageImpl<>(jobs, pageable, total);
    }

    // loads a job and checks the caller owns it (used by update/delete)
    private Job getOwnedJob(String employerId, String jobId) {
        Job job = getJob(jobId);
        if (!job.getEmployerId().equals(employerId)) {
            throw new AccessDeniedException("You do not own this job posting");
        }
        return job;
    }

    private Job getJob(String jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job", jobId));
    }

    private void apply(Job job, JobRequest request) {
        job.setTitle(request.title());
        job.setDescription(request.description());
        job.setLocation(request.location());
        job.setSalary(request.salary());
        job.setDeadline(request.deadline());
    }

    private JobResponse toResponse(Job job) {
        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getLocation(),
                job.getSalary(),
                job.getDeadline(),
                job.getEmployerId(),
                job.getEmployerName(),
                job.getCreatedAt());
    }
}
