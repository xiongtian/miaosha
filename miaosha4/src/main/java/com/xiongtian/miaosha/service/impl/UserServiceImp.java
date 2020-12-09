package com.xiongtian.miaosha.service.impl;

import com.xiongtian.miaosha.dao.UserDao;
import com.xiongtian.miaosha.domain.User;
import com.xiongtian.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    @Transactional
    @Override
    public boolean tx(int i) {

        userDao.insert(new User(2,"2222"));

        //userDao.insert(new User(1,"1111"));

        return true;
    }

}
