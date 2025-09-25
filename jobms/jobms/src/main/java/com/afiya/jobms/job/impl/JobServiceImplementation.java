package com.afiya.jobms.job.impl;


import com.afiya.jobms.job.Job;
import com.afiya.jobms.job.JobRepository;
import com.afiya.jobms.job.JobService;
import com.afiya.jobms.job.dto.JobWithCompanyDto;
import com.afiya.jobms.job.external.Company;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public List<JobWithCompanyDto> findAll() {
        List<Job> jobs=jobRepository.findAll();
        List<JobWithCompanyDto> jwc=new ArrayList<>();

        for (Job job:jobs){
            JobWithCompanyDto dto=new JobWithCompanyDto();
            dto.setJob(job);
            RestTemplate rest=new RestTemplate();
            Company company=rest.getForObject("http://localhost:8083/companies/"+job.getCompanyId(), Company.class);
            dto.setCompany(company);
            jwc.add(dto);
        }



        return jwc;
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
