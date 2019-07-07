package com.arrow.kronos.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.arrow.kronos.data.SocialEventRegistration;

public interface SocialEventRegistrationRepository extends MongoRepository<SocialEventRegistration, String>, SocialEventRegistrationRepositoryExtension {

	 public SocialEventRegistration findByVerificationCode(String verificationCode);
	 
	 public SocialEventRegistration findByEmail(String email);
	 
	 public SocialEventRegistration findByApplicationId(String applicationId);
}
