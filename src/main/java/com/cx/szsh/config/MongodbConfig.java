package com.cx.szsh.config;

import com.cx.szsh.repository.BaseRepoImpl;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@EnableMongoRepositories(repositoryBaseClass = BaseRepoImpl.class, basePackages = "com.cx.szsh")
//使能mongo model注解，如@createdDate等
@EnableMongoAuditing
public class MongodbConfig extends AbstractMongoConfiguration {
    @Value("${spring.mongodb.host}")
    private String host;

    @Value("${spring.mongodb.port}")
    private Integer port;

    @Value("${spring.mongodb.database}")
    private String database;

    @Value("${spring.mongodb.username}")
    private String username;

    @Value("${spring.mongodb.password}")
    private String password;
    
    @Override
    @Bean
    public Mongo mongo() throws Exception {
        ServerAddress sa = new ServerAddress(host, port);
        List<MongoCredential> mongoCredentialList = new ArrayList<MongoCredential>();
        // mongo 3.0 以上必须使用ScramSha1
        mongoCredentialList.add(MongoCredential.createScramSha1Credential(username, database, password.toCharArray()));
        return new MongoClient(sa, mongoCredentialList);
    }

    public MongoTemplate template() throws Exception {
        return new MongoTemplate(mongo(), database);
    }
	
	@Bean
	public ValidatingMongoEventListener validatingMongoEventListener() {
	    return new ValidatingMongoEventListener(validator());
	}
	
	@Bean
	public LocalValidatorFactoryBean validator() {
	    return new LocalValidatorFactoryBean();
	}
	
	@Override
	public String getDatabaseName() {
	    return database;
	}
}