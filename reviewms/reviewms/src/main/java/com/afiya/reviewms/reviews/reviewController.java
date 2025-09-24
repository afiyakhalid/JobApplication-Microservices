package com.afiya.reviewms.reviews;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/reviews")
public class reviewController {
    private reviewService reviewservice;

    public reviewController(reviewService reviewservice) {
        this.reviewservice = reviewservice;
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews( @RequestParam Long companyId){

        return ResponseEntity.ok(reviewservice.getAllReviews(companyId));
    }
    @PostMapping
    public ResponseEntity<String> create( @RequestParam Long companyId,@RequestBody Review review){

         boolean isReview=reviewservice.create( companyId,review);
//      Company r=review.getCompany();
        if(isReview){
        return  new ResponseEntity<>("review added succesfully", HttpStatus.CREATED);
        }else{
            return  new ResponseEntity<>("company not found", HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReviewid(@PathVariable Long reviewId) {
        return  new  ResponseEntity<>(reviewservice.getReviewid(reviewId),HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> delete(@PathVariable Long reviewId){
        boolean deleted=reviewservice.delete(reviewId);
        if(deleted){
            return new ResponseEntity<>("review deleted successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PutMapping("/{reviewId}")
    public ResponseEntity<String> updatedreview(@PathVariable Long reviewId,@RequestBody  Review review){
        boolean updated=reviewservice.updatedreview(reviewId,review);
        if(updated){
            return  new  ResponseEntity<>("review updated successfully",HttpStatus.OK);
        }
        return  new ResponseEntity<>( "review not updated ",HttpStatus.NOT_FOUND);
    }
}

