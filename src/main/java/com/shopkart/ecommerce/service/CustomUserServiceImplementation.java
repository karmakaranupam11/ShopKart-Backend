package com.shopkart.ecommerce.service;

import com.shopkart.ecommerce.model.User;
import com.shopkart.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserServiceImplementation implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

        User user = userRepository.findByEmail(username);

        if(user == null){
            throw  new UsernameNotFoundException("user not found with email " + username);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        // The User Class used in the below return statement is not our User class which we created in the model, it
        // is a user class provided by spring security
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),authorities);

    }

}
