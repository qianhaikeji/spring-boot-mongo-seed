package com.qhkj.seed.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 修改默认的mongo实现
 */
@NoRepositoryBean
public interface BaseRepo <T, ID extends Serializable> extends MongoRepository<T, ID>{
    public Page<T> find(Query query, Pageable pageable);
    public Page<T> find(Criteria criteria, Pageable pageable);
    public Page<T> find(List<Criteria> cs, Pageable pageable);
    public Page<T> find(List<Criteria> cs, String[] excludes, Pageable pageable);
    public void upsert(List<Criteria> cs, Update update);
    public boolean exists(List<Criteria> cs);
}
