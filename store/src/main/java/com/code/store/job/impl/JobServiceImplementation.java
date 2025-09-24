package com.code.store.job.impl;
import com.code.store.job.Job;
import com.code.store.job.JobRepository;
import com.code.store.job.JobService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Service
public class JobServiceImplementation implements JobService {
    JobRepository jobRepository;
    private Long nextId = 1L;//initializes the id to "1" if id is not specified"

    public JobServiceImplementation(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    //    private List<Job> jobs = new ArrayList<>();
//    JobRepository jobRepository;
//    private Long nextId = 1L;//initializes the id to "1" if id is not specified"

    @Override
    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    @Override
    public void createJob(Job job) {

        //sets the id to the job posted and increments itself to get added to another job posting
        jobRepository.save(job);
    }

    @Override
    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElse(null);
    }

    @Override
    public boolean delete(Long id) {
        try{
            jobRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean update(Long id, Job updatedJob) {
        Optional<Job> jobOptional=jobRepository.findById(id);

            if (jobOptional.isPresent()) {
                Job job=jobOptional.get();
                job.setTitle(updatedJob.getTitle());
                job.setDescription(updatedJob.getDescription());
                job.setMinSalary(updatedJob.getMinSalary());
                job.setMaxSalary(updatedJob.getMaxSalary());
                job.setLocation(updatedJob.getLocation());
                jobRepository.save(job);
                return true;
            }

return false;
    }
}
