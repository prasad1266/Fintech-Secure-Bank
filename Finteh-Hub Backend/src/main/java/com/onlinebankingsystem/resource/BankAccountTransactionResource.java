package com.onlinebankingsystem.resource;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.lowagie.text.DocumentException;
import com.onlinebankingsystem.dto.BankTransactionRequestDto;
import com.onlinebankingsystem.dto.BankTransactionResponseDto;
import com.onlinebankingsystem.dto.CommonApiResponse;
import com.onlinebankingsystem.entity.Bank;
import com.onlinebankingsystem.entity.BankAccount;
import com.onlinebankingsystem.entity.BankAccountTransaction;
import com.onlinebankingsystem.entity.User;
import com.onlinebankingsystem.exception.BankAccountTransactionException;
import com.onlinebankingsystem.service.BankAccountService;
import com.onlinebankingsystem.service.BankAccountTransactionService;
import com.onlinebankingsystem.service.BankService;
import com.onlinebankingsystem.service.UserService;
import com.onlinebankingsystem.utility.BankStatementDownloader;
import com.onlinebankingsystem.utility.Constants.BankAccountStatus;
import com.onlinebankingsystem.utility.Constants.TransactionNarration;
import com.onlinebankingsystem.utility.Constants.TransactionType;
import com.onlinebankingsystem.utility.Constants.UserStatus;
import com.onlinebankingsystem.utility.DateTimeUtils;
import com.onlinebankingsystem.utility.TransactionIdGenerator;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@Component
public class BankAccountTransactionResource {

	private final Logger LOG = LoggerFactory.getLogger(BankAccountTransactionResource.class);

	@Autowired
	private UserService userService;

	@Autowired
	private BankAccountTransactionService bankAccountTransactionService;

	@Autowired
	private BankAccountService bankAccountService;

	@Autowired
	private BankService bankService;

