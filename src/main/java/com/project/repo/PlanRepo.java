package com.project.repo;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entity.PlanEntity;

public interface PlanRepo  extends JpaRepository<PlanEntity, Serializable>{

	List<PlanEntity> findAll();

}
