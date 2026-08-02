package com.example.careernest.controller;

import com.example.careernest.dto.ApplicationRequest;
import com.example.careernest.dto.ApplicationResponse;
import com.example.careernest.dto.StatusUpdateRequest;
import com.example.careernest.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // --- Job seeker ---

    @PostMapping
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<ApplicationResponse> apply(Authentication authentication,
                                                     @Valid @RequestBody ApplicationRequest request) {
        ApplicationResponse response = applicationService.apply(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/mine")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public List<ApplicationResponse> myApplications(Authentication authentication) {
        return applicationService.getMyApplications(authentication.getName());
    }

    // --- Employer ---

    @GetMapping("/employer")
    @PreAuthorize("hasRole('EMPLOYER')")
    public List<ApplicationResponse> employerApplications(Authentication authentication) {
        return applicationService.getEmployerApplications(authentication.getName());
    }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public List<ApplicationResponse> jobApplications(Authentication authentication, @PathVariable String jobId) {
        return applicationService.getJobApplications(authentication.getName(), jobId);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ApplicationResponse updateStatus(Authentication authentication, @PathVariable String id,
                                            @Valid @RequestBody StatusUpdateRequest request) {
        return applicationService.updateStatus(authentication.getName(), id, request.status());
    }
}
