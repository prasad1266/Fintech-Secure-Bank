package com.onlinebankingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onlinebankingsystem.dto.AddBankAccountRequestDto;
import com.onlinebankingsystem.dto.BankAccountResponseDto;
import com.onlinebankingsystem.dto.BankAccountStatusUpdateRequestDto;
import com.onlinebankingsystem.dto.CommonApiResponse;
import com.onlinebankingsystem.resource.BankAccountResource;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("api/bank/account")
@CrossOrigin(origins = "http://localhost:3000")
public class BankAccountController {

	@Autowired
	private BankAccountResource bankAccountResource;

	// for customer and bank register
	@PostMapping("add")
	@Operation(summary = "Api to add bank account")
	public ResponseEntity<CommonApiResponse> addBankAccount(@RequestBody AddBankAccountRequestDto request) {
		return this.bankAccountResource.addBankAccount(request);
	}
	
	@GetMapping("fetch/all")
	@Operation(summary =  "Api to fetch All Bank accounts")
	public ResponseEntity<BankAccountResponseDto> getAllBankAccounts() {
		return this.bankAccountResource.fetchAllBankAccounts();
	}
	
	@GetMapping("fetch/bankwise")
	@Operation(summary =  "Api to fetch Bank accounts")
	public ResponseEntity<BankAccountResponseDto> getBankAccounts(@RequestParam("bankId") int bankId) {
		return this.bankAccountResource.fetchBankAccountByBank(bankId);
	}
	
	@GetMapping("fetch/id")
	@Operation(summary =  "Api to fetch Bank account by account Id")
	public ResponseEntity<BankAccountResponseDto> getBankAccountById(@RequestParam("accountId") int accountId) {
		return this.bankAccountResource.fetchBankAccountById(accountId);
	}
	
	@GetMapping("fetch/user")
	@Operation(summary =  "Api to fetch Bank account by user Id")
	public ResponseEntity<BankAccountResponseDto> getBankAccountByUser(@RequestParam("userId") int userId) {
		return this.bankAccountResource.fetchBankAccountByUserId(userId);
	}
	
	@GetMapping("search")
	@Operation(summary =  "Api to search bank accounts by bank")
	public ResponseEntity<BankAccountResponseDto> searchBankBy(@RequestParam("bankId") int bankId, @RequestParam("accountNumber") String accountNumber) {
		return this.bankAccountResource.searchBankAccounts(accountNumber, bankId);
	}
	
	@PostMapping("update/status")
	@Operation(summary =  "Api to update the bank account status")
	public ResponseEntity<CommonApiResponse>updateBankAccountStatus(@RequestBody BankAccountStatusUpdateRequestDto request) {
		return this.bankAccountResource.updateBankAccountStatus(request);
	}
	
	@GetMapping("search/all")
	@Operation(summary =  "Api to search bank accounts by account no")
	public ResponseEntity<BankAccountResponseDto> searchBankBy(@RequestParam("accountNumber") String accountNumber) {
		return this.bankAccountResource.searchBankAccounts(accountNumber);
	}

}
