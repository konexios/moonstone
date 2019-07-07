package com.arrow.pegasus.security;

import com.arrow.pegasus.data.profile.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class SecurityService {

    public boolean isSuperAdministrator() {
        final User authenticatedUser = getAuthenticatedUser();
        return authenticatedUser != null && authenticatedUser.isAdmin();
    }

    public boolean hasRole(String roleName) {
        final User authenticatedUser = getAuthenticatedUser();

        if (authenticatedUser == null || CollectionUtils.isEmpty(authenticatedUser.getRefRoles())) {
            return false;
        }

        return authenticatedUser.getRefRoles().stream().anyMatch(role -> role.getName().equals(roleName));
    }

    private User getAuthenticatedUser() {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth == null) {
                return null;
            }

            Object principal = auth.getPrincipal();

            return ((CoreUserDetails) principal).getUser();
        } catch (Exception e) {
            return null;
        }
    }
}
