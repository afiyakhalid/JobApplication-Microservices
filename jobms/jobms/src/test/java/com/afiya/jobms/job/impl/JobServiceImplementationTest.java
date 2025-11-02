package com.afiya.jobms.job.impl;

import com.afiya.jobms.job.Job;
import com.afiya.jobms.job.JobRepository;
import com.afiya.jobms.job.clients.CompanyClient;
import com.afiya.jobms.job.dto.JobWithDto;
import com.afiya.jobms.job.external.Company;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceImplementationTest {

    // 1. Create fake versions of ALL dependencies
    @Mock
    private JobRepository jobRepository;

    @Mock
    private CompanyClient companyClient;

    // 2. Inject these fake dependencies into a REAL service instance
    @InjectMocks
    private JobServiceImplementation jobService;

    @Test
    void testFindAll_ShouldReturnJobsWithCompanies() {
        // --- ARRANGE ---
        // 1. Create fake data
        Job job1 = new Job();
        job1.setId(1L);
        job1.setTitle("Java Dev");
        job1.setCompanyId(1L);

        List<Job> fakeJobs = new ArrayList<>();
        fakeJobs.add(job1);

        Company fakeCompany = new Company();
        fakeCompany.setId(1L);
        fakeCompany.setName("TestCo");

        // 2. Program the mocks
        // "WHEN jobRepository.findAll() is called, THEN return our fakeJobs list"
        when(jobRepository.findAll()).thenReturn(fakeJobs);

        // "WHEN companyClient.getCompany(1L) is called, THEN return our fakeCompany"
        when(companyClient.getCompany(1L)).thenReturn(fakeCompany);

        // --- ACT ---
        // Call the real method
        List<JobWithDto> jobsWithDtos = jobService.findAll();

        // --- ASSERT ---
        // Check that the service correctly combined the data
        assertNotNull(jobsWithDtos);
        assertEquals(1, jobsWithDtos.size());
        assertEquals("Java Dev", jobsWithDtos.get(0).getJob().getTitle());
        assertEquals("TestCo", jobsWithDtos.get(0).getCompany().getName());

        // Verify that our mocks were actually called
        verify(jobRepository, times(1)).findAll();
        verify(companyClient, times(1)).getCompany(1L);
    }

    @Test
    void testGetJobById_WhenJobExists() {
        // --- ARRANGE ---
        Job fakeJob = new Job();
        fakeJob.setId(1L);
        fakeJob.setTitle("Java Dev");

        // Program the mock
        when(jobRepository.findById(1L)).thenReturn(Optional.of(fakeJob));

        // --- ACT ---
        Job foundJob = jobService.getJobById(1L);

        // --- ASSERT ---
        assertNotNull(foundJob);
        assertEquals(1L, foundJob.getId());
        assertEquals("Java Dev", foundJob.getTitle());
        verify(jobRepository, times(1)).findById(1L);
    }

    @Test
    void testGetJobById_WhenJobDoesNotExist() {
        // --- ARRANGE ---
        // Program the mock to return an empty optional
        when(jobRepository.findById(1L)).thenReturn(Optional.empty());

        // --- ACT ---
        Job foundJob = jobService.getJobById(1L);

        // --- ASSERT ---
        assertNull(foundJob);
        verify(jobRepository, times(1)).findById(1L);
    }
}
