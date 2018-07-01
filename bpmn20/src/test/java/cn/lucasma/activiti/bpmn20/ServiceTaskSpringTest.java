package cn.lucasma.activiti.bpmn20;

import cn.lucasma.activiti.example.MyJavaBean;
import cn.lucasma.activiti.example.MyJavaDelegate;
import com.google.common.collect.Maps;
import org.activiti.engine.ActivitiEngineAgenda;
import org.activiti.engine.ManagementService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author Lucas Ma
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:activiti-context.xml")
public class ServiceTaskSpringTest {

    private static final Logger logger = LoggerFactory.getLogger(ServiceTaskSpringTest.class);


    @Resource
    @Rule
    public ActivitiRule activitiRule;


    /**
     *  JavaDelegate 对象表达式
     *  activiti:delegateExpression 每次都是在 spring 里创建单例对象， 这种情况下，不适合注入属性，此时线程不安全
     *  activiti:class 每次都是创建新的对象，不过注入属性是安全的，弊端是频繁的创建对象影响性能
     */
    @Test
    @Deployment(resources = {"my-process-servicetask4.bpmn20.xml"})
    public void testServiceTask() {
//        myJavaDelegate
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        List<HistoricActivityInstance> activityInstances = activitiRule.getHistoryService().createHistoricActivityInstanceQuery().orderByHistoricActivityInstanceEndTime().asc().list();
        for (HistoricActivityInstance activityInstance : activityInstances) {
            logger.info("activity = {}", activityInstance);
        }
    }

    @Test
    @Deployment(resources = {"my-process-servicetask4.bpmn20.xml"})
    public void testServiceTask2() {
//        myJavaDelegate
        Map<String, Object> variables = Maps.newHashMap();
        MyJavaDelegate myJavaDelegate = new MyJavaDelegate();
        logger.info("myJavaDelegate = {}", myJavaDelegate);
        variables.put("myJavaDelegate", myJavaDelegate);
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process", variables);
        List<HistoricActivityInstance> activityInstances = activitiRule.getHistoryService().createHistoricActivityInstanceQuery().orderByHistoricActivityInstanceEndTime().asc().list();
        for (HistoricActivityInstance activityInstance : activityInstances) {
            logger.info("activity = {}", activityInstance);
        }
    }

    @Test
    @Deployment(resources = {"my-process-servicetask5.bpmn20.xml"})
    public void testServiceTask5() {
//        myJavaDelegate
        Map<String, Object> variables = Maps.newHashMap();
        MyJavaBean myJavaBean = new MyJavaBean("TEST");
        logger.info("myJavaBean = {}", myJavaBean);
        variables.put("myJavaBean", myJavaBean);
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process", variables);
        List<HistoricActivityInstance> activityInstances = activitiRule.getHistoryService().createHistoricActivityInstanceQuery().orderByHistoricActivityInstanceEndTime().asc().list();
        for (HistoricActivityInstance activityInstance : activityInstances) {
            logger.info("activity = {}", activityInstance);
        }
    }
}
