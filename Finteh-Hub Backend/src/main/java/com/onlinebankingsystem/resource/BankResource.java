package com.onlinebankingsystem.resource;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.onlinebankingsystem.dto.BankDetailsResponseDto;
import com.onlinebankingsystem.dto.CommonApiResponse;
import com.onlinebankingsystem.dto.RegisterBankRequestDto;
import com.onlinebankingsystem.entity.Bank;
import com.onlinebankingsystem.entity.User;
import com.onlinebankingsystem.service.BankService;
import com.onlinebankingsystem.service.UserService;
import com.onlinebankingsystem.utility.Constants.UserRole;

import jakarta.transaction.Transactional;

@Component
public class BankResource {

	private final Logger LOG = LoggerFactory.getLogger(BankResource.class);

	@Autowired
	private BankService bankService;

	@Autowired
	private UserService userService;

	@Transactional
	public ResponseEntity<CommonApiResponse> registerBank(RegisterBankRequestDto request) {

		LOG.info("Received request for register bank");

		CommonApiResponse response = new CommonApiResponse();

		if (request == null) {
			response.setResponseMessage("bad request, missing data");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (request.getUserId() == 0) {
			response.setResponseMessage("bad request, Bank user not selected");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User bankUser = this.userService.getUserById(request.getUserId());

		if (bankUser == null || !bankUser.getRoles().equals(UserRole.ROLE_BANK.value())) {
			response.setResponseMessage("bad request, selected bank is not Bank user");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Bank bank = RegisterBankRequestDto.toBankEntity(request);

		Bank registeredBank = this.bankService.addBank(bank);

		if (registeredBank == null) {
			response.setResponseMessage("failed to register the bank!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		bankUser.setBank(registeredBank);

		User updatedUser = this.userService.updateUser(bankUser);

		if (updatedUser == null) {

		}

		response.setResponseMessage("Bank Registered Successful!!!");
		response.setSuccess(true);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
	}

	public ResponseEntity<BankDetailsResponseDto> fetchAllBanks() {

		LOG.info("Received request for fetching all the banks");

		BankDetailsResponseDto response = new BankDetailsResponseDto();

		List<Bank> banks = new ArrayList<>();

		banks = this.bankService.getAllBank();

		response.setBanks(banks);
		response.setResponseMessage("Banks Fetch successful!!!");
		response.setSuccess(true);
		return new ResponseEntity<BankDetailsResponseDto>(response, HttpStatus.OK);

	}
	
	public ResponseEntity<BankDetailsResponseDto> fetchBankById(int bankId) {

		LOG.info("Received request for fetching bank by Id");

		BankDetailsResponseDto response = new BankDetailsResponseDto();

		if(bankId == 0) { 
			response.setResponseMessage("bad request, bank id is missing");
			response.setSuccess(true);
			return new ResponseEntity<BankDetailsResponseDto>(response, HttpStatus.OK);
		}
		
		List<Bank> banks = new ArrayList<>();

		Bank bank = this.bankService.getBankById(bankId);
		
		if(bank ==  null) { 
			response.setResponseMessage("bank not found in db");
			response.setSuccess(true);
			return new ResponseEntity<BankDetailsResponseDto>(response, HttpStatus.BAD_REQUEST);
		}
		
		banks.add(bank);

		response.setBanks(banks);
		response.setResponseMessage("Banks Fetch successful!!!");
		response.setSuccess(true);
		return new ResponseEntity<BankDetailsResponseDto>(response, HttpStatus.OK);

	}
	
	public ResponseEntity<BankDetailsResponseDto> fetchBankByUserId(int userId) {

		LOG.info("Received request for fetching bank by user Id");

		BankDetailsResponseDto response = new BankDetailsResponseDto();

		if(userId == 0) { 
			response.setResponseMessage("bad request, user id is missing");
			response.setSuccess(true);
			return new ResponseEntity<BankDetailsResponseDto>(response, HttpStatus.OK);
		}
		
		List<Bank> banks = new ArrayList<>();

		User user = this.userService.getUserById(userId);
		
		if(user == null || !user.getRoles().equals(UserRole.ROLE_BANK.value())) { 
			response.setResponseMessage("bad request, user null or not bank user!!!");
			response.setSuccess(true);
			return new ResponseEntity<BankDetailsResponseDto>(response, HttpStatus.BAD_REQUEST);
		}
		
		Bank bank = this.bankService.getBankById(user.getBank().getId());
		
		if(bank ==  null) { 
			response.setResponseMessage("bank not found in db");
			response.setSuccess(true);
			return new ResponseEntity<BankDetailsResponseDto>(response, HttpStatus.BAD_REQUEST);
		}
		
		banks.add(bank);

		response.setBanks(banks);
		response.setResponseMessage("Banks Fetch successful!!!");
		response.setSuccess(true);
		return new ResponseEntity<BankDetailsResponseDto>(response, HttpStatus.OK);

	}

}
