package com.afiya.jobms.job.dto;

import com.afiya.jobms.job.Job;
import com.afiya.jobms.job.external.Company;

public class JobWithCompanyDto {
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
}
