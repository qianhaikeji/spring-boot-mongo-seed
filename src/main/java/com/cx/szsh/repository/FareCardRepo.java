package com.cx.szsh.repository;

import com.cx.szsh.models.FareCard;

public interface FareCardRepo extends BaseRepo<FareCard, String> {
    public FareCard findByPhone(String phone);
}
