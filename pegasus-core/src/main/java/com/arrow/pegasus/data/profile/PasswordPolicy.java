package com.arrow.pegasus.data.profile;

import java.io.Serializable;

public class PasswordPolicy implements Serializable {
    private static final long serialVersionUID = -8165095241355450047L;

    public final static String PEGASUS_SIMPLE_SPECIAL = ";=_%#!~-";

    private final static int DEFAULT_MIN_LENGTH = 8;
    private final static int DEFAULT_MAX_LENGTH = 32;
    private final static int DEFAULT_MIN_LOWER_CASE = 1;
    private final static int DEFAULT_MIN_UPPER_CASE = 1;
    private final static int DEFAULT_MIN_DIGIT = 1;
    private final static int DEFAULT_MIN_SPECIAL = 1;
    private final static int DEFAULT_HISTORICAL = 3;
    private final static boolean DEFAULT_ALLOW_WHITESPACE = false;

    private int minLength = DEFAULT_MIN_LENGTH;
    private int maxLength = DEFAULT_MAX_LENGTH;
    private int minLowerCase = DEFAULT_MIN_LOWER_CASE;
    private int minUpperCase = DEFAULT_MIN_UPPER_CASE;
    private int minDigit = DEFAULT_MIN_DIGIT;
    private int minSpecial = DEFAULT_MIN_SPECIAL;
    private int historical = DEFAULT_HISTORICAL;
    private boolean allowWhitespace = DEFAULT_ALLOW_WHITESPACE;

    public boolean isAllowWhitespace() {
        return allowWhitespace;
    }

    public void setAllowWhitespace(boolean allowWhitespace) {
        this.allowWhitespace = allowWhitespace;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getMinLowerCase() {
        return minLowerCase;
    }

    public void setMinLowerCase(int minLowerCase) {
        this.minLowerCase = minLowerCase;
    }

    public int getMinUpperCase() {
        return minUpperCase;
    }

    public void setMinUpperCase(int minUpperCase) {
        this.minUpperCase = minUpperCase;
    }

    public int getMinDigit() {
        return minDigit;
    }

    public void setMinDigit(int minDigit) {
        this.minDigit = minDigit;
    }

    public int getMinSpecial() {
        return minSpecial;
    }

    public void setMinSpecial(int minSpecial) {
        this.minSpecial = minSpecial;
    }

    public int getHistorical() {
        return historical;
    }

    public void setHistorical(int historical) {
        this.historical = historical;
    }
}
