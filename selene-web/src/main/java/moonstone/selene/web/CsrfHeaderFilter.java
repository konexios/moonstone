package moonstone.selene.web;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import moonstone.selene.Loggable;

public class CsrfHeaderFilter extends OncePerRequestFilter {
    private static final String XSRF_TOKEN = "XSRF-TOKEN";

    private static class Logger extends Loggable {
    }

    private Logger logger = new Logger();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String method = "doFilterInternal";

        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrf != null) {
            Cookie cookie = WebUtils.getCookie(request, XSRF_TOKEN);
            String token = csrf.getToken();
            if (cookie == null || !token.equals(cookie.getValue())) {
                cookie = new Cookie(XSRF_TOKEN, token);

                // mark as secure if the site is secure
                if (request.isSecure()) {
                    cookie.setSecure(true);
                }

                cookie.setPath("/");
                response.addCookie(cookie);
                logger.logInfo(method, "added cookie to response: %s", cookie.getValue());
            }
        } else {
            logger.logError(method, "%s not supported", CsrfToken.class.getName());
        }
        filterChain.doFilter(request, response);
    }
}
