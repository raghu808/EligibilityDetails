package com.project.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "DC_CHILDREN")
public class DcChildrenEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer childId;
	
	private Long caseNumber;
	
	private int childAge;
	
	private LocalDate childDob;
	
	private Long childSsn;
	
}
