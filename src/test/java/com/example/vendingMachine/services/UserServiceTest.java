package com.example.vendingMachine.services;

import com.example.vendingMachine.dtos.request.DepositCoinRequest;
import com.example.vendingMachine.models.Coin;
import com.example.vendingMachine.models.User;
import com.example.vendingMachine.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {UserService.class})
public class UserServiceTest {
	@MockBean
	private UserRepository userRepository;
	@MockBean
	private PasswordEncoder passwordEncoder;
    @Autowired
	private UserService userService;

	private static Long AUTH_ID = 1L;

    @Test
    public void testDeposit(){
	    // given
	    DepositCoinRequest request = DepositCoinRequest.builder()
			    .deposit(Coin.TEN)
			    .build();

	    User user = User.builder()
			    .deposit(100L)
			    .build();

	    // when
	    Mockito.when(userRepository.getById(AUTH_ID)).thenReturn(user);
	    Long deposit = userService.depositCoin(request, AUTH_ID);

	    // then
	    Assert.assertEquals(deposit, Long.valueOf(110));
    }

	@Test(expected = EntityNotFoundException.class)
	public void testDepositUserNotFound(){
		// given
		DepositCoinRequest request = DepositCoinRequest.builder()
				.deposit(Coin.TEN)
				.build();

		// when
		Mockito.when(userRepository.getById(AUTH_ID)).thenThrow(EntityNotFoundException.class);
	}

}