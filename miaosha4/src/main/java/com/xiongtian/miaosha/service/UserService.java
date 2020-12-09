package com.xiongtian.miaosha.service;

import com.xiongtian.miaosha.domain.User;

public interface UserService {

    public User getUserById(int id);

    public boolean tx(int i);
}
