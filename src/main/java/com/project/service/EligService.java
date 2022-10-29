package com.project.service;

import org.springframework.web.bind.annotation.PathVariable;
import com.project.binding.EligResponse;

public interface EligService {
	
	public EligResponse determineEligibilty (@PathVariable Long caseNumber);
}
