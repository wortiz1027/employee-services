package co.com.services.employee.config;

import org.keycloak.adapters.springsecurity.authentication.KeycloakCookieBasedRedirect;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomKeycloakAuthenticationFailureHandler implements AuthenticationFailureHandler {

    public CustomKeycloakAuthenticationFailureHandler() {}

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (!response.isCommitted()) {
            if (KeycloakCookieBasedRedirect.getRedirectUrlFromCookie(request) != null) {
                response.addCookie(KeycloakCookieBasedRedirect.createCookieFromRedirectUrl((String)null));
            }

            //response.sendError(401, "Unable to authenticate using the Authorization header");
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().println("{ \"error\": \"" + exception.getMessage() + "\" }");
        } else if (200 <= response.getStatus() && response.getStatus() < 300) {
            throw new RuntimeException("Success response was committed while authentication failed!", exception);
        }
    }
}