	@Transactional(rollbackOn = BankAccountTransactionException.class)
	public ResponseEntity<CommonApiResponse> depositAmountTxn(BankTransactionRequestDto request) throws Exception {

		LOG.info("Received request for deposit amount in customer account");

		CommonApiResponse response = new CommonApiResponse();

		if (request == null) {
			response.setResponseMessage("bad request, missing data");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (request.getAmount() == null || request.getSourceBankAccountId() == 0) {
			response.setResponseMessage("bad request, invalid or missing data");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
			response.setResponseMessage("Failed to deposit amount, please select valid amount");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		BankAccount account = this.bankAccountService.findByAccountId(request.getSourceBankAccountId());

		if (account == null) {
			response.setResponseMessage("Bank Account found, enter correct account details!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (!account.getStatus().equals(BankAccountStatus.OPEN.value())) {
			response.setResponseMessage("Bank Account is Locked, Can't Deposit amount");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Bank bank = account.getBank();

		account.setBalance(account.getBalance().add(request.getAmount()));
		BankAccount updateAccount = this.bankAccountService.updateBankAccount(account);

		if (updateAccount == null) {
			response.setResponseMessage("Failed to deposit the amount");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		User user = account.getUser();

		if (!user.getStatus().equals(UserStatus.ACTIVE.value())) {
			response.setResponseMessage("User is not Active, Can't Deposit amount");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		BankAccountTransaction transaction = new BankAccountTransaction();
		transaction.setType(TransactionType.DEPOSIT.value());
		transaction.setBank(bank);
		transaction.setBankAccount(account);
		transaction.setAmount(request.getAmount());
		transaction.setNarration(TransactionNarration.BANK_DEPOSIT.value());
		transaction.setTransactionId(TransactionIdGenerator.generate());
		transaction.setTransactionTime(
				String.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
		transaction.setUser(user);

		BankAccountTransaction addedTxn = this.bankAccountTransactionService.addBankTransaction(transaction);

		if (addedTxn == null) {
			throw new BankAccountTransactionException("Failed to deposit amount in customer account");
		}

		else {
			response.setResponseMessage("Amount Deposited succesful!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
		}
	}

	@Transactional(rollbackOn = BankAccountTransactionException.class)
	public ResponseEntity<CommonApiResponse> withdrawAmountTxn(BankTransactionRequestDto request) throws Exception {

		LOG.info("Received request for withdraw amount from customer account");

		CommonApiResponse response = new CommonApiResponse();

		if (request == null) {
			response.setResponseMessage("bad request, missing data");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (request.getAmount() == null || request.getSourceBankAccountId() == 0) {
			response.setResponseMessage("bad request, invalid or missing data");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
			response.setResponseMessage("Failed to deposit amount, please select valid amount");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		BankAccount account = this.bankAccountService.findByAccountId(request.getSourceBankAccountId());

		if (account == null) {
			response.setResponseMessage("Bank Account found, enter correct account details!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (!account.getStatus().equals(BankAccountStatus.OPEN.value())) {
			response.setResponseMessage("Bank Account is Locked, Can't Withdraw amount");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (account.getBalance().compareTo(request.getAmount()) < 0) {
			response.setResponseMessage("Failed to withdraw amount, insufficient balance in customer account");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Bank bank = account.getBank();

		account.setBalance(account.getBalance().subtract(request.getAmount()));
		BankAccount updateAccount = this.bankAccountService.updateBankAccount(account);

		if (updateAccount == null) {
			response.setResponseMessage("Failed to withdraw the amount");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		User user = account.getUser();

		if (!user.getStatus().equals(UserStatus.ACTIVE.value())) {
			response.setResponseMessage("User is not Active, Can't Withdraw amount");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		BankAccountTransaction transaction = new BankAccountTransaction();
		transaction.setType(TransactionType.WITHDRAW.value());
		transaction.setBank(bank);
		transaction.setBankAccount(account);
		transaction.setAmount(request.getAmount());
		transaction.setNarration(TransactionNarration.BANK_WITHDRAW.value());
		transaction.setTransactionId(TransactionIdGenerator.generate());
		transaction.setTransactionTime(
				String.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
		transaction.setUser(user);

		BankAccountTransaction addedTxn = this.bankAccountTransactionService.addBankTransaction(transaction);

		if (addedTxn == null) {
			throw new BankAccountTransactionException("Failed to withdraw amount from customer account");
		}

		else {
			response.setResponseMessage("Amount Withdraw succesful!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
		}
	}

	@Transactional(rollbackOn = BankAccountTransactionException.class)
	public ResponseEntity<CommonApiResponse> accountTransfer(BankTransactionRequestDto request) throws Exception {

		LOG.info("Received request for customer account transfer");

		CommonApiResponse response = new CommonApiResponse();

		if (request == null) {
			response.setResponseMessage("bad request, missing data");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (request.getUserId() == 0 || request.getBankId() == 0 || request.getAmount() == null
				|| request.getToBankAccount() == null || request.getToBankIfsc() == null) {
			response.setResponseMessage("bad request, invalid or missing data");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
			response.setResponseMessage("Failed to deposit amount, please select valid amount");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User user = this.userService.getUserById(request.getUserId());

		if (user == null) {
			response.setResponseMessage("Sender User not found in Db");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		BankAccount senderAccount = this.bankAccountService.findByUserAndStatus(user.getId(),
				BankAccountStatus.OPEN.value());

		if (senderAccount == null) {
			response.setResponseMessage("No Linked Bank Account found, contact Bank Administrator");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (!senderAccount.getStatus().equals(BankAccountStatus.OPEN.value())) {
			response.setResponseMessage("Bank Account is Locked, Can't Transfer the Amount");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (senderAccount.getBalance().compareTo(request.getAmount()) < 0) {
			response.setResponseMessage("Insufficient Fund, Failed to transfer the amount");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		BankAccount reciepentAccount = this.bankAccountService.findByNumberAndIfscCodeAndStatus(
				request.getToBankAccount(), request.getToBankIfsc(), BankAccountStatus.OPEN.value());

		if (reciepentAccount == null) {
			response.setResponseMessage("Receipent account not found, please enter the correct details and try again");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		senderAccount.setBalance(senderAccount.getBalance().subtract(request.getAmount()));
		BankAccount updateSenderAccount = this.bankAccountService.updateBankAccount(senderAccount);

		if (updateSenderAccount == null) {
			response.setResponseMessage("Failed to transfer the amount");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		reciepentAccount.setBalance(reciepentAccount.getBalance().add(request.getAmount()));
		BankAccount updateReciepentAccount = this.bankAccountService.updateBankAccount(reciepentAccount);

		if (updateReciepentAccount == null) {
			response.setResponseMessage("Failed to transfer the amount");
			response.setSuccess(true);

			throw new BankAccountTransactionException("Failed to transfer the amount");
		}

		BankAccountTransaction transaction = new BankAccountTransaction();
		transaction.setType(TransactionType.ACCOUNT_TRANSFER.value());
		transaction.setBank(senderAccount.getBank());
		transaction.setBankAccount(senderAccount);
		transaction.setDestinationBankAccount(reciepentAccount);
		transaction.setAmount(request.getAmount());
		transaction.setNarration(request.getAccountTransferPurpose());
		transaction.setTransactionId(TransactionIdGenerator.generate());
		transaction.setTransactionTime(
				String.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
		transaction.setUser(user);

		BankAccountTransaction addedTxn = this.bankAccountTransactionService.addBankTransaction(transaction);

		if (addedTxn == null) {
			throw new BankAccountTransactionException("Failed to transfer the amount");
		}

		else {
			response.setResponseMessage("Amount Transfer succesful!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
		}
	}

	public ResponseEntity<BankTransactionResponseDto> bankTransactionHistory(int userId) {

		LOG.info("Received request for fetching the bank transaction history");

		BankTransactionResponseDto response = new BankTransactionResponseDto();

		if (userId == 0) {
			response.setResponseMessage("bad request, missing data");
			response.setSuccess(true);

			return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		User user = this.userService.getUserById(userId);

		if (user == null) {
			response.setResponseMessage("user not found");
			response.setSuccess(true);

			return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<BankAccountTransaction> bankAccountTransactions = new ArrayList<>();

		bankAccountTransactions = this.bankAccountTransactionService.getTransactionsByUserId(user.getId());

		if (bankAccountTransactions.isEmpty()) {
			response.setResponseMessage("No transaction found");
			response.setSuccess(true);

			return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.OK);
		}

		response.setBankTransactions(bankAccountTransactions);
		response.setResponseMessage("Bank Transaction history fetched successfully");
		response.setSuccess(true);

		return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.OK);

	}

	public ResponseEntity<BankTransactionResponseDto> allBankCustomerTransactions() {

		LOG.info("Received request for fetching all bank customer transaction");

		BankTransactionResponseDto response = new BankTransactionResponseDto();

		List<BankAccountTransaction> bankAccountTransactions = new ArrayList<>();

		bankAccountTransactions = this.bankAccountTransactionService.getAllTransactions();

		if (bankAccountTransactions.isEmpty()) {
			response.setResponseMessage("No transaction found");
			response.setSuccess(true);

			return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.OK);
		}

		response.setBankTransactions(bankAccountTransactions);
		response.setResponseMessage("Bank Transactions fetched successfully");
		response.setSuccess(true);

		return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.OK);

	}

	public ResponseEntity<BankTransactionResponseDto> getBankCustomerTransaction(int bankId, String accountNo) {

		LOG.info("Received request for fetching bank customer transaction by account no.");

		BankTransactionResponseDto response = new BankTransactionResponseDto();

		if (bankId == 0 || accountNo == null) {
			response.setResponseMessage("bad request, missing data");
			response.setSuccess(true);

			return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		Bank bank = this.bankService.getBankById(bankId);

		if (bank == null) {
			response.setResponseMessage("bank not found");
			response.setSuccess(true);

			return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<BankAccount> accounts = this.bankAccountService.getByNumberContainingIgnoreCaseAndBank(accountNo, bankId);

		if (CollectionUtils.isEmpty(accounts)) {
			response.setResponseMessage("bank account found");
			response.setSuccess(true);

			return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		BankAccount account = accounts.get(0);

		List<BankAccountTransaction> bankAccountTransactions = new ArrayList<>();

		bankAccountTransactions = this.bankAccountTransactionService.getTransactionsByUserId(account.getUser().getId());

		if (bankAccountTransactions.isEmpty()) {
			response.setResponseMessage("No transaction found");
			response.setSuccess(true);

			return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.OK);
		}

		response.setBankTransactions(bankAccountTransactions);
		response.setResponseMessage("Bank Transactions fetched successfully");
		response.setSuccess(true);

		return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.OK);

	}

	public ResponseEntity<BankTransactionResponseDto> getBankCustomerTransactionByTimeRange(int bankId,
			String accountNo, String startTime, String endTime) {

		LOG.info("Received request for fetching bank customer transaction by account and time range");

		BankTransactionResponseDto response = new BankTransactionResponseDto();

		if (bankId == 0 || accountNo == null) {
			response.setResponseMessage("bad request, missing data");
			response.setSuccess(true);

			return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		Bank bank = this.bankService.getBankById(bankId);

		if (bank == null) {
			response.setResponseMessage("bank not found");
			response.setSuccess(true);

			return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<BankAccount> accounts = this.bankAccountService.getByNumberContainingIgnoreCaseAndBank(accountNo, bankId);

		if (CollectionUtils.isEmpty(accounts)) {
			response.setResponseMessage("bank account found");
			response.setSuccess(true);

			return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		BankAccount account = accounts.get(0);

		List<BankAccountTransaction> bankAccountTransactions = new ArrayList<>();

		bankAccountTransactions = this.bankAccountTransactionService
				.getAllTransactionsByTransactionTimeAndBankAccoountId(startTime, endTime, account.getId());

		if (bankAccountTransactions.isEmpty()) {
			response.setResponseMessage("No transaction found");
			response.setSuccess(true);

			return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.OK);
		}

		response.setBankTransactions(bankAccountTransactions);
		response.setResponseMessage("Bank Transactions fetched successfully");
		response.setSuccess(true);

		return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.OK);

	}

	public ResponseEntity<BankTransactionResponseDto> getBankAllCustomerTransactionByTimeRange(int bankId,
			String startTime, String endTime) {

		LOG.info("Received request for fetching bank customer transaction by account no.");

		BankTransactionResponseDto response = new BankTransactionResponseDto();

		if (bankId == 0) {
			response.setResponseMessage("bad request, missing data");
			response.setSuccess(true);

			return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		Bank bank = this.bankService.getBankById(bankId);

		if (bank == null) {
			response.setResponseMessage("bank not found");
			response.setSuccess(true);

			return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<BankAccountTransaction> bankAccountTransactions = new ArrayList<>();

		bankAccountTransactions = this.bankAccountTransactionService
				.getAllTransactionsByTransactionTimeAndBankId(startTime, endTime, bankId);

		if (bankAccountTransactions.isEmpty()) {
			response.setResponseMessage("No transaction found");
			response.setSuccess(true);

			return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.OK);
		}

		response.setBankTransactions(bankAccountTransactions);
		response.setResponseMessage("Bank Transactions fetched successfully");
		response.setSuccess(true);

		return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.OK);

	}

	public ResponseEntity<BankTransactionResponseDto> getBankAllCustomerTransaction(int bankId) {

		LOG.info("Received request for fetching bank all customer transactions");

		BankTransactionResponseDto response = new BankTransactionResponseDto();

		if (bankId == 0) {
			response.setResponseMessage("bad request, missing data");
			response.setSuccess(true);

			return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		Bank bank = this.bankService.getBankById(bankId);

		if (bank == null) {
			response.setResponseMessage("bank not found");
			response.setSuccess(true);

			return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<BankAccountTransaction> bankAccountTransactions = new ArrayList<>();

		bankAccountTransactions = this.bankAccountTransactionService.findByBankOrderByIdDesc(bankId);

		if (bankAccountTransactions.isEmpty()) {
			response.setResponseMessage("No transaction found");
			response.setSuccess(true);

			return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.OK);
		}

		response.setBankTransactions(bankAccountTransactions);
		response.setResponseMessage("Bank Transactions fetched successfully");
		response.setSuccess(true);

		return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.OK);

	}

	public ResponseEntity<BankTransactionResponseDto> bankTransactionHistoryByTimeRange(int userId, String startTime,
			String endTime) {

		LOG.info("Received request for fetching bank all customer transactions");

		BankTransactionResponseDto response = new BankTransactionResponseDto();

		if (userId == 0) {
			response.setResponseMessage("bad request, missing data");
			response.setSuccess(true);

			return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		User user = this.userService.getUserById(userId);

		if (user == null) {
			response.setResponseMessage("user not found");
			response.setSuccess(true);

			return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		BankAccount account = this.bankAccountService.findByUserAndStatus(user.getId(), BankAccountStatus.OPEN.value());

		if (account == null) {
			response.setResponseMessage("account not linked with user account");
			response.setSuccess(true);

			return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<BankAccountTransaction> bankAccountTransactions = new ArrayList<>();

		bankAccountTransactions = this.bankAccountTransactionService
				.getByUserAndTransactionTimeBetweenOrderByIdDesc(userId, startTime, endTime);

		if (bankAccountTransactions.isEmpty()) {
			response.setResponseMessage("No transaction found");
			response.setSuccess(true);

			return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.OK);
		}

		response.setBankTransactions(bankAccountTransactions);
		response.setResponseMessage("Bank Transactions fetched successfully");
		response.setSuccess(true);

		return new ResponseEntity<BankTransactionResponseDto>(response, HttpStatus.OK);

	}

	public void downloadBankStatement(int accountId, String startTime, String endTime, HttpServletResponse response)
			throws DocumentException, IOException {

		if (accountId == 0 || startTime == null || endTime == null) {
			return;
		}

		List<BankAccountTransaction> bankAccountTransactions = this.bankAccountTransactionService
				.getAllTransactionsByTransactionTimeAndBankAccoountId(startTime, endTime, accountId);

		if (CollectionUtils.isEmpty(bankAccountTransactions)) {
			return;
		}

		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + bankAccountTransactions.get(0).getBankAccount().getNumber()
				+ "_Statement.pdf";
		response.setHeader(headerKey, headerValue);

		BankStatementDownloader exporter = new BankStatementDownloader(bankAccountTransactions,
				DateTimeUtils.getProperDateTimeFormatFromEpochTime(startTime),
				DateTimeUtils.getProperDateTimeFormatFromEpochTime(endTime));
		exporter.export(response);

		return;

	}

}
