package com.example.vendingMachine.configs;

import com.example.vendingMachine.models.Role;
import com.example.vendingMachine.services.security.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private JpaUserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.cors().disable()
				.authorizeRequests()
				.mvcMatchers(HttpMethod.POST,"/users").permitAll()
				.mvcMatchers(HttpMethod.GET,"/products").permitAll()
				.mvcMatchers("/products/**").hasAuthority(Role.SELLER.name())
				.mvcMatchers(HttpMethod.POST,"/deposit").hasAuthority(Role.BUYER.name())
				.mvcMatchers(HttpMethod.POST,"/reset").hasAuthority(Role.BUYER.name())
				.mvcMatchers(HttpMethod.POST,"/buy").hasAuthority(Role.BUYER.name())
				.anyRequest().authenticated()
				.and()
				.httpBasic();
		http
				.sessionManagement()
				.maximumSessions(-1)
				.sessionRegistry(sessionRegistry());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}
}
