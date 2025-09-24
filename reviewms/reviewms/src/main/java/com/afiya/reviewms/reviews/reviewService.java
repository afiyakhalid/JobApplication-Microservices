package com.afiya.reviewms.reviews;

import java.util.List;

public interface reviewService {
    List<Review> getAllReviews(Long companyId);

    boolean  updatedreview(Long reviewId,Review review);
    boolean create( Long companyId,Review review);
    boolean delete(Long reviewId);
    Review getReviewid(Long reviewId);


}

