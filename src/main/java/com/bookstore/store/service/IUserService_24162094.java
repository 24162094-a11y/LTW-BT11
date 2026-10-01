package com.bookstore.store.service;

import com.bookstore.store.entity.User_24162094;

public interface IUserService_24162094 {
    User_24162094 findByEmail(String email);

    User_24162094 save(User_24162094 user);
}
