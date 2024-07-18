package org.discord.backend.auth;


import org.discord.backend.auth.UserInfoUserDetails;
import org.discord.backend.entity.User;
import org.discord.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> users =   userRepository.findUsersByEmail(email);
        return users.map(UserInfoUserDetails::new).orElseThrow(()->new UsernameNotFoundException("User not found" + email));

    }
}
