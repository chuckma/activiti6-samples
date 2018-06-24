package cn.lucasma.activiti.config;

import org.activiti.engine.event.EventLogEntry;
import org.activiti.engine.logging.LogMDC;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @Author Lucas Ma
 * <p>
 * EventLog 测试
 */
public class ConfigJobTest {
    private static final Logger logger = LoggerFactory.getLogger(ConfigJobTest.class);
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_job.cfg.xml");

    @Test
    @Deployment(resources = {"cn/lucasma/activiti/my-process_job.bpmn20.xml"})
    public void test() throws InterruptedException {
        logger.info("start");
        List<Job> jobs = activitiRule
                .getManagementService()
                .createTimerJobQuery()
                .listPage(0, 100);


        for (Job job : jobs) {
            logger.info("定时任务 = {}，默认重试次数 = {}",job,job.getRetries());
        }
        logger.info("jobs size = {}",jobs.size());
        Thread.sleep(1000 * 100);
        logger.info("end");
    }

}
