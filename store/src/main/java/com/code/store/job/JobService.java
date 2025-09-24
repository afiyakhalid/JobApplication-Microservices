package com.code.store.job;

import java.util.List;

public interface  JobService {
    List<Job> findAll();
    void createJob(Job job);

    Job getJobById(Long id);
    boolean delete(Long id);
    boolean update(Long id, Job updatedJob);
}
