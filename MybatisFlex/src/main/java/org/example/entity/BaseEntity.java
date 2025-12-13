package org.example.entity;

import com.mybatisflex.annotation.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class BaseEntity {

    @Id
    private Long id;

    private Long creator;

    private LocalDateTime createTime;

    private Long updater;

    private LocalDateTime updateTime;

    private Long deleted;

}
