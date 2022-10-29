package com.project.repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entity.DcIncomeEntity;

public interface DcIncomeRepo extends JpaRepository<DcIncomeEntity, Serializable>{
	
	public DcIncomeEntity findByCaseNumber(Long caseNumber);
}
