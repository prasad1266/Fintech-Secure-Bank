package com.onlinebankingsystem.dto;

import java.util.ArrayList;
import java.util.List;

import com.onlinebankingsystem.entity.BankAccountTransaction;

public class BankTransactionResponseDto extends CommonApiResponse {

	private List<BankAccountTransaction> bankTransactions = new ArrayList<>();

	public List<BankAccountTransaction> getBankTransactions() {
		return bankTransactions;
	}

	public void setBankTransactions(List<BankAccountTransaction> bankTransactions) {
		this.bankTransactions = bankTransactions;
	};

}
