package com.booking.hotelapplication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.booking.hotelapplication.exception.OurException;
import com.booking.hotelapplication.repo.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)  throws UsernameNotFoundException{
        // Logic to fetch user details from the database
        return userRepository.findByEmail(username).orElseThrow(()-> new OurException("Username/Email not Found")); // Replace with actual user details
    }

}
