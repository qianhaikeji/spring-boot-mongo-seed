package com.cx.szsh.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import com.cx.szsh.utils.QidiHelper;
import com.nqsky.meap.api.response.userCenter.UserInfo;

public class QidiAuthTokenFilter extends UsernamePasswordAuthenticationFilter {
	@Autowired
    private UserDetailsService userDetailsService;

	@Autowired
	private QidiHelper qidiTokenUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	
    	HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authToken = httpRequest.getHeader("Authorization");
        
        String appKey = null;
        String appSecret = null;
        Cookie[] cookies = httpRequest.getCookies();
        for (Cookie cookie : cookies){
        	if ("appKey".equals(cookie.getName())){
        		appKey = cookie.getValue();
        	} else if ("appSecret".equals(cookie.getName())){
        		appSecret = cookie.getValue();
        	}
        }
        
        UserInfo userInfo = qidiTokenUtil.authenticate(authToken, appKey, appSecret);
        
        if (userInfo.isSuccess() && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userInfo.getUserAccount().getUserName());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        chain.doFilter(request, response);
    }
}
