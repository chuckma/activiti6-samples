package cn.lucasma.activiti.config;

import com.google.common.collect.Maps;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.logging.LogMDC;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @Author Lucas Ma
 * <p>
 * History Level 测试
 * History 有 4 个级别，
 */
public class ConfigHistoryLevelTest {
    private static final Logger logger = LoggerFactory.getLogger(ConfigHistoryLevelTest.class);
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_history.cfg.xml");

    @Test
    @Deployment(resources = {"cn/lucasma/activiti/my-process.bpmn20.xml"})
    public void test() {
        // 打开 MDC


        // 启动流程
        startProcessInstance();

        // 修改变量
        changeVaiable();

        // 提交表单 task

        submitTaskFormData();

        // 输出历史内容
        // 输出历史活动
        showHistoryAcvitity();

        // 输出历史变量
        showHistoryVariable();

        // 输出历史用户任务
        showHistoryTask();

        // 输出历史表单
        showHistoryForm();

        // 输出历史详情

        showHistoryDetail();
    }

    private void showHistoryDetail() {
        List<HistoricDetail> historicDetails = activitiRule.getHistoryService()
                .createHistoricDetailQuery()
                .listPage(0, 100);
        for (HistoricDetail historicDetail : historicDetails) {

            logger.info("historicDetail {}", toString(historicDetail));
        }
        logger.info("historicDetails size ={}", historicDetails.size());
    }

    private void showHistoryForm() {
        List<HistoricDetail> historicDetailsForm = activitiRule.getHistoryService()
                .createHistoricDetailQuery()
                .formProperties()
                .listPage(0, 100);

        for (HistoricDetail historicDetail : historicDetailsForm) {
            logger.info("historicDetail {}", toString(historicDetail));
        }
        logger.info("historicDetailsForm size ={}", historicDetailsForm.size());
    }

    private void showHistoryTask() {
        List<HistoricTaskInstance> historicTaskInstances = activitiRule.getHistoryService()
                .createHistoricTaskInstanceQuery()
                .listPage(0, 100);

        for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
            logger.info("historicTaskInstance {}", historicTaskInstance);
        }
        logger.info("historicTaskInstances size ={}", historicTaskInstances.size());
    }

    private void showHistoryVariable() {
        List<HistoricVariableInstance> historicVariableInstances = activitiRule.getHistoryService()
                .createHistoricVariableInstanceQuery()
                .listPage(0, 100);

        for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
            logger.info("historicVariableInstance {}", historicVariableInstance);
        }
        logger.info("historicVariableInstances size = {}", historicVariableInstances.size());
    }

    private void showHistoryAcvitity() {
        List<HistoricActivityInstance> historicActivityInstances = activitiRule.getHistoryService()
                .createHistoricActivityInstanceQuery()
                .listPage(0, 100);
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            logger.info("historicActivityInstance {}", historicActivityInstance);
        }
        logger.info("historicActivityInstances size ={}", historicActivityInstances.size());
    }

    private void submitTaskFormData() {
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        Map<String, String> properties = Maps.newHashMap();
        properties.put("formKey1", "valueF1");
        properties.put("formKey2", "valueF2");
        activitiRule.getFormService().submitTaskFormData(task.getId(), properties);
    }

    private void changeVaiable() {
        List<Execution> executions = activitiRule.getRuntimeService().createExecutionQuery().listPage(0, 100);
        for (Execution execution : executions) {
            logger.info("execution :{}", execution);
        }
        logger.info("execution size :{}", executions.size());


        String id = executions.iterator().next().getId();
        activitiRule.getRuntimeService().setVariable(id, "kyeStart1", "value1_new");
    }

    private void startProcessInstance() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("kyeStart1", "value1");
        params.put("kyeStart2", "value2");
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        assertNotNull(processInstance);
    }

    static String toString(HistoricDetail historicDetail) {
        return ToStringBuilder.reflectionToString(historicDetail, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
