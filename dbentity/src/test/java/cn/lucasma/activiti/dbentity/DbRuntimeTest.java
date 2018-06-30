package cn.lucasma.activiti.dbentity;

import com.google.common.collect.Maps;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Author Lucas Ma
 */
public class DbRuntimeTest {
    private static final Logger logger = LoggerFactory.getLogger(DbRuntimeTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql.cfg.xml");

    @Test
    public void testRuntime() {
        activitiRule.getRepositoryService().createDeployment()
                .name("二次审批流程")
                .addClasspathResource("second_approve.bpmn20.xml")
                .deploy();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");
        ProcessInstance second_approve =
                activitiRule.getRuntimeService()
                        .startProcessInstanceByKey("second_approve", variables);
    }

    @Test
    public void testSetOwner() {
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionKey("second_approve").singleResult();
        taskService.setOwner(task.getId(), "user1");

    }

    @Test
    public void testMessage() {
        activitiRule.getRepositoryService().createDeployment()
                .addClasspathResource("my-process-message.bpmn20.xml")
                .deploy();
    }

    @Test
    public void testMessageReceived() {
        activitiRule.getRepositoryService().createDeployment()
                .addClasspathResource("my-process-message-received.bpmn20.xml")
                .deploy();

        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");


    }
    @Test
    public void testJob() throws InterruptedException {
        activitiRule.getRepositoryService().createDeployment()
                .addClasspathResource("my-process-job.bpmn20.xml")
                .deploy();

        Thread.sleep(1000*30L);


    }


}
