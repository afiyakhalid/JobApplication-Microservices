//package com.afiya.jobms;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class JobmsApplicationTests {
//
//	@Test
//	void contextLoads() {
//	}
//
//}
package com.afiya.jobms;

// JUnit 5 imports
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

// Spring Boot testing imports
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// Import your classes
import com.afiya.jobms.job.Job;
import com.afiya.jobms.job.JobRepository;
import com.afiya.jobms.job.JobService;

// JUnit assertion imports
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class JobmsApplicationTests {

    @Autowired
    private JobService jobService;

    @Autowired
    private JobRepository jobRepository;

    @Test
    void contextLoads() {
    }

    // --- Parameterized Test for JobService ---

    /**
     * Test Name: testGetJobById_WhenJobExists_ShouldReturnJob
     * This runs once for each @CsvSource line to test different job data.
     */
    @ParameterizedTest
    @CsvSource({
            "Software Engineer, 'Develops backend systems', '80000', '120000', 'New York', 1",
            "Data Analyst, 'Analyzes marketing data', '70000', '100000', 'Remote', 1",
            "Product Manager, 'Manages product lifecycle', '110000', '150000', 'San Francisco', 2"
    })
    void testGetJobById_WhenJobExists_ShouldReturnJob(
            String title, String description, String minSalary, String maxSalary, String location, Long companyId) {

        // ARRANGE - Set up the test data
        Job testJob = new Job();
        testJob.setTitle(title);
        testJob.setDescription(description);
        testJob.setMinSalary(minSalary);
        testJob.setMaxSalary(maxSalary);
        testJob.setLocation(location);
        testJob.setCompanyId(companyId); // Assuming Job has companyId

        Job savedJob = jobRepository.save(testJob);
        Long savedJobId = savedJob.getId();

        // ACT - Call the "System Under Test"
        Job foundJob = jobService.getJobById(savedJobId);

        // ASSERT - Check the expected result
        assertNotNull(foundJob, "The service returned null");

        assertEquals(title,
                foundJob.getTitle(),
                "The job title did not match.");

        assertEquals(description,
                foundJob.getDescription(),
                "The job description did not match.");

        assertEquals(minSalary,
                foundJob.getMinSalary(),
                "The min salary did not match.");

        assertEquals(companyId,
                foundJob.getCompanyId(),
                "The companyId did not match.");
    }
}