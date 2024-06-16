package com.bot.marketing.domain;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class CompanyService {
	
    @Autowired
    private CompanyInterface companyRepository;

    public void addCompany(Company company) {
        companyRepository.save(company);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company getCompanyById(String businessId) {
        return companyRepository.findById(businessId).orElse(null);
    }

    public void updateCompany(Company company) {
        companyRepository.save(company);
    }

    public void deleteCompany(String businessId) {
        companyRepository.deleteById(businessId);
    }

}
