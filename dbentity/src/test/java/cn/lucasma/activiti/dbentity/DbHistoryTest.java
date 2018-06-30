package cn.lucasma.activiti.dbentity;

import com.google.common.collect.Maps;
import org.activiti.engine.RuntimeService;
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
public class DbHistoryTest {
    private static final Logger logger = LoggerFactory.getLogger(DbHistoryTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql.cfg.xml");

    @Test
    public void testHistory(){
        activitiRule.getRepositoryService().createDeployment()
                .name("测试部署")
                .addClasspathResource("my-process.bpmn20.xml")
                .deploy();

        RuntimeService runtimeService = activitiRule.getRuntimeService();

        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key0","value0");
        variables.put("key1","value1");
        variables.put("key2","value2");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);

        runtimeService.setVariable(processInstance.getId(),"key1","value1_1");

        TaskService taskService = activitiRule.getTaskService();

        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();

        taskService.setOwner(task.getId(),"user1");

        taskService.createAttachment("url",
                task.getId(),processInstance.getId(),
                "name","desc",
                "/url/test.png");

        taskService.addComment(task.getId(),task.getProcessInstanceId(),"record note1");
        taskService.addComment(task.getId(),task.getProcessInstanceId(),"record note2");

        Map<String, String> properties = Maps.newHashMap();
        properties.put("key2","value2_1");
        properties.put("key3","value3_1");
        activitiRule.getFormService().submitTaskFormData(task.getId(),properties);

    }


}
