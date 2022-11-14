package com.spfwproject.quotes.services;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.models.SignUpFormRequest;
import com.spfwproject.quotes.validators.SignUpFormValidator;

@Component
public class AuthenticationService implements AuthenticationProvider {
	private Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

	private static final Random RANDOM = new SecureRandom();
	private static final int ITERATIONS = 1; // TODO: decide on value, was 1000
	private static final int KEY_LENGTH = 256;

	@Autowired
	private UserService userService;

	public AuthenticationService(UserService userService) {
		this.userService = userService;
	}

	public ArrayList<byte[]> generatePasswordHashWithSalt(char[] passwordAsCharArray) {
		final String methodName = "isExpectedPassword";
		logger.info("Entering " + methodName);
		byte[] salt = getNextSalt();

		byte[] passwordHash = generatePasswordWithPDKDF2(passwordAsCharArray, salt);

		ArrayList<byte[]> passwordAndSalt = new ArrayList<byte[]>(2);
		passwordAndSalt.add(passwordHash);
		passwordAndSalt.add(salt);

		logger.info("Exiting " + methodName);
		return passwordAndSalt;
	}

	/**
	 * Returns a.....
	 *
	 * @return
	 */
	protected static byte[] getNextSalt() {
		byte[] salt = new byte[16];
		RANDOM.nextBytes(salt);
		return salt;
	}

	private byte[] generatePasswordWithPDKDF2(final char[] password, byte[] salt) {
		try {
			return SecretKeyFactory.getInstance("PBKDF2WithHmacSha1")
					.generateSecret(new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH)).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isExpectedPassword(char[] password, byte[] salt, byte[] expectedPasswordHash) {
		final String methodName = "isExpectedPassword";
		logger.info("Entering " + methodName);

		byte[] pwdHash = generatePasswordWithPDKDF2(password, salt);
		Arrays.fill(password, Character.MIN_VALUE);

		if (pwdHash.length != expectedPasswordHash.length) {
			logger.info("Exiting " + methodName + ", password did not match expected password.");
			return false;
		}

		for (int i = 0; i < pwdHash.length; i++) {
			if (pwdHash[i] != expectedPasswordHash[i]) {
				logger.info("Exiting " + methodName + ", password did not match expected password.");
				return false;
			}
		}

		logger.info("Exiting " + methodName + ", password matched expected password.");
		return true;
	}

	public SignUpFormValidator validateSignupForm(SignUpFormRequest signupForm) {
		final String methodName = "validateSignupForm";
		logger.info("Entering " + methodName);

		SignUpFormValidator signUpFormValidator = new SignUpFormValidator(signupForm);
		signUpFormValidator.validate();

		if (userService.doesUsernameAlreadyExist(signupForm.getUsername())) {
			if (!signUpFormValidator.getListOfErrors().contains("Invalid username.")) {
				signUpFormValidator.addErrorMessageToErrorList("Invalid username.");
			}

		}

		logger.info("Exiting " + methodName);
		return signUpFormValidator;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		final String methodName = "authenticate";
		logger.info("Entering " + methodName);

		String username = authentication.getName();
		char[] password = authentication.getCredentials().toString().toCharArray();

		logger.info("in authenticate pre get userbyusername");
		UserEntity user = userService.getUserByUsername(username);

		if (isExpectedPassword(password, user.getSalt().getBytes(), user.getPassword().getBytes())) {
			logger.info("Exiting " + methodName);
			return new UsernamePasswordAuthenticationToken(user, password, Collections.emptyList());
		} else {

			logger.info("Exiting " + methodName + ", throwing exception");
			throw new BadCredentialsException("External system authentication failed");
		}
	}

	@Override
	public boolean supports(Class<?> authenticationType) {
		return authenticationType.equals(UsernamePasswordAuthenticationToken.class);
	}

}
