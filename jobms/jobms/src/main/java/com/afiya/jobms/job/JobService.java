package com.afiya.jobms.job;

import com.afiya.jobms.job.dto.JobWithCompanyDto;

import java.util.List;

public interface  JobService {
    List<JobWithCompanyDto> findAll();
    void createJob(Job job);

    Job getJobById(Long id);
    boolean delete(Long id);
    boolean update(Long id, Job updatedJob);
}
