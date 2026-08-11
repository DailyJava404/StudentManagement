package com.example.studentsmanagement.Service;

import com.example.studentsmanagement.Entity.UserInfo;
import com.example.studentsmanagement.Repository.UserRepository;
import com.example.studentsmanagement.Security.UserPrincipal;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository _userRepository;
    public CustomUserDetailsService(UserRepository userRepository) {
        _userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        UserInfo userInfo = _userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new UserPrincipal(userInfo);
    }
}
