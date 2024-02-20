package com.onlinebankingsystem.dto;

import java.util.List;

import com.onlinebankingsystem.entity.BankAccount;

public class BankAccountResponseDto extends CommonApiResponse {

	private List<BankAccount> accounts;

	public List<BankAccount> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<BankAccount> accounts) {
		this.accounts = accounts;
	}

}
