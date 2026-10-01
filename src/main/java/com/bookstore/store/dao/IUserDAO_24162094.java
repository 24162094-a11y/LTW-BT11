package com.bookstore.store.dao;

import com.bookstore.store.entity.User_24162094;

public interface IUserDAO_24162094 {
    User_24162094 findByEmail(String email);

    User_24162094 save(User_24162094 user);
}
