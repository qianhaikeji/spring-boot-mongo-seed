package com.cx.szsh.api;

import java.net.URISyntaxException;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.cx.szsh.auth.Authority.AuthorityName;
import com.cx.szsh.auth.JwtAuthReq;
import com.cx.szsh.auth.JwtAuthRsp;
import com.cx.szsh.auth.QidiAuthReq;
import com.cx.szsh.exceptions.ServiceException;
import com.cx.szsh.models.BaseQueryParams;
import com.cx.szsh.models.User;
import com.cx.szsh.services.UserService;
import com.cx.szsh.utils.JwtTokenHelper;
import com.cx.szsh.utils.PATCH;
import com.cx.szsh.utils.QidiHelper;
import com.cx.szsh.utils.RestfulHelper;
import com.nqsky.meap.api.response.userCenter.UserInfo;

@Component
@Path("/admin")
public class AdminRest extends BaseRest {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private QidiHelper qidiTokenUtil;
	
	@Autowired
	private JwtTokenHelper jwtTokenUtil;

	@Autowired
	private UserService userService;
	
	@GET
	@Path("/authorities")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAuthorityList() {
		AuthorityName[] list = userService.getAuthorityList();
		return Response.ok(list).build();
	}
	
	@GET
	@Path("/users/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@PathParam("username") String username) {
		User user = userService.getUser(username);
		if (user == null){
	        return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult("用户不存在!")).build();
	    }
		return Response.ok(user).build();
	}
	
	@GET
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserList(@BeanParam BaseQueryParams bps) {
		Page<User> list = userService.getUserList(bps);
		return Response.ok(list).build();
	}
	
	/**
	 * 检验启迪token，如果有效，则生产自己的token并返回
	 */
	@GET
	@Path("/token/qidi")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getQidiToken(@BeanParam QidiAuthReq authRequest) throws URISyntaxException {
		if (!qidiTokenUtil.validateToken(authRequest.getToken(), authRequest.getAppKey(), authRequest.getAppSecret())){
			return Response.temporaryRedirect(new java.net.URI(authRequest.getUri())).build();
		}
		
		User user = userService.getSuperUser();
		
		try{
			final String token = jwtTokenUtil.generateToken(user.getUsername());
			
			return Response.temporaryRedirect(new java.net.URI(authRequest.getUri()))
	         .cookie(new NewCookie("token", token, "/", null, 0, null, jwtTokenUtil.getTokenExpiration().intValue(), false))
	         .cookie(new NewCookie("role", (user.getAuthorities().get(0)!=null)?user.getAuthorities().get(0).getName().toString():"", "/", null, 0, null, jwtTokenUtil.getTokenExpiration().intValue(), false))
	         .cookie(new NewCookie("meta", user.getMeta(), "/", null, 0, null, jwtTokenUtil.getTokenExpiration().intValue(), false))
	         .cookie(new NewCookie("username", user.getUsername(), "/", null, 0, null, jwtTokenUtil.getTokenExpiration().intValue(), false))
	         .build();
		}catch (AuthenticationException e) {
			logger.warn(e.toString());
			return Response.temporaryRedirect(new java.net.URI(authRequest.getUri())).build();
		}
	}

	@POST
	@Path("/token")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getToken(JwtAuthReq authRequest) {
		String username = authRequest.getUsername();
		String password = authRequest.getPassword();
		try {
			// 验证登陆账户密码
			final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(username, password));
			
			SecurityContextHolder.getContext().setAuthentication(authentication);

			// Reload password post-security so we can generate token
			User user = userService.getUser(username);
	        if (user == null) {
	            throw new UsernameNotFoundException(String.format("用户 '%s' 不存在.", username));
	        }
			final String token = jwtTokenUtil.generateToken(username);

			// Return the token
			return Response.ok(new JwtAuthRsp(username, token, user.getMeta(), user.getAuthorities())).build();
		} catch (AuthenticationException e) {
			logger.warn(e.toString());
			return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
					.build();
		}
	}

	@POST
	@Path("/users")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response register(User user) {
		try{
			user = userService.addUser(user);
			return Response.created(null).entity(user).build();
		}catch(Exception e){
			logger.warn(e.toString());
			return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
					.build();
		}
	}

	@DELETE
    @Path("/users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteHospital(@PathParam("id") String id) {
        try {
            userService.deleteUser(id);
            return Response.noContent().build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }
	
	@PATCH
	@Path("/users/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addAuthority(@PathParam("id") String id, User user) throws AuthenticationException {
		try {
			userService.modifyUser(id, user);
			return Response.noContent().build();
		} catch (ServiceException e) {
			logger.warn(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
					.build();
		}
	}
}
