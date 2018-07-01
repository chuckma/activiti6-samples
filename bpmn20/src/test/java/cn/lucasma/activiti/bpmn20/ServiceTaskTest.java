package cn.lucasma.activiti.bpmn20;

import com.google.common.collect.Maps;
import org.activiti.engine.ActivitiEngineAgenda;
import org.activiti.engine.ManagementService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @Author Lucas Ma
 */
public class ServiceTaskTest {

    private static final Logger logger = LoggerFactory.getLogger(ServiceTaskTest.class);


    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    /**
     *  JavaDelegate 不等待
     */
    @Test
    @Deployment(resources = {"my-process-servicetask1.bpmn20.xml"})
    public void testServiceTask() {
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");

        List<HistoricActivityInstance> activityInstances = activitiRule.getHistoryService().createHistoricActivityInstanceQuery().orderByHistoricActivityInstanceEndTime().asc().list();

        for (HistoricActivityInstance activityInstance : activityInstances) {
            logger.info("activity = {}", activityInstance);
        }
    }

    /**
     * ActivityBehavior 等待其他驱动流程节点才能执行
     */
    @Test
    @Deployment(resources = {"my-process-servicetask2.bpmn20.xml"})
    public void testServiceTask2() {
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        List<HistoricActivityInstance> activityInstances = activitiRule.getHistoryService().createHistoricActivityInstanceQuery().orderByHistoricActivityInstanceEndTime().asc().list();
        for (HistoricActivityInstance activityInstance : activityInstances) {
            logger.info("activity = {}", activityInstance);
        }
        Execution execution = activitiRule.getRuntimeService().createExecutionQuery()
                .activityId("someTask")
                .singleResult();
        logger.info("execution = {}",execution);

        ManagementService managementService = activitiRule.getManagementService();
        managementService.executeCommand(new Command<Object>() {
            @Override
            public Object execute(CommandContext commandContext) {
                ActivitiEngineAgenda agenda = commandContext.getAgenda();
                agenda.planTakeOutgoingSequenceFlowsOperation((ExecutionEntity) execution,false);
                return null;
            }
        });

        activityInstances = activitiRule.getHistoryService()
                .createHistoricActivityInstanceQuery()
                .orderByHistoricActivityInstanceEndTime()
                .asc().list();
        for (HistoricActivityInstance activityInstance : activityInstances) {
            logger.info("activity = {}", activityInstance);
        }

    }

    /**
     *  JavaDelegate 注入属性
     */
    @Test
    @Deployment(resources = {"my-process-servicetask3.bpmn20.xml"})
    public void testServiceTask3() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("desc","the test java delegate");
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process",variables);

    }
}
