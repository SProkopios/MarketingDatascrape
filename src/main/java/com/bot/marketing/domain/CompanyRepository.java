package com.bot.marketing.domain;


import java.util.List;
import java.util.Optional;

import javax.persistence.Entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;



@Entity
public interface CompanyRepository extends JpaRepository<Company, String> {
	
	Optional<Company> findById(String businessId);
	
	List<Company> findAll();
}
