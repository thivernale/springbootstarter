package org.thivernale.springbootstarter.security.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thivernale.springbootstarter.security.UserRepository;
import org.thivernale.springbootstarter.security.models.CustomUserDetails;
import org.thivernale.springbootstarter.security.models.User;

/**
 * Service can look up users from any data source (hard-coded values, text file, any database etc.)
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> dbUser = userRepository.findByUserName(userName);

        dbUser.orElseThrow(() -> new UsernameNotFoundException("Not found: " + userName));

        return dbUser.map(CustomUserDetails::new).get();
    }
}
