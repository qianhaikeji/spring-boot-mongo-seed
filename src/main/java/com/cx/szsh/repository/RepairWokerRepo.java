package com.cx.szsh.repository;

import java.util.List;

import com.cx.szsh.models.RepairWorker;

public interface RepairWokerRepo extends BaseRepo<RepairWorker, String> {
    public List<RepairWorker> findByType(String type);
}
