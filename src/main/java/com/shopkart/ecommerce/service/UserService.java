package com.shopkart.ecommerce.service;

import com.shopkart.ecommerce.exception.UserException;
import com.shopkart.ecommerce.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {

    public User findUserById(Long userId) throws UserException;

    public User findUserProfileByJwt(String jwt) throws UserException;

    public List<User> findAllUsers();

}
