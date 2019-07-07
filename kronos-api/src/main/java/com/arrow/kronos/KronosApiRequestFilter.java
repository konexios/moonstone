package com.arrow.kronos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.arrow.acs.Loggable;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.AccessKey;

public class KronosApiRequestFilter extends OncePerRequestFilter {

    private static Loggable LOG = new Loggable() {
    };

    private final List<Pattern> exceptionPatterns;

    public KronosApiRequestFilter(List<String> exceptionPaths) {
        String method = getClass().getSimpleName();
        exceptionPatterns = new ArrayList<>();
        if (exceptionPaths != null) {
            for (String path : exceptionPaths) {
                try {
                    LOG.logDebug(method, "adding pattern %s", path);
                    exceptionPatterns.add(Pattern.compile(path));
                } catch (Exception e) {
                    LOG.logError(method, e);
                }
            }
        }
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String method = "doFilterInternal";

        LOG.logDebug(method, "method: %s, uri: %s", request.getMethod(), request.getRequestURI());

        for (Pattern pattern : exceptionPatterns) {
            if (pattern.matcher(request.getRequestURI()).matches()) {
                LOG.logDebug(method, "found match of exception pattern: %s", pattern.pattern());
                filterChain.doFilter(request, response);
                return;
            }
        }

        Object accessKey = RequestContextHolder.currentRequestAttributes().getAttribute(CoreConstant.API_KEY_CONTEXT,
                RequestAttributes.SCOPE_REQUEST);
        LOG.logDebug(method, "accessKey: %s", accessKey);
        if (accessKey != null && accessKey instanceof AccessKey) {
            String applicationId = ((AccessKey) accessKey).getApplicationId();
            if (applicationId != null) {
                // TODO NEED TO REVISIT
                // rpApiRequestprocessor.processRequest(applicationId);
            } else {
                LOG.logError(method, "applicationId is null");
            }
        } else {
            LOG.logError(method, "accessKey not found");
        }
        filterChain.doFilter(request, response);
    }
}
