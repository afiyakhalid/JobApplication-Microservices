package com.afiya.companyms.companies;

import java.util.List;

public interface CompanyService {
    List<Company> getAllCompany();
  boolean  updatedcompany(Company company,Long id);
  void create(Company company);
    boolean delete(Long id);
  Company getbyid(Long id);


}
