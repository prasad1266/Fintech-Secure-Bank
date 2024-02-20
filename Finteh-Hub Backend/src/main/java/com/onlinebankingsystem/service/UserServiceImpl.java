package com.onlinebankingsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinebankingsystem.dao.UserDao;
import com.onlinebankingsystem.entity.Bank;
import com.onlinebankingsystem.entity.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;

	@Override
	public User registerUser(User user) {
		return userDao.save(user);
	}

	@Override
	public User updateUser(User user) {
		return userDao.save(user);
	}

	@Override
	public User getUserById(int userId) {
		return userDao.findById(userId).get();
	}

	@Override
	public User getUserByEmailAndPassword(String email, String password) {
		return userDao.findByEmailAndPassword(email, password);
	}

	@Override
	public User getUserByEmailAndPasswordAndRoles(String email, String password, String role) {
		return userDao.findByEmailAndPasswordAndRoles(email, password, role);
	}

	@Override
	public User getUserByEmail(String email) {
		return userDao.findByEmail(email);
	}

	@Override
	public List<User> getUsersByRolesAndStatus(String role, String status) {
		return userDao.findByRolesAndStatus(role, status);
	}

	@Override
	public User getUserByEmailAndRoles(String email, String role) {
		return userDao.findByEmailAndRoles(email, role);
	}

	@Override
	public List<User> getUsersByRolesAndStatusAndBank(String role, String status, int bankId) {
		return userDao.findByRolesAndStatusAndBank_Id(role, status, bankId);
	}

	@Override
	public List<User> getUserByRoles(String role) {
		return userDao.findByRoles(role);
	}

	@Override
	public List<User> getUsersByRolesAndStatusAndBankIsNull(String role, String status) {
		return userDao.findByRolesAndStatusAndBankIsNull(role, status);
	}

	@Override
	public List<User> getUserByRolesAndBank(String role, int bankid) {
		// TODO Auto-generated method stub
		return userDao.findByRolesAndBank_Id(role, bankid);
	}

	@Override
	public List<User> searchBankCustomerByNameAndRole(String customerName, int bankId,  String role) {
		// TODO Auto-generated method stub
		return userDao.findByNameContainingIgnoreCaseAndBank_IdAndRoles(customerName, bankId, role);
	}

	@Override
	public List<User> searchBankCustomerByNameAndRole(String customerName, String role) {
		// TODO Auto-generated method stub
		return userDao.findByNameContainingIgnoreCaseAndRoles(customerName, role);
	}
	
}
