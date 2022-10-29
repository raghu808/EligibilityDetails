package com.project.repo;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project.entity.EligDtlsEntity;

@Repository
public interface EligDtlsRepo extends JpaRepository<EligDtlsEntity, Serializable>{
	
}
