package com.cx.szsh.repository;

import com.cx.szsh.models.MockTrafficPoint;

public interface MockTrafficPointRepo extends BaseRepo<MockTrafficPoint, String> {
	public MockTrafficPoint findByNameAndIdCardAndLicenseAndFn(String name, String idCard, String license, String fn);
}
