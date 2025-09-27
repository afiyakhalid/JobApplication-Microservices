package com.afiya.jobms.job;

import com.afiya.jobms.job.dto.JobWithDto;

import java.util.List;

public interface  JobService {
    List<JobWithDto> findAll();
    void createJob(Job job);

    Job getJobById(Long id);
    boolean delete(Long id);
    boolean update(Long id, Job updatedJob);
}
