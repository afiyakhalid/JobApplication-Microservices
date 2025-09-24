package com.code.store.reviews.impl;

import com.code.store.companies.Company;
import com.code.store.companies.CompanyService;
import com.code.store.reviews.Review;
import com.code.store.reviews.reviewRepository;
import com.code.store.reviews.reviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class reviewImplementation implements reviewService {
    public reviewImplementation(reviewRepository reviewrepository,CompanyService companyService) {
        this.reviewrepository = reviewrepository;
        this.companyService=companyService;
    }

    private  final reviewRepository reviewrepository;
    private  final CompanyService companyService;
    @Override
    public List<Review> getAllReviews(Long companyId) {
        List<Review> reviews=reviewrepository.findByCompanyId(companyId);
        return reviews;
    }




    @Override
    public  boolean updatedreview(Long companyId,Long reviewId,Review updatedreview) {
    if(companyService.getbyid(companyId)!=null) {
        updatedreview.setCompany(companyService.getbyid(companyId));
        updatedreview.setId(reviewId);
        reviewrepository.save(updatedreview);
        return true;
    }else{
        return false;

    }

    }

    @Override
    public boolean create( Long companyId,Review review) {
        Company company=companyService.getbyid(companyId);
        if(company!=null){
            review.setCompany(company);
            reviewrepository.save(review);
            return true;
        }else{
            return false;
        }

    }

    @Override
    public boolean delete(Long companyId,Long reviewId) {
        if (companyService.getbyid(companyId) != null && reviewrepository.existsById(reviewId)) {
            Review review = reviewrepository.findById(reviewId).orElse(null);
            Company company = review.getCompany();
            company.getReviews().remove(review);
            companyService.updatedcompany(company, companyId);
            reviewrepository.deleteById(reviewId);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Review getReviewid(Long companyId, Long reviewId) {
        List<Review> reviews = reviewrepository.findByCompanyId(companyId);
        return reviews.stream()
                .filter(review -> review.getId() == reviewId)
                .findFirst()
                .orElse(null);

    }


}
