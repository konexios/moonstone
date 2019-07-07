package com.arrow.pegasus.web;

import com.arrow.pegasus.AccountHasNotPrivilegesException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

import static com.arrow.pegasus.web.PegasusWebConstants.Privilegious.PEGASUS_ACCESS;

public class CustomSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private String redirectSuccessUrl;

    public CustomSuccessHandler(String redirectSuccessUrl) {
        this.redirectSuccessUrl = redirectSuccessUrl;
    }

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request,
                                        final HttpServletResponse response, final Authentication authentication)
            throws IOException, ServletException {

        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        if (currentAuth != null) {
            final Collection<? extends GrantedAuthority> authorities = currentAuth.getAuthorities();
            if (authorities.stream().noneMatch(authority -> authority.getAuthority().equals(PEGASUS_ACCESS))) {
                throw new AccountHasNotPrivilegesException();
            }

            response.sendRedirect(redirectSuccessUrl);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
