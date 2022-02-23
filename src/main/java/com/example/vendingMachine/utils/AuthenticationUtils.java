package com.example.vendingMachine.utils;

import com.example.vendingMachine.exceptions.ActionNotPermitted;
import com.example.vendingMachine.models.security.CustomSecurityUser;
import org.springframework.security.core.Authentication;


public class AuthenticationUtils {
	public static Long getAuthId(Authentication authentication){
		return ((CustomSecurityUser) authentication.getPrincipal()).getId();
	}

	public static void verifyAuthUserPermissions(Long userId, Long authUser){
		if (!authUser.equals(userId)){
			throw new ActionNotPermitted("Action not permitted");
		}
	}
}
