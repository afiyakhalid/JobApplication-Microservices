package com.code.store.job;

import com.code.store.companies.Company;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import java.util.ArrayList;



@RestController
public class JobController {
 private JobService jobservice;

    public JobController(JobService jobservice) {
        this.jobservice = jobservice;
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<Job>> findAll(){

        return ResponseEntity.ok(jobservice.findAll());
    }
    @PostMapping("/jobs")
    public ResponseEntity<String> createJob(@RequestBody Job job){

                jobservice.createJob(job);
                Company c=job.getCompany();
                return  new ResponseEntity<>("Job added succesfully",HttpStatus.CREATED);
    }
    @GetMapping("/jobs/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id){
    Job job=jobservice.getJobById(id);
        if (job!=null){
        return new ResponseEntity<>(job, HttpStatus.OK) ;}
        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

@DeleteMapping("/jobs/{id}")
public ResponseEntity<String> delete(@PathVariable Long id){
    boolean deleted=jobservice.delete(id);
    if(deleted){
        return new ResponseEntity<>("Job deleted successfully",HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
}
@PutMapping("/jobs/{id}")
public ResponseEntity<String> update(@PathVariable Long id,@RequestBody  Job updatedJob){
        boolean updated=jobservice.update(id,updatedJob);
        if(updated){
            return  new  ResponseEntity<>("Job updated successfully",HttpStatus.OK);
        }
        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
}
}

