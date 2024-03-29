package com.futoria.core.application.configuration.security;

import com.futoria.core.model.security.Role;
import com.futoria.core.model.user.User;
import com.futoria.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service("CoreUserDetailsService")
public class FutoriaUserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;

    public FutoriaUserDetailsServiceImpl(@Qualifier("CoreUserRepository")
                                          UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUserRepository(@Qualifier("CoreUserRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        UserDetails userDetails;

        User user = userRepository.getByUsername(username);

        userDetails = new FutoriaUserDetails(
                user,
                true,
                true,
                true,
                true,
                getAuthorities(user)
        );

        return userDetails;
    }

    public static Set<GrantedAuthority> getAuthorities(User user) {
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

        for (Role role : user.getRoles()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRoleName());
            authorities.add(grantedAuthority);
        }

        return authorities;
    }
}