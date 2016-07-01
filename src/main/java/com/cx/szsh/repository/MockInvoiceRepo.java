package com.cx.szsh.repository;

import com.cx.szsh.models.MockInvoice;

public interface MockInvoiceRepo extends BaseRepo<MockInvoice, String> {
	public MockInvoice findByCodeAndNumber(String code, String number);
	public MockInvoice findByTypeAndCodeAndNumberAndMoney(String type, String code, String number, String money);
}
