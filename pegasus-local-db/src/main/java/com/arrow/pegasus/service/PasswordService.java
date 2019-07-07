package com.arrow.pegasus.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordGenerator;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.pegasus.data.profile.PasswordPolicy;
import com.arrow.pegasus.data.profile.User;

@Service
public class PasswordService extends BaseServiceAbstract {

	public List<String> validatePassword(PasswordPolicy policy, User user, String password) {
		Assert.notNull(policy, "policy is missing");
		Assert.notNull(user, "user is missing");
		Assert.hasText(password, "password is missing");

		List<String> errors = new ArrayList<>();
		List<Rule> rules = new ArrayList<>();
		rules.add(new LengthRule(policy.getMinLength(), policy.getMaxLength()));
		rules.addAll(getCharacterRules(policy));
		if (!policy.isAllowWhitespace()) {
			rules.add(new WhitespaceRule());
		}

		PasswordValidator validator = new PasswordValidator(rules);
		RuleResult result = validator.validate(new PasswordData(password));
		if (result.isValid()) {
			String hashed = getCryptoService().getCrypto().hash(password, user.getSalt());
			if (!StringUtils.equals(hashed, user.getPassword())) {
				for (String old : user.getOldPasswords()) {
					if (StringUtils.equals(hashed, old)) {
						errors.add("Validation Error: same password is recently used");
						break;
					}
				}
			} else {
				errors.add("Validation Error: same as current password");
			}
		} else {
			errors.addAll(validator.getMessages(result));
		}
		return errors;
	}

	public String generateRandomPassword(PasswordPolicy policy) {
		return new PasswordGenerator()
				.generatePassword(policy.getMaxLength(), getCharacterRules(policy));
	}


	private List<CharacterRule> getCharacterRules(PasswordPolicy policy) {

		List<CharacterRule> rules = new ArrayList<>();

		if (policy.getMinLowerCase() > 0) {
			rules.add(new CharacterRule(EnglishCharacterData.LowerCase, policy.getMinLowerCase()));
		}

		if (policy.getMinUpperCase() > 0) {
			rules.add(new CharacterRule(EnglishCharacterData.UpperCase, policy.getMinUpperCase()));
		}

		if (policy.getMinDigit() > 0) {
			rules.add(new CharacterRule(EnglishCharacterData.Digit, policy.getMinDigit()));
		}

		if (policy.getMinSpecial() > 0) {
			rules.add(new CharacterRule(PegasusCharacterData.Special, policy.getMinSpecial()));
		}

		return rules;
	}

	public enum PegasusCharacterData implements CharacterData {
		Special("PEGASUS_SIMPLE_SPECIAL", PasswordPolicy.PEGASUS_SIMPLE_SPECIAL);

		private final String errorCode;

		private final String characters;

		PegasusCharacterData(final String code, final String charString) {
			errorCode = code;
			characters = charString;
		}

		@Override
		public String getErrorCode() {
			return errorCode;
		}

		@Override
		public String getCharacters() {
			return characters;
		}
	}
}
