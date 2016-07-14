package com.qhkj.seed.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.qhkj.seed.models.User;
import com.qhkj.seed.services.UserService;

/**
 * 自定义登录验证方法
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired UserService userService;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String)authentication.getPrincipal();
        User user = userService.getUser(username);
        if (user == null) {
            System.out.println("null");
            throw new BadCredentialsException(String.format("用户 '%s' 不存在.", username));
        }
        
        String additionInfo = (String) authentication.getDetails();
        
        if (passwordEncoder.matches((String)authentication.getCredentials(), user.getPassword())){
            return new UsernamePasswordAuthenticationToken(user, null, AuthUserFactory.create(user).getAuthorities());
        } else {
            throw new BadCredentialsException("密码错误！");
        }
        
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}