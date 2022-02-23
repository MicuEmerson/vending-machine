package com.example.vendingMachine.configs;

import com.example.vendingMachine.exceptions.ErrorDetails;
import com.example.vendingMachine.models.security.CustomSecurityUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class SessionTrackerFilter extends OncePerRequestFilter {

	private final ObjectMapper mapper;
	private final SessionRegistry sessionRegistry;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest,
	                                HttpServletResponse httpServletResponse,
	                                FilterChain filterChain) throws IOException, ServletException {

		Object currentPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if(currentPrincipal instanceof CustomSecurityUser && allowLogoutRequest(httpServletRequest.getRequestURI())){
			List<SessionInformation> activeUserSessions = sessionRegistry.getAllSessions(currentPrincipal, false);

			if(activeUserSessions.size() > 1){
				log.warn("More than one session for user!");
				httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
				httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

				ErrorDetails errorDetails = ErrorDetails.builder()
						.message("There is already an active session using your account")
						.build();

				mapper.writeValue(httpServletResponse.getWriter(), errorDetails);
			} else {
				filterChain.doFilter(httpServletRequest, httpServletResponse);
			}

		} else {
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		}
	}

	private boolean allowLogoutRequest(String uri){
		return !uri.equals("/logout/all");
	}
}