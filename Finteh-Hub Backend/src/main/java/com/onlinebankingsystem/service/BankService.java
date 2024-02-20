package com.onlinebankingsystem.service;

import java.util.List;

import com.onlinebankingsystem.entity.Bank;

public interface BankService {
	
	Bank getBankById(int bankId);
	Bank addBank(Bank bank);
	Bank updateBank(Bank bank);
	List<Bank> getAllBank();

}
