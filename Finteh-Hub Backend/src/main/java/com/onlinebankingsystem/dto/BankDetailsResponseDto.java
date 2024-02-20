package com.onlinebankingsystem.dto;

import java.util.ArrayList;
import java.util.List;

import com.onlinebankingsystem.entity.Bank;

public class BankDetailsResponseDto extends CommonApiResponse {

	private List<Bank> banks = new ArrayList<>();

	public List<Bank> getBanks() {
		return banks;
	}

	public void setBanks(List<Bank> banks) {
		this.banks = banks;
	}

}
