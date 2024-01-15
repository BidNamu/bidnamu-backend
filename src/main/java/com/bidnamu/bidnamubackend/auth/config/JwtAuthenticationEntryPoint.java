package com.bidnamu.bidnamubackend.auth.config;

import com.bidnamu.bidnamubackend.auth.exception.JwtErrorCode;
import com.bidnamu.bidnamubackend.global.exception.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String EXCEPTION_KEY = "exception";

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response,
        final AuthenticationException authException) throws IOException {
        final JwtErrorCode code = (JwtErrorCode) request.getAttribute(EXCEPTION_KEY);
        log.info("JwtAuthenticationEntryPoint has been called : {}", code.name());
        setResponse(response, code);
    }

    private void setResponse(final HttpServletResponse response, final JwtErrorCode jwtErrorCode)
        throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(jwtErrorCode.getHttpStatus().value());

        final ErrorResponse errorResponse = ErrorResponse.from(jwtErrorCode.getErrorCode(),
            jwtErrorCode.getMessage());
        final String result = new ObjectMapper().writeValueAsString(errorResponse);
        response.getWriter().write(result);
    }
}