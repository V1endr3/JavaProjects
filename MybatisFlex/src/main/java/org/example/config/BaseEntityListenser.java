package org.example.config;

import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.UpdateListener;
import org.example.entity.BaseEntity;

import java.time.LocalDateTime;

public class BaseEntityListenser implements InsertListener, UpdateListener {
    @Override
    public void onInsert(Object entity) {
        if (entity instanceof BaseEntity baseEntity) {
            baseEntity.setCreateTime(LocalDateTime.now());
            baseEntity.setUpdateTime(LocalDateTime.now());
        }
    }

    @Override
    public void onUpdate(Object entity) {
        if (entity instanceof BaseEntity baseEntity) {
            baseEntity.setUpdateTime(LocalDateTime.now());
        }
    }
}
