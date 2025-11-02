package com.afiya.reviewms.reviews;

import com.afiya.reviewms.reviews.messaging.ReviewMessageProducer;
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

@WebMvcTest(reviewController.class)
class reviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private reviewService reviewservice; // Mocked

    @Autowired
    private ReviewMessageProducer reviewMessageProducer; // Also mocked

    // This class provides mocks for BOTH dependencies in the controller's constructor
    @TestConfiguration
    static class TestConfig {
        @Bean
        public reviewService reviewService() {
            return mock(reviewService.class);
        }

        @Bean
        public ReviewMessageProducer reviewMessageProducer() {
            return mock(ReviewMessageProducer.class);
        }
    }

    @BeforeEach
    void setUp() {
        // Reset both mocks before each test
        reset(reviewservice);
        reset(reviewMessageProducer);
    }

    @Test
    void testGetAllReviews() throws Exception {
        // ARRANGE
        List<Review> fakeReviews = new ArrayList<>();
        when(reviewservice.getAllReviews(1L)).thenReturn(fakeReviews);

        // ACT & ASSERT
        // Note: We must include the ".param()" for the @RequestParam
        mockMvc.perform(get("/reviews").param("companyId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreate_WhenSuccessful() throws Exception {
        // ARRANGE
        Review newReview = new Review();
        newReview.setTitle("Great Place");
        // Your service returns true, indicating success
        when(reviewservice.create(eq(1L), any(Review.class))).thenReturn(true);
        // The producer returns void
        doNothing().when(reviewMessageProducer).sendMessage(any(Review.class));

        // ACT & ASSERT
        mockMvc.perform(post("/reviews").param("companyId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newReview)))
                .andExpect(status().isCreated()) // Your controller returns 201 CREATED
                .andExpect(content().string("review added succesfully"));

        // VERIFY
        // Check that the message was sent to RabbitMQ
        verify(reviewMessageProducer, times(1)).sendMessage(any(Review.class));
    }

    @Test
    void testCreate_WhenCompanyNotFound() throws Exception {
        // ARRANGE
        Review newReview = new Review();
        // Your service returns false, indicating "company not found"
        when(reviewservice.create(eq(1L), any(Review.class))).thenReturn(false);

        // ACT & ASSERT
        mockMvc.perform(post("/reviews").param("companyId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newReview)))
                .andExpect(status().isNotFound()) // Your controller returns 404
                .andExpect(content().string("company not found"));

        // VERIFY
        // Check that the message was NOT sent
        verify(reviewMessageProducer, never()).sendMessage(any(Review.class));
    }

    @Test
    void testGetReviewid() throws Exception {
        // ARRANGE
        Review fakeReview = new Review();
        fakeReview.setId(1L);
        when(reviewservice.getReviewid(1L)).thenReturn(fakeReview);

        // ACT & ASSERT
        mockMvc.perform(get("/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testDelete_WhenReviewExists() throws Exception {
        // ARRANGE
        when(reviewservice.delete(1L)).thenReturn(true);

        // ACT & ASSERT
        mockMvc.perform(delete("/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("review deleted successfully"));
    }

    @Test
    void testDelete_WhenReviewNotFound() throws Exception {
        // ARRANGE
        when(reviewservice.delete(1L)).thenReturn(false);

        // ACT & ASSERT
        mockMvc.perform(delete("/reviews/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdatedreview_WhenReviewExists() throws Exception {
        // ARRANGE
        Review updatedReview = new Review();
        when(reviewservice.updatedreview(eq(1L), any(Review.class))).thenReturn(true);

        // ACT & ASSERT
        mockMvc.perform(put("/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedReview)))
                .andExpect(status().isOk())
                .andExpect(content().string("review updated successfully"));
    }

    @Test
    void testUpdatedreview_WhenReviewNotFound() throws Exception {
        // ARRANGE
        Review updatedReview = new Review();
        when(reviewservice.updatedreview(eq(1L), any(Review.class))).thenReturn(false);

        // ACT & ASSERT
        mockMvc.perform(put("/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedReview)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("review not updated ")); // Your controller's specific message
    }

    @Test
    void testGetAverageRating() throws Exception {
        // ARRANGE
        // Test your specific controller logic (average of IDs)
        Review r1 = new Review();
        r1.setId(2L); // Using ID for avg as per your code
        Review r2 = new Review();
        r2.setId(4L); // Using ID for avg as per your code
        List<Review> fakeReviews = List.of(r1, r2);

        when(reviewservice.getAllReviews(1L)).thenReturn(fakeReviews);
        // The average of 2 and 4 is 3.0

        // ACT & ASSERT
        mockMvc.perform(get("/reviews/averageRating").param("companyId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("3.0"));
    }
}