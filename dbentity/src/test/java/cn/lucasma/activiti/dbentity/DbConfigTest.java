package cn.lucasma.activiti.dbentity;

import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * @Author Lucas Ma
 */
public class DbConfigTest {
    private static final Logger logger = LoggerFactory.getLogger(DbConfigTest.class);

    @Test
    public void testDbConfig() {

        ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-mysql.cfg.xml")
                .buildProcessEngine();
        ManagementService managementService = processEngine.getManagementService();
        Map<String, Long> tableCount = managementService.getTableCount();
        // 表名
        ArrayList<String> tableNames = Lists.newArrayList(tableCount.keySet());

        Collections.sort(tableNames);
        for (String tableName : tableNames) {
            logger.info("table = {}", tableName);
        }

        logger.info("tableNames.size = {}", tableNames.size());
    }

    @Test
    public void testDropTable() {
        ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-mysql.cfg.xml")
                .buildProcessEngine();
        ManagementService managementService = processEngine.getManagementService();
        Object o = managementService.executeCommand(new Command<Object>() {
            @Override
            public Object execute(CommandContext commandContext) {
                commandContext.getDbSqlSession().dbSchemaDrop();
                logger.info("删除表结构");
                return null;
            }
        });
    }
}
