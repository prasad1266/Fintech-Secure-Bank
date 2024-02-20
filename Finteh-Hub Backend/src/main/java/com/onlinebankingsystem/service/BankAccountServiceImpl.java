package com.onlinebankingsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinebankingsystem.dao.BankAccountDao;
import com.onlinebankingsystem.entity.BankAccount;

@Service
public class BankAccountServiceImpl implements BankAccountService  {
	
	@Autowired
	private BankAccountDao bankAccountDao;

	@Override
	public BankAccount addBankAccount(BankAccount bankAccount) {
		return this.bankAccountDao.save(bankAccount);
	}
	
	@Override
	public BankAccount updateBankAccount(BankAccount bankAccount) {
		return this.bankAccountDao.save(bankAccount);
	}

	@Override
	public BankAccount getBankAccountById(int bankAccountId) {
		return this.bankAccountDao.findById(bankAccountId).get();
	}

	@Override
	public List<BankAccount> getAllBankAccouts() {
		return this.bankAccountDao.findAll();
	}

	@Override
	public BankAccount findByUserAndStatus(int userId, String status) {
		return this.bankAccountDao.findByUser_IdAndStatus(userId, status);
	}

	@Override
	public List<BankAccount> getByBank(int bankId) {
		return this.bankAccountDao.findByBank_Id(bankId);
	}

	@Override
	public List<BankAccount> getByBankAndStatus(int bankId, String status) {
		return this.bankAccountDao.findByBank_IdAndStatus(bankId, status);
	}

	@Override
	public List<BankAccount> getByStatus(String status) {
		return this.bankAccountDao.findByStatus(status);
	}

	@Override
	public BankAccount findByNumberAndIfscCodeAndBankAndStatus(String accNumber, String ifscCode, int bankId,
			String Status) {
		return this.bankAccountDao.findByNumberAndIfscCodeAndBank_IdAndStatus(accNumber, ifscCode, bankId, Status);
	}

	@Override
	public List<BankAccount> getByNumberContainingIgnoreCaseAndBank(String accountNumber, int bankId) {
		return this.bankAccountDao.findByNumberContainingIgnoreCaseAndBank_Id(accountNumber, bankId);
	}

	@Override
	public BankAccount findByAccountId(int accountId) {
		return this.bankAccountDao.findById(accountId).get();
	}

	@Override
	public BankAccount getBankAccountByUser(int userId) {
		return this.bankAccountDao.findByUser_Id(userId);
	}

	@Override
	public BankAccount findByNumberAndIfscCodeAndStatus(String accNumber, String ifscCode, String Status) {
		return this.bankAccountDao.findByNumberAndIfscCodeAndStatus(accNumber, ifscCode, Status);
	}

	@Override
	public List<BankAccount> getByNumberContainingIgnoreCase(String accountNumber) {
		// TODO Auto-generated method stub
		return this.bankAccountDao.findByNumberContainingIgnoreCase(accountNumber);
	}

}
