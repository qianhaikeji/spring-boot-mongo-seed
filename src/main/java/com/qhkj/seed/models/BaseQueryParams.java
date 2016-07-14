package com.qhkj.seed.models;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import lombok.Getter;
import lombok.Setter;

/**
 * 列表查询的基本参数结构
 */
@Getter @Setter
public class BaseQueryParams {
	@QueryParam("fuzzy")
    private String fuzzy;
	@QueryParam("limit") @DefaultValue("10")
    private int limit;
	@QueryParam("offset") @DefaultValue("0")
    private int offset;
    
    public BaseQueryParams() {

    }

	public BaseQueryParams(String fuzzy, int limit, int offset) {
		super();
		this.fuzzy = fuzzy;
		this.limit = limit;
		this.offset = offset;
	}
}
