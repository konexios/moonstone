package com.arrow.pegasus.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import moonstone.acs.Loggable;

public class CsrfHeaderFilter extends OncePerRequestFilter {

	private Loggable logger = new Loggable() {
	};

	private final String csrfTokenName;
	private final List<Pattern> exceptionPatterns;

	public CsrfHeaderFilter(String csrfTokenName, String[] exceptionPaths) {
		String method = getClass().getSimpleName();
		exceptionPatterns = new ArrayList<>();

		this.csrfTokenName = csrfTokenName;
		logger.logDebug(method, "csrfTokenName: %s", csrfTokenName);

		if (exceptionPaths != null) {
			for (String path : exceptionPaths) {
				try {
					logger.logDebug(method, "adding pattern %s", path);
					exceptionPatterns.add(Pattern.compile(path));
				} catch (Exception e) {
					logger.logError(method, e);
				}
			}
		}
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {
		String method = "doFilterInternal";

        for (Pattern pattern : exceptionPatterns) {
            if (pattern.matcher(request.getRequestURI()).matches()) {
            	logger.logDebug(method, "found match of exception pattern: %s", pattern.pattern());
                filterChain.doFilter(request, response);
                return;
            }
        }

		logger.logInfo(method, "method: %s, uri: %s", request.getMethod(), request.getRequestURI());
        
		CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
		if (csrf != null) {
			Cookie cookie = WebUtils.getCookie(request, csrfTokenName);
			String token = csrf.getToken();
			if (cookie == null || !token.equals(cookie.getValue())) {
				logger.logDebug(method, "csrfTokenName: %s, token: %s, cookie: %s", csrfTokenName, token,
				        (cookie == null ? "NULL" : cookie.getValue()));

				cookie = new Cookie(csrfTokenName, token);

				// mark as secure if the site is secure
				if (request.isSecure()) {
					cookie.setSecure(true);
				}

				cookie.setPath("/");
				response.addCookie(cookie);
				logger.logDebug(method, "added cookie to response: %s", cookie.getValue());
			}
		} else {
			logger.logError(method, "%s not supported", CsrfToken.class.getName());
		}
		filterChain.doFilter(request, response);
	}
}
