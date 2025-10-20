package com.afiya.jobms.job.impl;


import com.afiya.jobms.job.Job;
import com.afiya.jobms.job.JobRepository;
import com.afiya.jobms.job.JobService;
import com.afiya.jobms.job.clients.CompanyClient;
import com.afiya.jobms.job.dto.JobWithDto;
import com.afiya.jobms.job.external.Company;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
//import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImplementation implements JobService {
    JobRepository jobRepository;
    private Long nextId = 1L;//initializes the id to "1" if id is not specified"
@Autowired
RestTemplate restTemplate;
    public JobServiceImplementation(JobRepository jobRepository , CompanyClient companyClient) {
        this.companyClient = companyClient;
        this.jobRepository = jobRepository;
    }
    private CompanyClient companyClient;

    //    private List<Job> jobs = new ArrayList<>();
//    JobRepository jobRepository;
//    private Long nextId = 1L;//initializes the id to "1" if id is not specified"

    @Override
   @CircuitBreaker(name="companyBreaker",fallbackMethod = "companyBreakerFallback")
//    @Retry(name="companyRetry",fallbackMethod = "companyBreakerFallback")
    public List<JobWithDto> findAll() {
        List<Job> jobs=jobRepository.findAll();
        List<JobWithDto> jwc=new ArrayList<>();

        for (Job job:jobs){
            JobWithDto dto=new JobWithDto();
            dto.setJob(job);
//            RestTemplate restTemplate=new RestTemplate();

            Company company = companyClient.getCompany(job.getCompanyId());
            dto.setCompany(company);
            jwc.add(dto);
        }



        return jwc;
    }
    public List<String> companyBreakerFallback(Exception e){
        List<String> list=new ArrayList<>();
        list.add("Company Service is down");
        return list;
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
