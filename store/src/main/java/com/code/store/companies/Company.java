package com.code.store.companies;

import com.code.store.job.Job;
import com.code.store.reviews.Review;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
@Entity

public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String description;
    private String name;
    @OneToMany(mappedBy = "company")
    @JsonIgnore
    private List<Job> jobs;
    @OneToMany(mappedBy = "company")
    private List<Review> reviews;

    //private List<Review> reviews;
    public Company() {

    }

    public long getId() {
        return id;   // ✅ add this getter
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }


    public List<Review> getReviews() {
        return reviews;
    }


}
