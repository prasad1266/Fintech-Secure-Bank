package com.onlinebankingsystem.service;

import java.util.List;

import com.onlinebankingsystem.entity.Bank;
import com.onlinebankingsystem.entity.User;

public interface UserService {
	
	User registerUser(User user);
	User updateUser(User user);
	User getUserById(int userId);
	User getUserByEmailAndPassword(String email, String password);
	User getUserByEmailAndPasswordAndRoles(String email, String password, String role);
	User getUserByEmail(String email);
	User getUserByEmailAndRoles(String email, String roles);
	List<User> getUsersByRolesAndStatus(String role, String status);
	List<User> getUsersByRolesAndStatusAndBank(String role, String status, int bankId);
	List<User> getUserByRoles(String role);
	List<User> getUsersByRolesAndStatusAndBankIsNull(String role, String status);
	List<User> getUserByRolesAndBank(String role, int bankId);
	List<User> searchBankCustomerByNameAndRole(String customerName, int bankId, String role);
	List<User> searchBankCustomerByNameAndRole(String customerName, String role);

}
