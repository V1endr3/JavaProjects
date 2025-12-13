package org.example.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "system_users")
public class SystemUsers extends TenantEntity {

    private String username;

    private String password;

    private String nickname;

    private String remark;

    private Long deptId;

    private String postIds;

    private String email;

    private String mobile;

    private Integer sex;

    private String avatar;

    private Integer status;

    private String loginIp;

    private LocalDateTime loginDate;

    private Integer userType;

    private Integer adminType;

}
