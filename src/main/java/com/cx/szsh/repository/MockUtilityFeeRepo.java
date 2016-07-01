package com.cx.szsh.repository;

import com.cx.szsh.models.MockUtilityFee;

public interface MockUtilityFeeRepo extends BaseRepo<MockUtilityFee, String> {
	public MockUtilityFee findByAccountAndType(String account, String type);
}
