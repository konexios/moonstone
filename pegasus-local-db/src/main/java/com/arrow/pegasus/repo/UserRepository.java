package com.arrow.pegasus.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.UserStatus;
import com.arrow.pegasus.data.security.AuthType;

@Repository
public interface UserRepository extends MongoRepository<User, String>, UserRepositoryExtension {
	User findByHashedLogin(String hashedLogin);

	User findByAuthsTypeAndAuthsRefIdAndAuthsPrincipal(AuthType type, String refId, String principal);

	List<User> findByCompanyId(String companyId);

	List<User> findByStatus(UserStatus status);

	List<User> findByCompanyIdAndStatus(String companyId, UserStatus status);

	Long deleteByCompanyId(String companyId);
}