package org.example.config;

import com.mybatisflex.core.tenant.TenantFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DefaultTenantFactory implements TenantFactory {

    private final static List<String> TENANT_IGNORES;

    private final static int NO_TENANT_FLAG = -1;

    static {
        TENANT_IGNORES = new ArrayList<>();
        TENANT_IGNORES.add("system_users");
    }


    @Override
    public Object[] getTenantIds() {
        List<Integer> tenantIds = new ArrayList<>();
        tenantIds.add(NO_TENANT_FLAG);
        // Add tenant conditions
        tenantIds.add(1);

        return tenantIds.toArray();
    }

    @Override
    public Object[] getTenantIds(String tableName) {
        if (TENANT_IGNORES.contains(tableName)) {
            return new Object[]{NO_TENANT_FLAG};
        }

        return this.getTenantIds();
    }
}
