package cn.lucasma.activiti.bpmn20;

import com.google.common.collect.Maps;
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
public class SubProcessTest {

    private static final Logger logger = LoggerFactory.getLogger(SubProcessTest.class);


    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process-subprocess1.bpmn20.xml"})
    public void testSubProcess() {
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process");
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        logger.info("task.name = {}", task.getName());

    }

    @Test
    @Deployment(resources = {"my-process-subprocess1.bpmn20.xml"})
    public void testSubProcess2() {
        Map<String, Object> varibles = Maps.newHashMap();
        varibles.put("errorflag", true);
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process", varibles);
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        logger.info("task.name = {}", task.getName());
        Map<String, Object> variables = activitiRule.getRuntimeService().getVariables(processInstance.getId());
        logger.info("variables = {}", variables);

    }

    @Test
    @Deployment(resources = {"my-process-subprocess2.bpmn20.xml"})
    public void testSubProcess3() {
        Map<String, Object> varibles = Maps.newHashMap();
        varibles.put("errorflag", true);
        varibles.put("key1", "value1");
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process", varibles);
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        logger.info("task.name = {}", task.getName());
        Map<String, Object> variables = activitiRule.getRuntimeService().getVariables(processInstance.getId());
        logger.info("variables = {}", variables);

    }

    @Test
    @Deployment(resources = {"my-process-subprocess3.bpmn20.xml"
            , "my-process-subprocess4.bpmn20.xml"})
    public void testSubProcess4() {
        Map<String, Object> varibles = Maps.newHashMap();
        varibles.put("errorflag", true);
        varibles.put("key0", "value0");
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process", varibles);
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        logger.info("task.name = {}", task.getName());
        Map<String, Object> variables = activitiRule.getRuntimeService().getVariables(processInstance.getId());
        logger.info("variables = {}", variables);

    }

}
