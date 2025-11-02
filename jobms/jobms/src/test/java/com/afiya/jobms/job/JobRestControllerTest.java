package com.afiya.jobms.job;

import com.afiya.jobms.job.dto.JobWithDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JobController.class)
class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JobService jobservice; // This will be the mock

    @Autowired
    private ObjectMapper objectMapper;

    // This class provides the mock JobService to the test context
    @TestConfiguration
    static class TestConfig {
        @Bean
        public JobService jobService() {
            return mock(JobService.class);
        }
    }

    // This is important to prevent tests from interfering with each other
    @BeforeEach
    void setUp() {
        reset(jobservice);
    }

    @Test
    void testFindAll() throws Exception {
        // ARRANGE
        List<JobWithDto> fakeJobs = new ArrayList<>();
        when(jobservice.findAll()).thenReturn(fakeJobs);

        // ACT & ASSERT
        mockMvc.perform(get("/jobs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateJob() throws Exception {
        // ARRANGE
        Job newJob = new Job();
        newJob.setTitle("Java Dev");
        // 'createJob' returns void, so we use doNothing()
        doNothing().when(jobservice).createJob(any(Job.class));

        // ACT & ASSERT
        mockMvc.perform(post("/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newJob)))
                .andExpect(status().isCreated()) // Your controller returns 201 CREATED
                .andExpect(content().string("Job added succesfully"));

        // VERIFY
        verify(jobservice, times(1)).createJob(any(Job.class));
    }

    @Test
    void testGetJobById_WhenJobExists() throws Exception {
        // ARRANGE
        Job fakeJob = new Job();
        fakeJob.setId(1L);
        fakeJob.setTitle("Java Dev");
        when(jobservice.getJobById(1L)).thenReturn(fakeJob);

        // ACT & ASSERT
        mockMvc.perform(get("/jobs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Java Dev"));
    }

    @Test
    void testGetJobById_WhenJobNotFound() throws Exception {
        // ARRANGE
        when(jobservice.getJobById(1L)).thenReturn(null);

        // ACT & ASSERT
        mockMvc.perform(get("/jobs/1"))
                .andExpect(status().isNotFound()); // Your controller returns 404
    }

    @Test
    void testDelete_WhenJobExists() throws Exception {
        // ARRANGE
        when(jobservice.delete(1L)).thenReturn(true);

        // ACT & ASSERT
        mockMvc.perform(delete("/jobs/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Job deleted successfully"));
    }

    @Test
    void testDelete_WhenJobNotFound() throws Exception {
        // ARRANGE
        when(jobservice.delete(1L)).thenReturn(false);

        // ACT & ASSERT
        mockMvc.perform(delete("/jobs/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdate_WhenJobExists() throws Exception {
        // ARRANGE
        Job updatedJob = new Job();
        updatedJob.setTitle("Sr. Java Dev");
        when(jobservice.update(eq(1L), any(Job.class))).thenReturn(true);

        // ACT & ASSERT
        mockMvc.perform(put("/jobs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedJob)))
                .andExpect(status().isOk())
                .andExpect(content().string("Job updated successfully"));
    }

    @Test
    void testUpdate_WhenJobNotFound() throws Exception {
        // ARRANGE
        Job updatedJob = new Job();
        when(jobservice.update(eq(1L), any(Job.class))).thenReturn(false);

        // ACT & ASSERT
        mockMvc.perform(put("/jobs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedJob)))
                .andExpect(status().isNotFound());
    }
}