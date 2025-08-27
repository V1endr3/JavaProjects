package org.example.model;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.mybatisflex.core.mask.Masks;
import lombok.Data;
import org.example.constant.GenderEnum;

import java.time.LocalDateTime;

@Data
@Table(value = "t_user_info")
public class User {

    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long userId;

    private String account;

    private String credential;

    private String userName;

    private GenderEnum gender;

    private Integer age;

    @ColumnMask(Masks.MOBILE)
    private String mobile;

    @ColumnMask(Masks.EMAIL)
    private String email;

    private Long createUser;

    @Column(onInsertValue = "now()")
    private LocalDateTime createTime;

    private Long updateUser;

    @Column(onInsertValue = "now()", onUpdateValue = "now()")
    private LocalDateTime updateTime;

    @Column(isLogicDelete = true)
    private Long deleteFlag;

    @Column(tenantId = true)
    private Long tenantId;

}
