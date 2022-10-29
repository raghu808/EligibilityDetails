package com.project.repo;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.entity.CitizenAppEntity;

public interface CitizenAppRepo extends JpaRepository<CitizenAppEntity, Serializable>{

}
