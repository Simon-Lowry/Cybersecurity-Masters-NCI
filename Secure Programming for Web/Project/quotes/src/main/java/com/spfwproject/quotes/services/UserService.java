package com.spfwproject.quotes.services;

import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import com.spfwproject.quotes.entities.UserEntity;
import com.spwproject.quotes.dbaccesslayer.UserDBAccess;


@Component
public class UserService {
	 private final UserDBAccess userDBAccess;
	    
	 private Logger logger = LoggerFactory.getLogger(UserService.class);

	 public UserService(UserDBAccess userDBAccess) {
		 this.userDBAccess = userDBAccess;
	 }

	public List<UserEntity> getUsers() {
		final String methodName = "getUsers";
		logger.info("Entered " + methodName);

		List<UserEntity> allusers = userDBAccess.getAllUsers();

		logger.info("Exiting method " + methodName + ".");
		return allusers;

	}

	public UserEntity getUser(@PathVariable Long id) {
		final String methodName = "getUser";
		logger.info("Entered " + methodName + ", retrieving user with id: " + id);

		UserEntity user = userDBAccess.getUser(id);

		logger.info("Exiting method " + methodName + ".");
		return user;
	}

	public UserEntity createUser(@RequestBody UserEntity user) throws URISyntaxException {
		final String methodName = "createUser";
		logger.info("Entered " + methodName);

		UserEntity createdUser = userDBAccess.createUser(user);

		logger.info("Exiting method " + methodName + ".");
		return createdUser;
	}

	public UserEntity updateUser(@PathVariable Long id, @RequestBody UserEntity user) throws URISyntaxException {
		final String methodName = "updateUser";
		logger.info("Entered " + methodName);

		UserEntity userToBeUpdated = userDBAccess.getUser(id);
		userToBeUpdated.setName(user.getName());
		userToBeUpdated.setEmail(user.getEmail());
		userToBeUpdated = userDBAccess.createUser(user);

		logger.info("Exiting method " + methodName + ".");
		return userToBeUpdated;
	}

	public boolean deleteUser(@PathVariable Long id) {
		final String methodName = "deleteUser";
		logger.info("Entered " + methodName);

		userDBAccess.deleteUser(id);

		logger.info("Exiting method " + methodName + ".");
		return true;
	}

}
