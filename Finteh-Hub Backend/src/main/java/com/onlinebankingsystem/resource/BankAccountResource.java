package com.onlinebankingsystem.resource;

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

import com.onlinebankingsystem.dto.AddBankAccountRequestDto;
import com.onlinebankingsystem.dto.BankAccountResponseDto;
import com.onlinebankingsystem.dto.BankAccountStatusUpdateRequestDto;
import com.onlinebankingsystem.dto.CommonApiResponse;
import com.onlinebankingsystem.entity.Bank;
import com.onlinebankingsystem.entity.BankAccount;
import com.onlinebankingsystem.entity.User;
import com.onlinebankingsystem.service.BankAccountService;
import com.onlinebankingsystem.service.BankService;
import com.onlinebankingsystem.service.UserService;
import com.onlinebankingsystem.utility.Constants.BankAccountStatus;
import com.onlinebankingsystem.utility.Constants.IsAccountLinked;

@Component
public class BankAccountResource {
	
	private final Logger LOG = LoggerFactory.getLogger(BankAccountResource.class);
	
	@Autowired
	private BankAccountService bankAccountService;
	
	@Autowired
	private BankService bankService;
	
	@Autowired
	private UserService userService;
	
	public ResponseEntity<CommonApiResponse> addBankAccount(AddBankAccountRequestDto request) {

		LOG.info("Received request for add bank account");

		CommonApiResponse response = new CommonApiResponse();

		if (request == null) {
			response.setResponseMessage("bad request, missing data");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(request.getUserId() == 0) {
			response.setResponseMessage("bad request, user id is null");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}
		
        if(request.getBankId() == 0) {
			response.setResponseMessage("bad request, bank id is null");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}
        
        BankAccount account = AddBankAccountRequestDto.toBankAccountEntity(request);
        
        User user = this.userService.getUserById(request.getUserId());
        account.setUser(user);
        
        Bank bank = this.bankService.getBankById(request.getBankId());
        account.setBank(bank);
        
        account.setStatus(BankAccountStatus.OPEN.value());
        account.setCreationDate(String.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        account.setBalance(BigDecimal.ZERO);
        
        BankAccount addedBankAccount = this.bankAccountService.addBankAccount(account);
        
        if(addedBankAccount != null) {
        	
        	user.setIsAccountLinked(IsAccountLinked.YES.value());
        	this.userService.updateUser(user);
        	
			response.setResponseMessage("Bank Account Created Successfully!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);    	 
        } else {
			response.setResponseMessage("Failed to add the bank account");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
        }

	}
	
	public ResponseEntity<BankAccountResponseDto> fetchAllBankAccounts() { 
		
		LOG.info("Received request for fetching all the bank accounts");

		BankAccountResponseDto response = new BankAccountResponseDto();
		
		List<BankAccount> accounts = new ArrayList<>();
		
		accounts = this.bankAccountService.getAllBankAccouts();
		
		response.setAccounts(accounts);
		response.setResponseMessage("Bank Accounts Fetch Successfully!!!");
		response.setSuccess(true);

		return new ResponseEntity<BankAccountResponseDto>(response, HttpStatus.OK);    	 
   
	}
	
    public ResponseEntity<BankAccountResponseDto> fetchBankAccountByBank(int bankId) { 
		
		LOG.info("Received request for fetching all the bank accounts from bank side");

		BankAccountResponseDto response = new BankAccountResponseDto();
		
		List<BankAccount> accounts = new ArrayList<>();
		
		if(bankId == 0) {
			response.setResponseMessage("bad request, bank id is missing");
			response.setSuccess(true);

			return new ResponseEntity<BankAccountResponseDto>(response, HttpStatus.BAD_REQUEST);	
		}
		
		accounts = this.bankAccountService.getByBank(bankId);
		
		response.setAccounts(accounts);
		response.setResponseMessage("Bank Accounts Fetch Successfully!!!");
		response.setSuccess(true);

		return new ResponseEntity<BankAccountResponseDto>(response, HttpStatus.OK);    	 
   
	}
    
    public ResponseEntity<BankAccountResponseDto> fetchBankAccountById(int accountId) { 
		
		LOG.info("Received request for fetching bank by using account Id");

		BankAccountResponseDto response = new BankAccountResponseDto();
		
		List<BankAccount> accounts = new ArrayList<>();
		
		if(accountId == 0) {
			response.setResponseMessage("bad request, account id is missing");
			response.setSuccess(true);

			return new ResponseEntity<BankAccountResponseDto>(response, HttpStatus.BAD_REQUEST);	
		}
		
		BankAccount account = this.bankAccountService.getBankAccountById(accountId);
		
		if(account == null) {
			response.setAccounts(accounts);
			response.setResponseMessage("Bank account not found with this account id");
			response.setSuccess(true);
			return new ResponseEntity<BankAccountResponseDto>(response, HttpStatus.BAD_REQUEST);
		}
		
		accounts.add(account);

		response.setAccounts(accounts);
		response.setResponseMessage("Bank Accounts Fetch Successfully!!!");
		response.setSuccess(true);

		return new ResponseEntity<BankAccountResponseDto>(response, HttpStatus.OK);    	 
   
	}
    
    public ResponseEntity<BankAccountResponseDto> fetchBankAccountByUserId(int userId) { 
		
		LOG.info("Received request for fetching bank by using User Id");

		BankAccountResponseDto response = new BankAccountResponseDto();
		
		List<BankAccount> accounts = new ArrayList<>();
		
		if(userId == 0) {
			response.setResponseMessage("bad request, user id is missing");
			response.setSuccess(true);

			return new ResponseEntity<BankAccountResponseDto>(response, HttpStatus.BAD_REQUEST);	
		}
		
		BankAccount account = this.bankAccountService.getBankAccountByUser(userId);
		
		if(account == null) {
			response.setResponseMessage("No Bank Account found for User");
			response.setSuccess(true);
			return new ResponseEntity<BankAccountResponseDto>(response, HttpStatus.BAD_REQUEST);
		}
		
		accounts.add(account);

		response.setAccounts(accounts);
		response.setResponseMessage("Bank Accounts Fetch Successfully!!!");
		response.setSuccess(true);

		return new ResponseEntity<BankAccountResponseDto>(response, HttpStatus.OK);    	 
   
	}
    
    public ResponseEntity<BankAccountResponseDto> searchBankAccounts(String accountNumber, int bankId) { 
		
		LOG.info("Received request for searching the Bank account from Bank side");

		BankAccountResponseDto response = new BankAccountResponseDto();
		
		List<BankAccount> accounts = new ArrayList<>();
		
		if(bankId == 0 || accountNumber == null) {
			response.setResponseMessage("bad request, missing data");
			response.setSuccess(true);

			return new ResponseEntity<BankAccountResponseDto>(response, HttpStatus.BAD_REQUEST);	
		}
		
		accounts = this.bankAccountService.getByNumberContainingIgnoreCaseAndBank(accountNumber, bankId);

		response.setAccounts(accounts);
		response.setResponseMessage("Bank Accounts Fetch Successfully!!!");
		response.setSuccess(true);

		return new ResponseEntity<BankAccountResponseDto>(response, HttpStatus.OK);    	 
   
	}

	public ResponseEntity<CommonApiResponse> updateBankAccountStatus(BankAccountStatusUpdateRequestDto request) { 
		
		LOG.info("Received request for updating the Bank Account");

		CommonApiResponse response = new CommonApiResponse();
		
		if(request == null) {
			response.setResponseMessage("bad request, missing data");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(request.getAccountId() == 0) {
			response.setResponseMessage("bad request, account id is missing");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}
		
		
		
		BankAccount account = null;
		account = this.bankAccountService.getBankAccountById(request.getAccountId());
		
        account.setStatus(request.getStatus());
        
        BankAccount updateBankAccount = this.bankAccountService.updateBankAccount(account);
		
        if(updateBankAccount != null) {
        	response.setResponseMessage("Bank Account "+request.getStatus()+" Successfully!!!");
    		response.setSuccess(true);
    		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
        } else {
        	response.setResponseMessage("Failed to "+request.getStatus() +" the account");
    		response.setSuccess(true);
    		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
	}

	public ResponseEntity<BankAccountResponseDto> searchBankAccounts(String accountNumber) { 
		
		LOG.info("Received request for searching the Bank account from Admin side");

		BankAccountResponseDto response = new BankAccountResponseDto();
		
		List<BankAccount> accounts = new ArrayList<>();
		
		if(accountNumber == null) {
			response.setResponseMessage("bad request, missing data");
			response.setSuccess(true);

			return new ResponseEntity<BankAccountResponseDto>(response, HttpStatus.BAD_REQUEST);	
		}
		
		accounts = this.bankAccountService.getByNumberContainingIgnoreCase(accountNumber);

		response.setAccounts(accounts);
		response.setResponseMessage("Bank Accounts Fetch Successfully!!!");
		response.setSuccess(true);

		return new ResponseEntity<BankAccountResponseDto>(response, HttpStatus.OK);    	 
   
	}

}
