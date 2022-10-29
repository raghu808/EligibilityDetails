package com.project.repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entity.DcEducationEntity;

public interface DcEducationRepo extends JpaRepository<DcEducationEntity, Serializable>{
	
	
	public DcEducationEntity  findByCaseNumber(Long caseNumber);

}
