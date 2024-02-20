package com.onlinebankingsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinebankingsystem.dao.BankAccountTransactionDao;
import com.onlinebankingsystem.entity.BankAccountTransaction;

@Service
public class BankAccountTransactionServiceImpl implements BankAccountTransactionService {

	@Autowired
	private BankAccountTransactionDao bankAccountTransactionDao;

	@Override
	public BankAccountTransaction addBankTransaction(BankAccountTransaction transaction) {
		return bankAccountTransactionDao.save(transaction);
	}

	@Override
	public BankAccountTransaction getTransactionById(int id) {
		return bankAccountTransactionDao.findById(id).get();
	}

	@Override
	public BankAccountTransaction getTransactionByTransactionId(String transactionId) {
		return bankAccountTransactionDao.findByTransactionId(transactionId);
	}

	@Override
	public List<BankAccountTransaction> getAllTransactions() {
		return bankAccountTransactionDao.findAllByOrderByIdDesc();
	}

	@Override
	public List<BankAccountTransaction> getAllTransactionsByTransactionTime(String startDate, String endDate) {
		return bankAccountTransactionDao.findByTransactionTimeBetweenOrderByIdDesc(startDate, endDate);
	}

	@Override
	public List<BankAccountTransaction> getAllTransactionsByTransactionTimeAndBankId(String startDate, String endDate,
			int bankId) {
		return bankAccountTransactionDao.findByTransactionTimeBetweenAndBank_idOrderByIdDesc(startDate, endDate,
				bankId);
	}

	@Override
	public List<BankAccountTransaction> getAllTransactionsByTransactionTimeAndBankAccoountId(String startDate,
			String endDate, int accountId) {
		return bankAccountTransactionDao.findByTransactionTimeBetweenAndBankAccount_IdOrderByIdDesc(startDate, endDate,
				accountId);
	}

	@Override
	public List<BankAccountTransaction> getTransactionsByUserId(int userId) {
		return bankAccountTransactionDao.findByUser_idOrderByIdDesc(userId);
	}

	@Override
	public List<BankAccountTransaction> findByBankOrderByIdDesc(int bankId) {
		return bankAccountTransactionDao.findByBank_idOrderByIdDesc(bankId);
	}

	@Override
	public List<BankAccountTransaction> getByUserAndTransactionTimeBetweenOrderByIdDesc(int userId, String startDate,
			String endDate) {
		return bankAccountTransactionDao.findByUser_idAndTransactionTimeBetweenOrderByIdDesc(userId, startDate,
				endDate);
	}

}
