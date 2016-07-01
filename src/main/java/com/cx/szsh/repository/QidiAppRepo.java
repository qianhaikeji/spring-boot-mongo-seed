package com.cx.szsh.repository;

import com.cx.szsh.models.QidiApp;

public interface QidiAppRepo extends BaseRepo<QidiApp, String> {
	public QidiApp findByName(String name);
}
