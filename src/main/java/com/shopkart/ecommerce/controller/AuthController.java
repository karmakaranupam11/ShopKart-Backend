package com.shopkart.ecommerce.controller;

import com.shopkart.ecommerce.config.JwtProvider;
import com.shopkart.ecommerce.exception.UserException;
import com.shopkart.ecommerce.model.User;
import com.shopkart.ecommerce.repository.UserRepository;
import com.shopkart.ecommerce.request.LoginRequest;
import com.shopkart.ecommerce.response.AuthResponse;
import com.shopkart.ecommerce.service.CustomUserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    // This is for Authentications

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserServiceImplementation customUserServiceImplementation;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {

        String email = user.getEmail();
        String password = user.getPassword();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        // Check if email already exists
        User isEmailExist = userRepository.findByEmail(email);

        if(isEmailExist != null){
            throw  new UserException("Email is Already Used");
        }

        // if the user does not exists create a user
        User createdUser = new User();

        createdUser.setEmail(email);
        // Hash the password and store
        createdUser.setPassword(passwordEncoder.encode(password));
        createdUser.setFirstName(firstName);
        createdUser.setLastName(lastName);

        // Save the user
        User savedUser = userRepository.save(createdUser);

        // Authentication
        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(),savedUser.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // generate token
        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();

        authResponse.setJwt(token);
        authResponse.setMessage("Sign Up Success");

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest){

        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(username,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // generate token
        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Sign In Success");

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);

    }

    private Authentication authenticate(String username, String password){

        UserDetails userDetails = customUserServiceImplementation.loadUserByUsername(username);

        // Check if the user exists
        if(userDetails == null){

            throw new BadCredentialsException("Invalid Username");

        }

        // Match the password and the encoded password from the database for the user
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("Invalid Password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null , userDetails.getAuthorities());

    }

}
