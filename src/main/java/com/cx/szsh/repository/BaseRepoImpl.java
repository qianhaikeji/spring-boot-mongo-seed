package com.cx.szsh.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;

/**
 * 修改默认的mongo实现
 */
public class BaseRepoImpl<T, ID extends Serializable> extends SimpleMongoRepository<T, ID> implements BaseRepo<T, ID> {
	protected final MongoOperations mongoTemplate;

	protected final MongoEntityInformation<T, ID> entityInformation;

	public BaseRepoImpl(MongoEntityInformation<T, ID> metadata, MongoOperations mongoOperations) {
		super(metadata, mongoOperations);

		this.mongoTemplate = mongoOperations;
		this.entityInformation = metadata;
	}

	protected Class<T> getEntityClass() {
		return entityInformation.getJavaType();
	}
	
	protected String getCollectionName() {
		return entityInformation.getCollectionName();
	}

	@Override
	public Page<T> find(Query query, Pageable pageable) {
		long total = mongoTemplate.count(query, getEntityClass());
		List<T> list = mongoTemplate.find(query.with(pageable), getEntityClass());

		return new PageImpl<T>(list, pageable, total);
	}
	
	@Override
	public Page<T> find(List<Criteria> cs, Pageable pageable) {
		Query query = new Query();
		for (Criteria c : cs) {
			query.addCriteria(c);
        }
		return find(query, pageable);
	}
	
	@Override
	public Page<T> find(List<Criteria> cs, String[] excludes, Pageable pageable){
		Query query = new Query();
		for (Criteria c : cs) {
			query.addCriteria(c);
        }
		
		for (String key : excludes) {
			query.fields().exclude(key);
		}
		
		return find(query, pageable);
	}
	
	@Override
	public Page<T> find(Criteria criteria, Pageable pageable) {
		return find(new Query(criteria), pageable);
	}

	@Override
	public void upsert(List<Criteria> cs, Update update){
		Query query = new Query();
		for (Criteria c : cs) {
			query.addCriteria(c);
	    }
		
		mongoTemplate.upsert(query, update, getEntityClass());
	}
	
	@Override
	public boolean exists(List<Criteria> cs){
		Query query = new Query();
		for (Criteria c : cs) {
			query.addCriteria(c);
	    }
		
		return mongoTemplate.exists(query, getEntityClass(), getCollectionName());
	}
}
