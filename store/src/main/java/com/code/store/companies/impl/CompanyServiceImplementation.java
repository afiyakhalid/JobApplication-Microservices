package com.code.store.companies.impl;

import com.code.store.companies.Company;
import com.code.store.companies.CompanyRepository;
import com.code.store.companies.CompanyService;
import com.code.store.job.Job;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImplementation implements CompanyService {
    private CompanyRepository companyRepository;
    public CompanyServiceImplementation(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }


    @Override
    public List<Company> getAllCompany() {
        return companyRepository.findAll();
    }

    @Override
    public boolean updatedcompany(Company company,Long id) {
        Optional<Company> companyOptional=companyRepository.findById(id);

        if (companyOptional.isPresent()) {
            Company company1=companyOptional.get();

            company1.setDescription(company.getDescription());
            company1.setName(company.getName());
            company1.setJobs(company.getJobs());

            companyRepository.save(company1);
            return true;
        }

        return false;
    }

    @Override
    public void create(Company company) {
        companyRepository.save(company);
    }
    public boolean delete(Long id) {
        if (companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Company getbyid(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

}

