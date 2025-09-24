package com.code.store.reviews;

import com.code.store.companies.Company;

import java.util.List;

public interface reviewService {
    List<Review> getAllReviews(Long companyId);

    boolean  updatedreview(Long companyId,Long reviewId,Review review);
    boolean create( Long companyId,Review review);
    boolean delete(Long companyId,Long reviewId);
    Review getReviewid(Long companyId,Long reviewId);


}

