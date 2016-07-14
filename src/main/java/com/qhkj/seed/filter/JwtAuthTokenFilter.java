package com.qhkj.seed.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import com.qhkj.seed.services.UserService;
import com.qhkj.seed.utils.JwtTokenHelper;

public class JwtAuthTokenFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenHelper jwtTokenUtil;

    @Value("${jwt.authType}")
    private String authType;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	
    	HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authToken = httpRequest.getHeader("Authorization");
        if (authToken != null && authToken.startsWith(authType) && authToken.length() > (authType.length()+1)){
            authToken = authToken.substring(authType.length()+1);
            String username = jwtTokenUtil.getUsernameFromToken(authToken);
            
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            	try {
	                UserDetails userDetails = userService.loadUserByUsername(username);
	            
	                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
	                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	                    // setDetails 可省略
	                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
	                    SecurityContextHolder.getContext().setAuthentication(authentication);
	                }
            	}catch(UsernameNotFoundException e){
            		logger.info(e.getMessage());
            	}
            }
        }
       
        chain.doFilter(request, response);
    }
}
