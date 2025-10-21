package com.afiya.jobms.job.clients;

import com.afiya.jobms.job.external.Company;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="companyms", url="${companyservice.url}")
public interface CompanyClient {
    @GetMapping("/companies/{id}")
   Company getCompany(@PathVariable("id") Long  id);
}
