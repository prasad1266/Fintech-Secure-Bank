package com.onlinebankingsystem.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onlinebankingsystem.entity.BankAccountTransaction;

@Repository
public interface BankAccountTransactionDao extends JpaRepository<BankAccountTransaction, Integer>{
	
	List<BankAccountTransaction> findByBankAccount_id(int accountId);
	
	BankAccountTransaction findByTransactionId(String transactionId);
	
	List<BankAccountTransaction> findByTransactionTimeBetweenAndBankAccount_IdOrderByIdDesc(String startDate, String endDate, int accountId);
	
	List<BankAccountTransaction> findByTransactionTimeBetweenAndBank_idOrderByIdDesc(String startDate, String endDate, int bankId);
	
	List<BankAccountTransaction> findByTransactionTimeBetweenOrderByIdDesc(String startDate, String endDate);

	List<BankAccountTransaction> findAllByOrderByIdDesc();
	
	List<BankAccountTransaction> findByUser_idOrderByIdDesc(int userId);
	
	List<BankAccountTransaction> findByBank_idOrderByIdDesc(int userId);
	
	List<BankAccountTransaction> findByUser_idAndTransactionTimeBetweenOrderByIdDesc(int userId, String startDate, String endDate);
	
}
