package com.arrow.kronos.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.KioskSignup;

@Repository
public interface KioskSignupRepository extends MongoRepository<KioskSignup, String>, KioskSignupRepositoryExtension {

	public KioskSignup findByEmail(String email);
}