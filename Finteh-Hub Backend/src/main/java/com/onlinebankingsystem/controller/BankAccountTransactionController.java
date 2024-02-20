package com.onlinebankingsystem.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lowagie.text.DocumentException;
import com.onlinebankingsystem.dto.BankTransactionRequestDto;
import com.onlinebankingsystem.dto.BankTransactionResponseDto;
import com.onlinebankingsystem.dto.CommonApiResponse;
import com.onlinebankingsystem.resource.BankAccountTransactionResource;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/bank/transaction")
@CrossOrigin(origins = "http://localhost:3000")
public class BankAccountTransactionController {

	@Autowired
	private BankAccountTransactionResource bankAccountTransactionResource;

	@PostMapping("deposit")
	@Operation(summary =  "Api for Bank transaction deposit")
	public ResponseEntity<CommonApiResponse> bankDepositTransaction(@RequestBody BankTransactionRequestDto request)
			throws Exception {
		return this.bankAccountTransactionResource.depositAmountTxn(request);
	}

	@PostMapping("withdraw")
	@Operation(summary =  "Api for Bank transaction withdraw")
	public ResponseEntity<CommonApiResponse> bankWithdrawTransaction(@RequestBody BankTransactionRequestDto request)
			throws Exception {
		return this.bankAccountTransactionResource.withdrawAmountTxn(request);
	}

	@PostMapping("account/transfer")
	@Operation(summary =  "Api for Bank Account transfer")
	public ResponseEntity<CommonApiResponse> accountTransferTransaction(@RequestBody BankTransactionRequestDto request)
			throws Exception {
		return this.bankAccountTransactionResource.accountTransfer(request);
	}

	@GetMapping("history")
	@Operation(summary =  "Api for fetch bank transaction history")
	public ResponseEntity<BankTransactionResponseDto> getUserBankTransactionHistory(
			@RequestParam("userId") int userId) {
		return this.bankAccountTransactionResource.bankTransactionHistory(userId);
	}

	@GetMapping("all")
	@Operation(summary =  "Api for fetch bank transaction history")
	public ResponseEntity<BankTransactionResponseDto> getAllBankCustomerTransactions() {
		return this.bankAccountTransactionResource.allBankCustomerTransactions();
	}

	@GetMapping("customer/fetch")
	@Operation(summary =  "Api for fetch bank transaction history")
	public ResponseEntity<BankTransactionResponseDto> getBankCustomerTransaction(@RequestParam("bankId") int bankId,
			@RequestParam("accountNo") String accountNo) {
		return this.bankAccountTransactionResource.getBankCustomerTransaction(bankId, accountNo);
	}

	@GetMapping("customer/fetch/timerange")
	@Operation(summary =  "Api for fetch bank customer transaction history by time range")
	public ResponseEntity<BankTransactionResponseDto> getBankCustomerTransactionByTimeRange(
			@RequestParam("bankId") int bankId, @RequestParam("accountNo") String accountNo,
			@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
		return this.bankAccountTransactionResource.getBankCustomerTransactionByTimeRange(bankId, accountNo, startTime,
				endTime);
	}

	@GetMapping("all/customer/fetch/timerange")
	@Operation(summary =  "Api for fetch bank all customer transaction history")
	public ResponseEntity<BankTransactionResponseDto> getBankAllCustomerTransactionsByTimeRange(
			@RequestParam("bankId") int bankId, @RequestParam("startTime") String startTime,
			@RequestParam("endTime") String endTime) {
		return this.bankAccountTransactionResource.getBankAllCustomerTransactionByTimeRange(bankId, startTime, endTime);
	}

	@GetMapping("all/customer/fetch")
	@Operation(summary =  "Api for fetch bank all customer tranctions")
	public ResponseEntity<BankTransactionResponseDto> getBankAllCustomerTransaction(
			@RequestParam("bankId") int bankId) {
		return this.bankAccountTransactionResource.getBankAllCustomerTransaction(bankId);
	}

	@GetMapping("history/timerange")
	@Operation(summary =  "Api for fetch customer transactions by time range")
	public ResponseEntity<BankTransactionResponseDto> getCustomerTransactionsByTimeRange(
			@RequestParam("userId") int userId, @RequestParam("startTime") String startTime,
			@RequestParam("endTime") String endTime) {
		return this.bankAccountTransactionResource.bankTransactionHistoryByTimeRange(userId, startTime, endTime);
	}

	@GetMapping("statement/download")
	@Operation(summary =  "Api for downloading the Bank Statement using account Id")
	public void downloadBankStatement(@RequestParam("accountId") int accountId,
			@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
			HttpServletResponse response) throws DocumentException, IOException {
		this.bankAccountTransactionResource.downloadBankStatement(accountId, startTime, endTime, response);
	}

}
