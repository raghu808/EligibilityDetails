package com.project.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "CO_TRIGGERS")
public class CoTriggerEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer tgrId;
	
	private Long caseNumber;
	@Lob //large Object
	private byte[] coPdf;
	private String trgstatus;
	
	
}
