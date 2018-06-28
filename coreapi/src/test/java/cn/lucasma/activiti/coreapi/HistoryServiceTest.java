package cn.lucasma.activiti.coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Lucas Ma
 */
public class HistoryServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(HistoryServiceTest.class);
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testHistoryService() {

        HistoryService historyService = activitiRule.getHistoryService();
        // 流程实例
        ProcessInstanceBuilder processInstanceBuilder = activitiRule.getRuntimeService().createProcessInstanceBuilder();

        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key0", "value0");
        variables.put("key1", "value1");
        variables.put("key2", "value2");


        Map<String, Object> transientVariables = Maps.newHashMap();
        transientVariables.put("tkey1", "tvalue1");

        ProcessInstance processInstance = processInstanceBuilder
                .processDefinitionKey("my-process")
                .variables(variables)// 持久变量
                .transientVariables(transientVariables).start();// 瞬时变量 不会存到历史库里

        // 修改数据，看是否记录在History里
        activitiRule.getRuntimeService()
                .setVariable(processInstance.getId(), "key1", "value1_1");


        Task task = activitiRule.getTaskService()
                .createTaskQuery()
                .processInstanceId(processInstance.getId()).singleResult();
//        activitiRule.getTaskService().complete(task.getId(),variables);
        Map<String, String> properties = Maps.newHashMap();
        properties.put("fKey1", "fValue1");
        properties.put("key2", "value_2_2");

        // 通过表单提交数据
        activitiRule.getFormService().submitTaskFormData(task.getId(), properties);




        // 流程实例
        List<HistoricProcessInstance> historicProcessInstances = historyService
                .createHistoricProcessInstanceQuery()
                .listPage(0, 100);
        
        for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
            logger.info("historicProcessInstance = {}", ToStringBuilder
                    .reflectionToString(historicProcessInstance, ToStringStyle.JSON_STYLE));
        }

        // 流程节点
        List<HistoricActivityInstance> historicActivityInstances = historyService
                .createHistoricActivityInstanceQuery()
                .listPage(0, 100);

        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            logger.info("historicActivityInstance = {}",historicActivityInstance);
        }

        // 流程任务
        List<HistoricTaskInstance> historicTaskInstances = historyService
                .createHistoricTaskInstanceQuery()
                .listPage(0, 100);

        for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
            logger.info("historicTaskInstance = {}",ToStringBuilder
                    .reflectionToString(historicTaskInstance,ToStringStyle.JSON_STYLE));
        }

        // 流程变量
        List<HistoricVariableInstance> historicVariableInstances = historyService
                .createHistoricVariableInstanceQuery()
                .listPage(0, 100);
        for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
            logger.info("historicVariableInstance = {}",historicVariableInstance);
        }

        // 流程详情
        List<HistoricDetail> historicDetails = historyService
                .createHistoricDetailQuery()
                .listPage(0, 100);
        for (HistoricDetail historicDetail : historicDetails) {
            logger.info("historicDetail = {} ",historicDetail);
        }

        // 流程历史日志信息
        ProcessInstanceHistoryLog processInstanceHistoryLog = historyService
                .createProcessInstanceHistoryLogQuery(processInstance.getId())
                .includeVariables()
                .includeFormProperties()
                .includeComments()
                .includeTasks()
                .includeActivities()
                .includeVariableUpdates().singleResult();

        List<HistoricData> historicDataList = processInstanceHistoryLog.getHistoricData();
        for (HistoricData historicData : historicDataList) {
            logger.info("historicData = {}",historicData);
        }

        // 删除流程实例
        historyService.deleteHistoricProcessInstance(processInstance.getId());

        // 查询一次流程实例
        HistoricProcessInstance historicProcessInstance = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstance.getId()).singleResult();

        logger.info("historicProcessInstance = {}",historicProcessInstance);

    }
}
