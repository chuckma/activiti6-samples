package cn.lucasma.activiti.bpmn20;

import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Author Lucas Ma
 */
public class UserTaskTest {

    private static final Logger logger = LoggerFactory.getLogger(UserTaskTest.class);


    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process-usertask.bpmn20.xml"})
    public void testUserTask() {

        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process");
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().taskCandidateUser("user1").singleResult();
        logger.info("find by user1 task = {}", task);

        task = taskService.createTaskQuery().taskCandidateUser("user2").singleResult();
        logger.info("find by user2 task = {}", task);

        task = taskService.createTaskQuery().taskCandidateGroup("group1").singleResult();
        logger.info("find by group1 task = {}", task);


        taskService.claim(task.getId(), "user2");
        logger.info("claim task.id = {} by user2", task.getId());

        task = taskService.createTaskQuery().taskCandidateOrAssigned("user1").singleResult();
        logger.info("find by user1 task ={}", task);
        task = taskService.createTaskQuery().taskCandidateOrAssigned("user2").singleResult();
        logger.info("find by user2 task ={}", task);
    }
    @Test
    @Deployment(resources = {"my-process-usertask2.bpmn20.xml"})
    public void testUserTask2() {
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().taskCandidateUser("user1").singleResult();
        logger.info("find by user1 task ={}", task);
        taskService.complete(task.getId());
    }
}
