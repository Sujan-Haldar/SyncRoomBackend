package org.discord.backend.auth;


import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserInfoUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> users =   userRepository.findFirstByEmail(email);
        return users.map(UserInfoUserDetails::new).orElseThrow(()->new UsernameNotFoundException("User not found" + email));
    }
}
