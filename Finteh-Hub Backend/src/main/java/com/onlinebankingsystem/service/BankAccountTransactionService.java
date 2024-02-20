package com.onlinebankingsystem.service;

import java.util.List;

import com.onlinebankingsystem.entity.BankAccountTransaction;

public interface BankAccountTransactionService {

	BankAccountTransaction addBankTransaction(BankAccountTransaction transaction);

	BankAccountTransaction getTransactionById(int id);

	BankAccountTransaction getTransactionByTransactionId(String transactionId);

	List<BankAccountTransaction> getAllTransactions();

	List<BankAccountTransaction> getAllTransactionsByTransactionTime(String startDate, String endDate);

	List<BankAccountTransaction> getAllTransactionsByTransactionTimeAndBankId(String startDate, String endDate,
			int bankId);

	List<BankAccountTransaction> getAllTransactionsByTransactionTimeAndBankAccoountId(String startDate, String endDate,
			int accountId);

	List<BankAccountTransaction> getTransactionsByUserId(int userId);

	List<BankAccountTransaction> findByBankOrderByIdDesc(int bankId);

	List<BankAccountTransaction> getByUserAndTransactionTimeBetweenOrderByIdDesc(int userId, String startDate,
			String endDate);

}
