package com.code.store.reviews;

import com.code.store.companies.Company;
import com.code.store.job.Job;
import com.code.store.job.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import java.util.ArrayList;



@RestController
@RequestMapping("companies/{companyId}")
public class reviewController {
    private reviewService reviewservice;

    public reviewController(reviewService reviewservice) {
        this.reviewservice = reviewservice;
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<Review>> getAllReviews( @PathVariable Long companyId){

        return ResponseEntity.ok(reviewservice.getAllReviews(companyId));
    }
    @PostMapping("/reviews")
    public ResponseEntity<String> create( @PathVariable Long companyId,@RequestBody Review review){

         boolean isReview=reviewservice.create( companyId,review);
//      Company r=review.getCompany();
        if(isReview){
        return  new ResponseEntity<>("review added succesfully", HttpStatus.CREATED);
        }else{
            return  new ResponseEntity<>("company not found", HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> getReviewid(@PathVariable Long companyId,@PathVariable Long reviewId) {
        return  new  ResponseEntity<>(reviewservice.getReviewid(companyId,reviewId),HttpStatus.OK);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<String> delete(@PathVariable Long companyId, Long reviewId){
        boolean deleted=reviewservice.delete(companyId,reviewId);
        if(deleted){
            return new ResponseEntity<>("review deleted successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<String> updatedreview(@PathVariable Long companyId,@PathVariable Long reviewId,@RequestBody  Review review){
        boolean updated=reviewservice.updatedreview(companyId,reviewId,review);
        if(updated){
            return  new  ResponseEntity<>("review updated successfully",HttpStatus.OK);
        }
        return  new ResponseEntity<>( "review not updated ",HttpStatus.NOT_FOUND);
    }
}

