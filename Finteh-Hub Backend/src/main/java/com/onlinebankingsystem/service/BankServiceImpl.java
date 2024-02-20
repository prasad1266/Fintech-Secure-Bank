package com.onlinebankingsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinebankingsystem.dao.BankDao;
import com.onlinebankingsystem.entity.Bank;

@Service
public class BankServiceImpl implements BankService {
	
	@Autowired
	private BankDao bankDao;

	@Override
	public Bank getBankById(int bankId) {
		return this.bankDao.findById(bankId).get();
	}

	@Override
	public Bank addBank(Bank bank) {
		return this.bankDao.save(bank);
	}

	@Override
	public Bank updateBank(Bank bank) {
		return this.bankDao.save(bank);
	}

	@Override
	public List<Bank> getAllBank() {
		return this.bankDao.findAll();
	}

}
