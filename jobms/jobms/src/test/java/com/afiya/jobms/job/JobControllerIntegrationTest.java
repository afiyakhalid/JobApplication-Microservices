package com.afiya.jobms.job;

import com.afiya.jobms.job.dto.JobWithDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// 1. Start a REAL server on a RANDOM port
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// 2. Load our H2 database configuration
@TestPropertySource(locations = "classpath:application-test.properties")
class JobControllerIntegrationTest {

    // 3. Get the tool to make real HTTP requests
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JobRepository jobRepository; // We can inject the real repo to set up data

    @Test
    void testCreateAndGetJob() {

        // --- ARRANGE (Create a job object) ---
        Job newJob = new Job();
        newJob.setTitle("Java Developer");
        newJob.setDescription("Test Description");
        newJob.setCompanyId(1L); // Set a dummy companyId

        // --- ACT 1: CREATE ---
        ResponseEntity<String> createResponse = restTemplate.postForEntity(
                "/jobs",
                newJob,
                String.class
        );

        // --- ASSERT 1 ---
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertEquals("Job added succesfully", createResponse.getBody());

        // --- ACT 2: GET (using JobWithDto) ---
        // Note: The /jobs endpoint (findAll) returns JobWithDto
        // We can't easily test this in an integration test without a
        // mock Company service. Let's test getJobById instead.

        // --- ACT 2: GET BY ID ---
        ResponseEntity<Job> getResponse = restTemplate.getForEntity(
                "/jobs/1", // Assumes this is the first job (ID 1)
                Job.class
        );

        // --- ASSERT 2 ---
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals("Java Developer", getResponse.getBody().getTitle());
    }
}