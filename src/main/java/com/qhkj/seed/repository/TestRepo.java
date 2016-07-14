package com.qhkj.seed.repository;

import com.qhkj.seed.models.Test;

public interface TestRepo extends BaseRepo<Test, String > {
    public Test findByFirstName(String firstName);
    public Test findByLastName(String lastName);
}