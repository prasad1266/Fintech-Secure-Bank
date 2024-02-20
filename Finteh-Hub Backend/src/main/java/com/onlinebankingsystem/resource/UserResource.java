package com.onlinebankingsystem.resource;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinebankingsystem.config.CustomUserDetailsService;
import com.onlinebankingsystem.dto.CommonApiResponse;
import com.onlinebankingsystem.dto.RegisterUserRequestDto;
import com.onlinebankingsystem.dto.UserListResponseDto;
import com.onlinebankingsystem.dto.UserLoginRequest;
import com.onlinebankingsystem.dto.UserLoginResponse;
import com.onlinebankingsystem.dto.UserStatusUpdateRequestDto;
import com.onlinebankingsystem.entity.Bank;
import com.onlinebankingsystem.entity.BankAccount;
import com.onlinebankingsystem.entity.User;
import com.onlinebankingsystem.service.BankService;
import com.onlinebankingsystem.service.JwtService;
import com.onlinebankingsystem.service.UserService;
import com.onlinebankingsystem.utility.Constants.IsAccountLinked;
import com.onlinebankingsystem.utility.Constants.UserRole;
import com.onlinebankingsystem.utility.Constants.UserStatus;

@Component
public class UserResource {
	
	private final Logger LOG = LoggerFactory.getLogger(UserResource.class);

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private BankService bankService;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public ResponseEntity<CommonApiResponse> registerUser(RegisterUserRequestDto request) {
		
		LOG.info("Received request for register user");

		CommonApiResponse response = new CommonApiResponse();

		if (request == null) {
			response.setResponseMessage("user is null");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User existingUser = this.userService.getUserByEmail(request.getEmail());

		if (existingUser != null) {
			response.setResponseMessage("User with this Email Id already resgistered!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (request.getRoles() == null ) {
			response.setResponseMessage("bad request ,Role is missing");
			response.setSuccess(true);
			
			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}
		
		Bank bank = null;
		
		User user = RegisterUserRequestDto.toUserEntity(request);
		
		if(request.getRoles().equals(UserRole.ROLE_CUSTOMER.value())) {
			if (request.getBankId() == 0) {
				response.setResponseMessage("bad request ,Bank Id is missing");
				response.setSuccess(true);
				
				return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
			}
			
			bank = this.bankService.getBankById(request.getBankId());
			user.setBank(bank);
			user.setIsAccountLinked(IsAccountLinked.NO.value());
		}

		
		
		String encodedPassword = passwordEncoder.encode(user.getPassword());

		user.setStatus(UserStatus.ACTIVE.value());
		user.setPassword(encodedPassword);
		
		
		existingUser = this.userService.registerUser(user);

		if (existingUser == null) {
			response.setResponseMessage("failed to register user");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		response.setResponseMessage("User registered Successfully");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
	}

	public ResponseEntity<CommonApiResponse> registerAdmin(RegisterUserRequestDto registerRequest) {

		CommonApiResponse response = new CommonApiResponse();

		if (registerRequest == null) {
			response.setResponseMessage("user is null");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (registerRequest.getEmail() == null || registerRequest.getPassword() == null) {
			response.setResponseMessage("missing input");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User existingUser = this.userService.getUserByEmail(registerRequest.getEmail());

		if (existingUser != null) {
			response.setResponseMessage("User already register with this Email");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User user = new User();
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setRoles(UserRole.ROLE_ADMIN.value());
		user.setStatus(UserStatus.ACTIVE.value());
		existingUser = this.userService.registerUser(user);

		if (existingUser == null) {
			response.setResponseMessage("failed to register admin");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		response.setResponseMessage("Admin registered Successfully");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
	}
	
	public ResponseEntity<UserLoginResponse> login(UserLoginRequest loginRequest) {

		UserLoginResponse response = new UserLoginResponse();

		if (loginRequest == null) {
			response.setResponseMessage("Missing Input");
			response.setSuccess(true);

			return new ResponseEntity<UserLoginResponse>(response, HttpStatus.BAD_REQUEST);
		}

		String jwtToken = null;
		User user = null;
		
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmailId(), loginRequest.getPassword()));
		} catch (Exception ex) {
			response.setResponseMessage("Invalid email or password.");
			response.setSuccess(true);
			return new ResponseEntity<UserLoginResponse>(response, HttpStatus.BAD_REQUEST);
		}

		UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getEmailId());

		user = userService.getUserByEmail(loginRequest.getEmailId());

		if (!user.getStatus().equals(UserStatus.ACTIVE.value())) {
			response.setResponseMessage("Failed to login");
			response.setSuccess(true);
			return new ResponseEntity<UserLoginResponse>(response, HttpStatus.BAD_REQUEST);
		}

		for (GrantedAuthority grantedAuthory : userDetails.getAuthorities()) {
			if (grantedAuthory.getAuthority().equals(loginRequest.getRole())) {
				jwtToken = jwtService.generateToken(userDetails.getUsername());
			}
		}

		// user is authenticated
		if (jwtToken != null) {
			response.setUser(user);
			response.setResponseMessage("Logged in sucessful");
			
			response.setSuccess(true);
			response.setJwtToken(jwtToken);
			return new ResponseEntity<UserLoginResponse>(response, HttpStatus.OK);
		} 
		
		else {
			response.setResponseMessage("Failed to login");
			response.setSuccess(true);
			return new ResponseEntity<UserLoginResponse>(response, HttpStatus.BAD_REQUEST);
		}

	}
	
	public ResponseEntity<UserListResponseDto> getUsersByRole(String role) {

		UserListResponseDto response = new UserListResponseDto();

		List<User> users = new ArrayList<>();
		users = this.userService.getUserByRoles(role);
		
		if(!users.isEmpty()) {
			response.setUsers(users);
		}
		
		response.setResponseMessage("User Fetched Successfully");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<UserListResponseDto>(response, HttpStatus.OK);
	}
	
	public ResponseEntity<UserListResponseDto> fetchBankManagers() {

		UserListResponseDto response = new UserListResponseDto();

		List<User> users = new ArrayList<>();
		users = this.userService.getUsersByRolesAndStatusAndBankIsNull(UserRole.ROLE_BANK.value(), UserStatus.ACTIVE.value());
		
		if(!users.isEmpty()) {
			response.setUsers(users);
		}
		
		response.setResponseMessage("User Fetched Successfully");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<UserListResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<CommonApiResponse> updateUserStatus(UserStatusUpdateRequestDto request) { 
		
		LOG.info("Received request for updating the user status");

		CommonApiResponse response = new CommonApiResponse();
		
		if(request == null) {
			response.setResponseMessage("bad request, missing data");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(request.getUserId() == 0) {
			response.setResponseMessage("bad request, user id is missing");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}
		
		
		
		User user = null;
		user = this.userService.getUserById(request.getUserId());
		
		user.setStatus(request.getStatus());
        
		User updatedUser = this.userService.updateUser(user);
		
        if(updatedUser != null) {
        	response.setResponseMessage("User "+request.getStatus()+" Successfully!!!");
    		response.setSuccess(true);
    		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
        } else {
        	response.setResponseMessage("Failed to "+request.getStatus() +" the user");
    		response.setSuccess(true);
    		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    
	}

	public ResponseEntity<UserListResponseDto> fetchBankCustomerByBankId(int bankId) {

		UserListResponseDto response = new UserListResponseDto();

		List<User> users = new ArrayList<>();
		
		users = this.userService.getUserByRolesAndBank(UserRole.ROLE_CUSTOMER.value(), bankId);
		
		if(!users.isEmpty()) {
			response.setUsers(users);
		}
		
		response.setResponseMessage("User Fetched Successfully");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<UserListResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<UserListResponseDto> searchBankCustomer(int bankId, String customerName) {

		UserListResponseDto response = new UserListResponseDto();

		List<User> users = new ArrayList<>();
		
		users = this.userService.searchBankCustomerByNameAndRole(customerName, bankId, UserRole.ROLE_CUSTOMER.value());
		
		if(!users.isEmpty()) {
			response.setUsers(users);
		}
		
		response.setResponseMessage("User Fetched Successfully");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<UserListResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<UserListResponseDto> searchBankCustomer(String customerName) {

		UserListResponseDto response = new UserListResponseDto();

		List<User> users = new ArrayList<>();
		
		users = this.userService.searchBankCustomerByNameAndRole(customerName, UserRole.ROLE_CUSTOMER.value());
		
		if(!users.isEmpty()) {
			response.setUsers(users);
		}
		
		response.setResponseMessage("User Fetched Successfully");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<UserListResponseDto>(response, HttpStatus.OK);
	}

}
