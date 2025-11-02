//package com.afiya.reviewms;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class ReviewmsApplicationTests {
//
//	@Test
//	void contextLoads() {
//	}
//
//}
package com.afiya.reviewms;

// JUnit 5 imports
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

// Spring Boot testing imports
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// Import your classes
import com.afiya.reviewms.reviews.Review;
import com.afiya.reviewms.reviews.reviewRepository;
import com.afiya.reviewms.reviews.reviewService;

// JUnit assertion imports
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ReviewmsApplicationTests {

    @Autowired
    private reviewService reviewService;

    @Autowired
    private reviewRepository reviewRepository;

    @Test
    void contextLoads() {
    }

    // --- Parameterized Test for reviewService ---

    /**
     * Test Name: testGetReviewById_WhenReviewExists_ShouldReturnReview
     * This runs once for each @CsvSource line to test different review data.
     */
    @ParameterizedTest
    @CsvSource({
            "Great place to work, 'Loved the culture and the team', 1",
            "Good pay, 'Work-life balance could be better', 1",
            "Challenging projects, 'Learned a lot in a short time', 2"
    })
    void testGetReviewById_WhenReviewExists_ShouldReturnReview(
            String title, String description, Long companyId) {

        // ARRANGE - Set up the test data
        Review testReview = new Review();
        testReview.setTitle(title);
        testReview.setDescription(description);
        testReview.setCompanyId(companyId);

        Review savedReview = reviewRepository.save(testReview);
        Long savedReviewId = savedReview.getId();

        // ACT - Call the "System Under Test"
        Review foundReview = reviewService.getReviewid(savedReviewId);

        // ASSERT - Check the expected result
        assertNotNull(foundReview, "The service returned null");

        assertEquals(title,
                foundReview.getTitle(),
                "The review title did not match.");

        assertEquals(description,
                foundReview.getDescription(),
                "The review description did not match.");

        assertEquals(companyId,
                foundReview.getCompanyId(),
                "The companyId did not match.");
    }
}
