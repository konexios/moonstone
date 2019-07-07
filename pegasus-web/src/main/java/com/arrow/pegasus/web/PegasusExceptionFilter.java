package com.arrow.pegasus.web;

import com.arrow.pegasus.AccountHasNotPrivilegesException;
import com.arrow.pegasus.InvalidLoginException;
import com.arrow.pegasus.LockedAccountException;
import com.arrow.pegasus.LoginRequiredException;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.NestedServletException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Exception handler for chain of filters
 */
public class PegasusExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (InvalidLoginException | LoginRequiredException e) {
            setStatus(response, HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (LockedAccountException | AccountHasNotPrivilegesException e) {
            setStatus(response, HttpStatus.FORBIDDEN, e.getMessage());
        } catch (NestedServletException e) {
            loginRequiredExceptionHandler(response, e);
        } catch (RuntimeException e) {
            setStatus(response, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void setStatus(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.getWriter().write(message);
    }

    private void loginRequiredExceptionHandler(HttpServletResponse response, NestedServletException e) throws IOException {
        if (e.getRootCause().getClass().equals(LoginRequiredException.class)) {
            setStatus(response, HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
