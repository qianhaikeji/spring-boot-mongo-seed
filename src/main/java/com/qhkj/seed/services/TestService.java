package com.qhkj.seed.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.qhkj.seed.models.Test;
import com.qhkj.seed.repository.TestRepo;

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
