package org.example.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class BaseEntity {
    public static final String ID = "id";
    public static final String CREATE_TIME = "create_time";

    @Id
    private Long id;

    private Long creator;

    @Column(value = CREATE_TIME)
    private LocalDateTime createTime;

    private Long updater;

    private LocalDateTime updateTime;

    private Long deleted;

}
