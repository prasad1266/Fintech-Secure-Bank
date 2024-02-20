package com.onlinebankingsystem.dto;

import java.math.BigDecimal;

public class BankTransactionRequestDto {

	private int userId;

	private int bankId;

	private BigDecimal amount;

	private int sourceBankAccountId;

	private String transactionType;

	private String toBankAccount; // for account transfer

	private String toBankIfsc; // for account transfer

	private String accountTransferPurpose;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {
		this.bankId = bankId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public int getSourceBankAccountId() {
		return sourceBankAccountId;
	}

	public void setSourceBankAccountId(int sourceBankAccountId) {
		this.sourceBankAccountId = sourceBankAccountId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getToBankAccount() {
		return toBankAccount;
	}

	public void setToBankAccount(String toBankAccount) {
		this.toBankAccount = toBankAccount;
	}

	public String getToBankIfsc() {
		return toBankIfsc;
	}

	public void setToBankIfsc(String toBankIfsc) {
		this.toBankIfsc = toBankIfsc;
	}

	public String getAccountTransferPurpose() {
		return accountTransferPurpose;
	}

	public void setAccountTransferPurpose(String accountTransferPurpose) {
		this.accountTransferPurpose = accountTransferPurpose;
	}

}
