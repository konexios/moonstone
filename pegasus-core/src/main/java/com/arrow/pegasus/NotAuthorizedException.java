package com.arrow.pegasus;

import com.arrow.acs.AcsLogicalException;

public class NotAuthorizedException extends AcsLogicalException {
    private static final long serialVersionUID = -2291163963012514803L;

    public NotAuthorizedException() {
        super("Not Authorized");
        // TODO Auto-generated constructor stub
    }
}
