package com.qhkj.seed;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * 若打包成war包,则需要继承 org.springframework.boot.context.web.SpringBootServletInitializer类,
 * 覆盖其config(SpringApplicationBuilder)方法
 */
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

}
