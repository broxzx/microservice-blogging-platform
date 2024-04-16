package com.example.security.security;

import com.example.security.entity.UserEntity;
import com.example.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The UserDetailsServiceImpl class is an implementation of the UserDetailsService interface.
 * It retrieves user details from the UserRepository and constructs a UserDetails object.
 */
@RequiredArgsConstructor
@Service
@Log4j2
@ConditionalOnProperty(prefix = "security", name = "jwt.enabled", matchIfMissing = false)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Retrieves the user details for the given username.
     *
     * @param username the username of the user
     * @return the UserDetails object for the user
     * @throws UsernameNotFoundException if the user with the given username is not found
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("user with username '%s' was not found".formatted(username))
                );

        return User
                .builder()
                .username(user.getUsername())
//                .password(user.getPassword())
//                .authorities(user.getRole().name())
                .build();
    }
}
