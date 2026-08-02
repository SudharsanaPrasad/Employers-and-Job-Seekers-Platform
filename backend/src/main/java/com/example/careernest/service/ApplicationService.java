package com.example.careernest.service;

import com.example.careernest.document.Application;
import com.example.careernest.document.ApplicationStatus;
import com.example.careernest.document.Job;
import com.example.careernest.document.User;
import com.example.careernest.dto.ApplicationRequest;
import com.example.careernest.dto.ApplicationResponse;
import com.example.careernest.exception.BusinessRuleException;
import com.example.careernest.exception.DuplicateResourceException;
import com.example.careernest.exception.ResourceNotFoundException;
import com.example.careernest.repository.ApplicationRepository;
import com.example.careernest.repository.JobRepository;
import com.example.careernest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final SmsService smsService;

    // job seeker applies to a job
    public ApplicationResponse apply(String seekerId, ApplicationRequest request) {
        Job job = jobRepository.findById(request.jobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job", request.jobId()));

        if (applicationRepository.existsByJobIdAndSeekerId(job.getId(), seekerId)) {
            throw new DuplicateResourceException("You have already applied to this job");
        }

        User seeker = userRepository.findById(seekerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", seekerId));

        Application application = new Application();
        application.setJobId(job.getId());
        application.setJobTitle(job.getTitle());
        application.setSeekerId(seekerId);
        application.setSeekerName(seeker.getName());
        application.setEmployerId(job.getEmployerId());
        application.setStatus(ApplicationStatus.APPLIED);
        application.setCoverLetter(request.coverLetter());
        application.setAppliedAt(Instant.now());
        applicationRepository.save(application);

        // notify the seeker (confirmation) and the employer (new application)
        smsService.send(seeker.getPhone(),
                "CareerNest: your application for '" + job.getTitle() + "' has been submitted.");
        userRepository.findById(job.getEmployerId()).ifPresent(employer ->
                smsService.send(employer.getPhone(),
                        "CareerNest: " + seeker.getName() + " applied to your job '" + job.getTitle() + "'."));

        return toResponse(application);
    }

    public List<ApplicationResponse> getMyApplications(String seekerId) {
        return applicationRepository.findBySeekerIdOrderByAppliedAtDesc(seekerId).stream()
                .map(this::toResponse)
                .toList();
    }

    // every application across the employer's postings
    public List<ApplicationResponse> getEmployerApplications(String employerId) {
        return applicationRepository.findByEmployerIdOrderByAppliedAtDesc(employerId).stream()
                .map(this::toResponse)
                .toList();
    }

    // applications for one job posting (must belong to the employer)
    public List<ApplicationResponse> getJobApplications(String employerId, String jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job", jobId));
        if (!job.getEmployerId().equals(employerId)) {
            throw new AccessDeniedException("You do not own this job posting");
        }
        return applicationRepository.findByJobIdOrderByAppliedAtDesc(jobId).stream()
                .map(this::toResponse)
                .toList();
    }

    // employer moves an application to REVIEWED / ACCEPTED / REJECTED
    public ApplicationResponse updateStatus(String employerId, String applicationId, ApplicationStatus status) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application", applicationId));

        if (!application.getEmployerId().equals(employerId)) {
            throw new AccessDeniedException("You do not own the job for this application");
        }
        if (status == ApplicationStatus.APPLIED) {
            throw new BusinessRuleException("An application cannot be set back to APPLIED");
        }

        application.setStatus(status);
        applicationRepository.save(application);

        // notify the seeker of the decision
        userRepository.findById(application.getSeekerId()).ifPresent(seeker ->
                smsService.send(seeker.getPhone(),
                        "CareerNest: your application for '" + application.getJobTitle()
                                + "' is now " + status.name() + "."));

        return toResponse(application);
    }

    private ApplicationResponse toResponse(Application a) {
        return new ApplicationResponse(
                a.getId(),
                a.getJobId(),
                a.getJobTitle(),
                a.getSeekerId(),
                a.getSeekerName(),
                a.getEmployerId(),
                a.getStatus(),
                a.getCoverLetter(),
                a.getAppliedAt());
    }
}
