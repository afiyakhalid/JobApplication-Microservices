package com.afiya.reviewms.reviews;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// 1. Load only JPA components and use H2
@DataJpaTest
class ReviewRepositoryTest {

    // 2. Get the real repository
    @Autowired
    private reviewRepository reviewRepository;

    @Test
    void testFindByCompanyId_WhenReviewsExist() {
        // --- ARRANGE ---
        // Create reviews for two different companies
        Review review1 = new Review();
        review1.setTitle("Great Place");
        review1.setCompanyId(1L); // Belongs to Company 1

        Review review2 = new Review();
        review2.setTitle("Good Work-Life");
        review2.setCompanyId(1L); // Belongs to Company 1

        Review review3 = new Review();
        review3.setTitle("Bad Management");
        review3.setCompanyId(2L); // Belongs to Company 2

        // Save them all to the database
        reviewRepository.save(review1);
        reviewRepository.save(review2);
        reviewRepository.save(review3);

        // --- ACT ---
        // Test your custom query method
        List<Review> company1Reviews = reviewRepository.findByCompanyId(1L);

        // --- ASSERT ---
        // Check that it *only* found the reviews for Company 1
        assertNotNull(company1Reviews);
        assertEquals(2, company1Reviews.size(), "Should only find 2 reviews for company 1");
    }

    @Test
    void testFindByCompanyId_WhenNoReviewsExist() {
        // --- ARRANGE ---
        // (Save no reviews)

        // --- ACT ---
        // Look for reviews for a company that has none
        List<Review> reviews = reviewRepository.findByCompanyId(404L);

        // --- ASSERT ---
        assertNotNull(reviews);
        assertTrue(reviews.isEmpty(), "The list of reviews should be empty");
    }
}
