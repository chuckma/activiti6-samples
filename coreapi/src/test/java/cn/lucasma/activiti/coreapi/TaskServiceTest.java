package cn.lucasma.activiti.coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.*;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author Lucas Ma
 */
public class TaskServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceTest.class);


    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskService() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("message", "my test message !!!");
        activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process", variables);
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        logger.info("task = {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        logger.info("task.description = {}", task.getDescription());

        taskService.setVariable(task.getId(), "key1", "value1");
        taskService.setVariableLocal(task.getId(), "localKey1", "localValue1");

        Map<String, Object> taskServiceVariables = taskService.getVariables(task.getId());
        Map<String, Object> taskServiceVariablesLocal = taskService.getVariablesLocal(task.getId());

        Map<String, Object> processVariables = activitiRule.getRuntimeService().getVariables(task.getExecutionId());
        logger.info("taskServiceVariables = {}", taskServiceVariables);
        logger.info("taskServiceVariablesLocal = {}", taskServiceVariablesLocal);
        logger.info("processVariables = {}", processVariables);

        Map<String, Object> completeVar = Maps.newConcurrentMap();
        completeVar.put("cKey1", "cValue1");
        taskService.complete(task.getId(), completeVar);

        Task task1 = taskService.createTaskQuery().taskId(task.getId()).singleResult();
        logger.info("task1 = {}", task1);

    }


    /**
     *  任务处理人测试
     */
    @Test
    @Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskServiceUser() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("message", "my test message !!!");
        activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process", variables);
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        logger.info("task = {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        logger.info("task.description = {}", task.getDescription());

        taskService.setOwner(task.getId(), "user1");
//        taskService.setAssignee(task.getId(),"lucas");

        List<Task> taskList = taskService
                .createTaskQuery()
                .taskCandidateUser("lucas")
                .taskUnassigned().listPage(0, 100);

        for (Task task1 : taskList) {
            try {
                taskService.claim(task1.getId(), "lucas");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(task.getId());
        for (IdentityLink identityLink : identityLinksForTask) {
            logger.info("identityLink = {}", identityLink);
        }

        List<Task> lucass = taskService
                .createTaskQuery()
                .taskAssignee("lucas")
                .listPage(0, 100);

        for (Task lucas : lucass) {
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("ckey1", "cvalue1");
            taskService.complete(lucas.getId(), vars);
        }

        lucass = taskService.createTaskQuery().taskAssignee("lucas").listPage(0, 100);
        logger.info("是否存在 {}", CollectionUtils.isEmpty(lucass));
    }

    /**
     * 附件测试
     */
    @Test
    @Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskAttachment() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("message", "my test message !!!");
        activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process", variables);
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        taskService.createAttachment("url", task.getId(),
                task.getProcessInstanceId(), "name",
                "desc", "/url/test.png");

        List<Attachment> taskAttachments = taskService.getTaskAttachments(task.getId());
        for (Attachment taskAttachment : taskAttachments) {
            logger.info("taskAttachment = {}",ToStringBuilder.reflectionToString(taskAttachment,ToStringStyle.JSON_STYLE));
        }

    }


    /**
     *  任务评论测试
     */
    @Test
    @Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskComment() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("message", "my test message !!!");
        activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process", variables);
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        taskService.setOwner(task.getId(),"user1");
        taskService.setAssignee(task.getId(),"lucas");
        taskService.addComment(task.getId(),task.getProcessInstanceId(),"record note 1");
        taskService.addComment(task.getId(),task.getProcessInstanceId(),"record note 2");
        List<Comment> taskComments = taskService.getTaskComments(task.getId());

        for (Comment taskComment : taskComments) {

            logger.info("taskComment = {}",ToStringBuilder.reflectionToString(taskComment,ToStringStyle.JSON_STYLE));
        }

        List<Event> taskEvents = taskService.getTaskEvents(task.getId());
        for (Event taskEvent : taskEvents) {
            logger.info("taskEvent = {}",ToStringBuilder.reflectionToString(taskEvent,ToStringStyle.JSON_STYLE));
        }

    }
}
