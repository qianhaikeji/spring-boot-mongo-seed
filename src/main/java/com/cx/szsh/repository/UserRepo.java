package com.cx.szsh.repository;

import com.cx.szsh.models.User;

public interface UserRepo extends BaseRepo<User, String> {
	public User findByUsername(String username);
}
