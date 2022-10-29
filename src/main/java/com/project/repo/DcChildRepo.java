package com.project.repo;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entity.DcChildrenEntity;

public interface DcChildRepo extends JpaRepository<DcChildrenEntity, Serializable>{
	
	public List<DcChildrenEntity> findByCaseNumber(Long caseNumber);
}	
