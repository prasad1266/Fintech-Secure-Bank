package com.onlinebankingsystem.dto;

import java.util.ArrayList;
import java.util.List;

import com.onlinebankingsystem.entity.User;

public class UserListResponseDto extends CommonApiResponse {

	private List<User> users = new ArrayList<>();

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}
