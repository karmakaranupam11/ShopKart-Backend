package com.shopkart.ecommerce.repository;

import com.shopkart.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    // for getting the user by email
    public User findByEmail(String email);

}
