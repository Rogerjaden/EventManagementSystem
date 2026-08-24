package com.rogerjaden.eventmanagement.security;

import com.rogerjaden.eventmanagement.entity.User;
import com.rogerjaden.eventmanagement.entity.UserPrincipal;
import com.rogerjaden.eventmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException("user not found");
        }
        return new UserPrincipal(user);
    }
}
