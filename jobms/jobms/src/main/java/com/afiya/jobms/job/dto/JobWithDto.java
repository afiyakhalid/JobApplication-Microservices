package com.afiya.jobms.job.dto;

import com.afiya.jobms.job.Job;
import com.afiya.jobms.job.external.Company;
//import com.afiya.jobms.job.external.Review;

public class JobWithDto {
    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    private Job job;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    private Company company;

//    public Review getReview() {
//        return review;
//    }
//
//    public void setReview(Review review) {
//        this.review = review;
//    }
//
//    private Review review;
}
