package com.afiya.reviewms.reviews.impl;


import com.afiya.reviewms.reviews.Review;
import com.afiya.reviewms.reviews.reviewRepository;
import com.afiya.reviewms.reviews.reviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class reviewImplementation implements reviewService {
    public reviewImplementation(reviewRepository reviewrepository) {
        this.reviewrepository = reviewrepository;

    }

    private  final reviewRepository reviewrepository;

    @Override
    public List<Review> getAllReviews(Long companyId) {
        List<Review> reviews=reviewrepository.findByCompanyId(companyId);
        return reviews;
    }




    @Override
    public  boolean updatedreview( Long reviewId, Review updatedreview) {
        Review review=reviewrepository.findById(reviewId).orElse(null);
if(review!=null){
      review.setTitle(updatedreview.getTitle());
      review.setDescription(updatedreview.getDescription());

        reviewrepository.save(review);
        return true;
    }else{
        return false;

    }

    }

    @Override
    public boolean create( Long companyId,Review review) {

        if(companyId!=null&& review!=null){
            review.setCompanyId(companyId);
            reviewrepository.save(review);
            return true;
        }else{
            return false;
        }

    }

    @Override
    public boolean delete(Long reviewId) {

        Review review=reviewrepository.findById(reviewId).orElse(null);
        if (review!=null) {

            reviewrepository.delete(review);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Review getReviewid( Long reviewId) {
       return reviewrepository.findById(reviewId).orElse(null);

    }


}
