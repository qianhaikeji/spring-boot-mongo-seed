package com.cx.szsh.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cx.szsh.models.Test;
import com.cx.szsh.repository.TestRepo;

@Service
public class TestService extends BaseService {
	@Autowired
    private TestRepo repository;

    @Cacheable(value = "testCache")
    public Test find(String id) {
        System.out.println("无缓存的时候调用这里");
        return new Test(id);
    }
}
