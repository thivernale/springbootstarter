package org.thivernale.springbootstarter.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thivernale.springbootstarter.security.models.MyUserDetails;

/**
 * Service can look up users from any data source (hard-coded values, text file, any database etc.)
 */
@Service
public class MysqlUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // return a new instance with passed username
        return new MyUserDetails(username);
    }

}
