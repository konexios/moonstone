package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.UserRegistration;
import com.arrow.kronos.service.UserRegistrationStatus;

@Repository
public interface UserRegistrationRepository
        extends MongoRepository<UserRegistration, String>, UserRegistrationRepositoryExtension {
	
	public UserRegistration findByEmail(String email);
	
	public List<UserRegistration> findByStatus(UserRegistrationStatus status);

}