package com.xiaobudian.yamikitchen.common.auth;

import com.xiaobudian.yamikitchen.domain.User;
import com.xiaobudian.yamikitchen.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by Johnson on 2015/4/23.
 */
@Component(value = "customAuthenticationProvider")
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Inject
    private PasswordEncoder passwordEncoder;
    @Inject
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = userRepository.findByUsername(name);
        if (user != null && passwordEncoder.matches(password, user.getPassword()))
            return new UsernamePasswordAuthenticationToken(user, password, null);
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
