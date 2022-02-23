package com.example.vendingMachine.dtos.request;

import com.example.vendingMachine.models.Coin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepositCoinRequest implements Serializable {
	Coin deposit;
}
