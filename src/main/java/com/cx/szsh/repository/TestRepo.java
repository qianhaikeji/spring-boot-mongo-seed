package com.cx.szsh.repository;

import com.cx.szsh.models.Test;

public interface TestRepo extends BaseRepo<Test, String > {
    public Test findByFirstName(String firstName);
    public Test findByLastName(String lastName);
}