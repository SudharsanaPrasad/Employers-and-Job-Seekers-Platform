package com.example.careernest.controller;

import com.example.careernest.dto.JobRequest;
import com.example.careernest.dto.JobResponse;
import com.example.careernest.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    // --- Employer ---

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<JobResponse> create(Authentication authentication,
                                              @Valid @RequestBody JobRequest request) {
        JobResponse response = jobService.create(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public JobResponse update(Authentication authentication, @PathVariable String id,
                              @Valid @RequestBody JobRequest request) {
        return jobService.update(authentication.getName(), id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Void> delete(Authentication authentication, @PathVariable String id) {
        jobService.delete(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mine")
    @PreAuthorize("hasRole('EMPLOYER')")
    public Page<JobResponse> myJobs(Authentication authentication,
                                    @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                                    Pageable pageable) {
        return jobService.findMyJobs(authentication.getName(), pageable);
    }

    // --- Anyone logged in (job seekers browse and view) ---

    // GET /api/jobs?location=..&keyword=..&page=0&size=10
    @GetMapping
    public Page<JobResponse> search(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return jobService.search(location, keyword, pageable);
    }

    @GetMapping("/{id}")
    public JobResponse findById(@PathVariable String id) {
        return jobService.findById(id);
    }
}
