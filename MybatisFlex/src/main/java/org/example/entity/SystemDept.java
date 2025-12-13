package org.example.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "system_dept")
public class SystemDept extends TenantEntity {

    private String deptname;

    private String remark;

    private Integer status;

    private Integer deptType;

    private Integer roleType;

}
