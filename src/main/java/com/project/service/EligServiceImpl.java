package com.project.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.project.binding.EligResponse;
import com.project.entity.CitizenAppEntity;
import com.project.entity.CoTriggerEntity;
import com.project.entity.DcCaseEntity;
import com.project.entity.DcChildrenEntity;
import com.project.entity.DcEducationEntity;
import com.project.entity.DcIncomeEntity;
import com.project.entity.EligDtlsEntity;
import com.project.entity.PlanEntity;
import com.project.repo.CitizenAppRepo;
import com.project.repo.DcCaseRepo;
import com.project.repo.DcChildRepo;
import com.project.repo.DcEducationRepo;
import com.project.repo.DcIncomeRepo;
import com.project.repo.EligDtlsRepo;
import com.project.repo.PlanRepo;

public class EligServiceImpl implements EligService {
	@Autowired
	private PlanRepo planRepo;

	@Autowired
	private DcCaseRepo dcCaseRepo;

	@Autowired
	private DcIncomeRepo incomeRepo;

	@Autowired
	private DcChildRepo childRepo;

	@Autowired
	private CitizenAppRepo appRepo;

	@Autowired
	private DcEducationRepo educationRepo;

	@Autowired
	private EligDtlsRepo eligDtlsRepo;
	
	

	@Override
	public EligResponse determineEligibilty(Long caseNumber) {

		Optional<DcCaseEntity> caseEntity = dcCaseRepo.findById(caseNumber);
		Integer planId = null;
		String planName = null;
		Integer appId = null;
		
		
		if (caseEntity.isPresent()) {
			DcCaseEntity dcCaseEntity = caseEntity.get();
			planId = dcCaseEntity.getPlanId();
			appId = dcCaseEntity.getAppId();
		}
		Optional<PlanEntity> planEntity = planRepo.findById(planId);
		
		if (planEntity.isPresent()) {
			PlanEntity plan = planEntity.get();
			planName = plan.getPlanName();
		}
		
		
		Optional<CitizenAppEntity> app = appRepo.findById(appId);
		int age = 0;
		CitizenAppEntity citizenAppEntity = null;
		
		if (app.isPresent()) {
			citizenAppEntity = app.get();
			LocalDate dob = citizenAppEntity.getDob();
			LocalDate now = LocalDate.now();
			age = Period.between(dob, now).getYears();
		}
		 EligResponse eligResponse = planConditions(caseNumber, planName, age);
		  
		 // logic to store data
		 
		 EligDtlsEntity eligEntity = new EligDtlsEntity();
		 BeanUtils.copyProperties(eligResponse, eligEntity);
		 
		 eligEntity.setCaseNumber(caseNumber);
		 eligEntity.setHolderName(citizenAppEntity.getFullName());
		 eligEntity.setSsn(citizenAppEntity.getSsn());
		 
		 eligDtlsRepo.save(eligEntity);
		 
		 CoTriggerEntity coEntity = new CoTriggerEntity();
		 coEntity.setCaseNumber(caseNumber);
		 coEntity.setTrgstatus("pending");
		 
		 return eligResponse;
	}
	private EligResponse planConditions(Long CaseNumber, String PlanName, Integer age) {

		EligResponse response = new EligResponse();
		DcIncomeEntity income = incomeRepo.findByCaseNumber(CaseNumber);
		response.setPlanName(PlanName);

		if ("SNAP".equals(PlanName)) {
			Double empIncome = income.getEmpIncome();
			if (empIncome < 300) {
				response.setPlanStatus("AP");
			} else {
				response.setPlanStatus("DN");
				response.setDenialReason("HIGH INCOME");
			}

		} else if ("CCAP".equals(PlanName)) {
			boolean ageCondition = true;
			boolean kidsCountCondition = true;

			List<DcChildrenEntity> childs = childRepo.findByCaseNumber(CaseNumber);

			if (!childs.isEmpty()) {
				kidsCountCondition = false;
				for (DcChildrenEntity entity : childs) {
					age = entity.getChildAge();
					if (age > 16) {
						ageCondition = false;
						break;
					}
				}
			}
			if (income.getEmpIncome() <= 300 && kidsCountCondition && ageCondition) {
				response.setPlanStatus("AP");
			} else {
				response.setPlanStatus("DN");
				response.setPlanStatus("NOT SATISFIED RULES");
			}

		} else if ("Medicaid".equals(PlanName)) {

			Double empIncome = income.getEmpIncome();

			Double propertIncome = income.getPropertIncome();

			if (empIncome <= 300 && propertIncome <= 0) {
				response.setPlanStatus("AP");
			} else {
				response.setPlanStatus("DN");
				response.setPlanStatus("HIGH INCOME");
			}

		} else if ("Mediaid".equals(PlanName)) {

			if (age > 65) {
				response.setPlanStatus("AP");
			} else {
				response.setPlanStatus("DN");
				response.setPlanStatus("AGE NOT MATCHED");
			}

		} else if ("NJW".equals(PlanName)) {

			DcEducationEntity educationEntity = educationRepo.findByCaseNumber(CaseNumber);
			Integer graduationYear = educationEntity.getGraduationYear();

			int curnYear = LocalDate.now().getYear();

			if (income.getEmpIncome() < 0 && graduationYear < curnYear) {
				response.setPlanStatus("AP");
			} else {
				response.setPlanStatus("DN");
				response.setPlanStatus("RULES NOT SATISFIED");
			}
		}
		if (response.getPlanStatus().equals("AP")) {
			response.setPlanStartDate(LocalDate.now());
			response.setPlanEndDate(LocalDate.now().plusMonths(6));
			response.setBenifitAmount(500.00);
		}
		return response;
	}
}