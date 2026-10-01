package com.bookstore.store.service;

import com.bookstore.store.dao.IUserDAO_24162094;
import com.bookstore.store.entity.User_24162094;

public class UserServiceImpl_24162094 implements IUserService_24162094 {
    private final IUserDAO_24162094 userDAO;

    public UserServiceImpl_24162094(IUserDAO_24162094 userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User_24162094 findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    @Override
    public User_24162094 save(User_24162094 user) {
        return userDAO.save(user);
    }
}
