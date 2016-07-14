package com.qhkj.seed.repository;

import com.qhkj.seed.models.User;

public interface UserRepo extends BaseRepo<User, String> {
	public User findByUsername(String username);
}
