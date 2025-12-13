package org.example.config;

import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.core.keygen.KeyGeneratorFactory;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import org.example.entity.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class FlexConfiguration implements MyBatisFlexCustomizer {
    private static final Logger logger = LoggerFactory
            .getLogger("Flex-SQL");

    @Autowired
    private UUIDKeyGenerator uuidKeyGenerator;

    @Override
    public void customize(FlexGlobalConfig defaultConfig) {
        defaultConfig.setPrintBanner(false);

        // SQL audit
        AuditManager.setAuditEnable(true);
        AuditManager.setMessageCollector(auditMessage ->
                logger.info("{}, Time consumption: {}ms", auditMessage.getFullSql()
                        , auditMessage.getElapsedTime())
        );

        // logic delete
        defaultConfig.setLogicDeleteColumn("deleted");
        defaultConfig.setNormalValueOfLogicDelete(0);
        defaultConfig.setDeletedValueOfLogicDelete(LocalDateTime.now().getNano());

        // tenant column
        defaultConfig.setTenantColumn("tenant_id");

        // base infomation listener
        BaseEntityListenser baseEntityListenser = new BaseEntityListenser();
        defaultConfig.registerInsertListener(baseEntityListenser, BaseEntity.class);
        defaultConfig.registerUpdateListener(baseEntityListenser, BaseEntity.class);

        // key generators
        KeyGeneratorFactory.register("uuid_gen", uuidKeyGenerator);

        FlexGlobalConfig.KeyConfig keyConfig = new FlexGlobalConfig.KeyConfig();
        keyConfig.setKeyType(KeyType.Generator);
        keyConfig.setValue("uuid_gen");
        keyConfig.setBefore(true);
        defaultConfig.setKeyConfig(keyConfig);
    }
}
