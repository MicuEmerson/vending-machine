package com.example.vendingMachine.controllers;

import com.example.vendingMachine.utils.AuthenticationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class SessionController {

	private final SessionRegistry sessionRegistry;

	@RequestMapping(method = RequestMethod.PUT, value = "/logout/all")
	public ResponseEntity<Void> clearSession(Authentication authentication){
		List<SessionInformation> activeUserSessions = sessionRegistry.getAllSessions(authentication.getPrincipal(), false);
		activeUserSessions.forEach(session -> sessionRegistry.removeSessionInformation(session.getSessionId()));
		Long authId = AuthenticationUtils.getAuthId(authentication);
		log.info("Cleared session for userId : {}", authId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
