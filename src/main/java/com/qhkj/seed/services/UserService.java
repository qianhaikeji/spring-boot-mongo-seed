package com.qhkj.seed.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.qhkj.seed.auth.AuthUserFactory;
import com.qhkj.seed.auth.Authority;
import com.qhkj.seed.auth.Authority.AuthorityName;
import com.qhkj.seed.exceptions.ServiceException;
import com.qhkj.seed.models.BaseQueryParams;
import com.qhkj.seed.models.User;
import com.qhkj.seed.repository.UserRepo;

@Service
public class UserService extends BaseService implements UserDetailsService {
	@Autowired
	private UserRepo userRepository;
	
    @Autowired
    private PasswordEncoder passwordEncoder;
	
    private static final String SUPER_USER_NAME = "admin";
    private static final String SUPER_USER_PASSWORD = "admin123";
    private static final String SUPER_USER_EMAIL = "admin@qq.com";
    private static final String SUPER_USER_PHONE = "";

    // 初始化超级管理员帐户
    @PostConstruct
    protected void initialize() {
        getSuperUser();
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("用户 '%s' 不存在.", username));
        } else {
            return AuthUserFactory.create(user);
        }
    }
    
    public User getSuperUser(){
    	User user = userRepository.findByUsername(SUPER_USER_NAME);

        if ( user == null) {
            user = addUser(new User(SUPER_USER_NAME, SUPER_USER_EMAIL, SUPER_USER_PHONE, SUPER_USER_PASSWORD, null, Collections.singletonList(new Authority(AuthorityName.ROLE_ADMIN))));
        }
        
        return user;
    }
    
    public UserDetails getQidiUser() {
    	return AuthUserFactory.create(new User("qidiclient", "qidiclient", "qidiclient", "qidiclient", null, Collections.emptyList()));
    }
    
    public User getUser(String username) {
    	return userRepository.findByUsername(username);
    }
    
    public Page<User> getUserList(BaseQueryParams bps) {
        Pageable pageable = new PageRequest(bps.getOffset(), bps.getLimit());
        return userRepository.findAll(pageable);
    }
    
    public AuthorityName[] getAuthorityList() {
		return AuthorityName.values();
	}

	public User addUser(User data) throws ServiceException {
	    User user = new User(data.getUsername(), data.getEmail(), data.getPhone(), passwordEncoder.encode(data.getPassword()), data.getMeta(), data.getAuthorities());
	    try{
	    	return userRepository.save(user);
	    } catch (Exception e) {
	        throw new ServiceException(e.getMessage());
	    }
	}

	public void modifyUser(String id, User user) throws ServiceException {
    	User exist = userRepository.findOne(id);
        if (exist == null) {
            throw new ServiceException("用户 [" + id + "] 不存在!");
        }

        if (user.getAuthorities() != null && user.getAuthorities().size() > 0) {
            exist.setAuthorities(user.getAuthorities());
        }
        
        exist.setPhone(user.getPhone());
        exist.setEmail(user.getEmail());
        exist.setMeta(user.getMeta());

        try {
        	userRepository.save(exist);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void addUserAuthority(String uid, String authority){
    	List<Criteria> cs = new ArrayList<Criteria>();
		cs.add(new Criteria("id").is(new ObjectId(uid)));		
		Update update = new Update().push("authorities", new BasicDBObject("name", authority));
    	userRepository.upsert(cs, update);
    }

    public void deleteUser(String id) throws ServiceException {
        User exist = userRepository.findOne(id);
        if (exist == null){
            throw new ServiceException("用户 [" + id + "] 不存在!");
        }

        if (SUPER_USER_NAME.equals(exist.getUsername())){
            throw new ServiceException("超级用户不可删除!");
        }

        try {
            userRepository.delete(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void deleteUserAuthority(String uid, String authority){
    	List<Criteria> cs = new ArrayList<Criteria>();
    	cs.add(new Criteria("id").is(new ObjectId(uid)));
		Update update = new Update().pull("authorities", new BasicDBObject("name", authority));
    	userRepository.upsert(cs, update);
    }

    // public boolean changePassword(User user, String password, String newPassword){
    //     if (password == null || newPassword == null || password.isEmpty() || newPassword.isEmpty())
    //         return false;

    //     logger.info("" + passwordEncoder.matches(password, user.getPassword()));
    //     if (!user.getPassword().equals(passwordEncoder.encode(password)))
    //         return false;

    //     user.setPassword(passwordEncoder.encode(newPassword));

    //     logger.info("User @"+user.getEmail() + " changed password.");

    //     return true;
    // }
}
