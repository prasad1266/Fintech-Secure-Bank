package com.onlinebankingsystem.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onlinebankingsystem.entity.User;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
	
	User findByEmailAndPassword(String emailId, String password); 
	User findByEmailAndPasswordAndRoles(String emailId, String password, String role); 
	User findByEmailAndRoles(String emailId, String role);
	User findByEmail(String emailId);
	List<User> findByRolesAndStatus(String role, String status);
	List<User> findByRolesAndStatusAndBank_Id(String role, String status, int bankId);
	List<User> findByRolesAndStatusAndBankIsNull(String role, String status);
	List<User> findByRoles(String role);
	List<User> findByRolesAndBank_Id(String role, int bankId);
	List<User> findByNameContainingIgnoreCaseAndBank_IdAndRoles(String customerName, int bankId, String role);
	List<User> findByNameContainingIgnoreCaseAndRoles(String customerName, String role);

}
