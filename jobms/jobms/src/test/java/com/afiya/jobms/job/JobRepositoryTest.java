
package com.afiya.jobms.job;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

// 1. Load only the JPA components and use an in-memory H2 database
@DataJpaTest
class JobRepositoryTest {

    // 2. Get the real repository, connected to the H2 database
    @Autowired
    private JobRepository jobRepository;

    @Test
    void testFindById_WhenJobExists() {
        // --- ARRANGE ---
        // Create a new job object
        Job jobToSave = new Job();
        jobToSave.setTitle("Java Developer");
        jobToSave.setDescription("A test job");
        jobToSave.setCompanyId(1L);

        // Save it to the in-memory database
        Job savedJob = jobRepository.save(jobToSave);

        // --- ACT ---
        // Try to find the job we just saved
        Optional<Job> foundJob = jobRepository.findById(savedJob.getId());

        // --- ASSERT ---
        assertTrue(foundJob.isPresent(), "Job should be found");
        assertEquals("Java Developer", foundJob.get().getTitle());
    }

    @Test
    void testFindById_WhenJobDoesNotExist() {
        // --- ARRANGE ---
        // (Do not save anything)

        // --- ACT ---
        Optional<Job> foundJob = jobRepository.findById(99L);

        // --- ASSERT ---
        assertFalse(foundJob.isPresent(), "Job should not be found");
    }
}