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

import com.onlinebankingsystem.dto.BankDetailsResponseDto;
import com.onlinebankingsystem.dto.CommonApiResponse;
import com.onlinebankingsystem.dto.RegisterBankRequestDto;
import com.onlinebankingsystem.resource.BankResource;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("api/bank/")
@CrossOrigin(origins = "http://localhost:3000")
public class BankController {

	@Autowired
	private BankResource bankResource;

	// for customer and bank register
	@PostMapping("register")
	@Operation(summary =  "Api to register bank")
	public ResponseEntity<CommonApiResponse> registerBank(@RequestBody RegisterBankRequestDto request) {
		return this.bankResource.registerBank(request);
	}

	// for fetching all the Banks
	@GetMapping("fetch/all")
	@Operation(summary =  "Api to fetch all banks")
	public ResponseEntity<BankDetailsResponseDto> fetchAllBanks() {
		return this.bankResource.fetchAllBanks();
	}

	// for fetching all the Bank by Id
	@GetMapping("fetch/id")
	@Operation(summary =  "Api to fetch bank by id")
	public ResponseEntity<BankDetailsResponseDto> fetchBankById(@RequestParam("bankId") int bankId) {
		return this.bankResource.fetchBankById(bankId);
	}
	
	// for fetching the Bank by using the Bank user Id
	@GetMapping("fetch/user")
	@Operation(summary =  "Api to fetch bank by user id")
	public ResponseEntity<BankDetailsResponseDto> fetchBankByUserId(@RequestParam("userId") int userId) {
		return this.bankResource.fetchBankByUserId(userId);
	}

}
