package cn.lucasma.activiti.coreapi;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Author Lucas Ma
 * <p>
 * Test
 */
public class RepositoryServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(RepositoryServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    public void testRepostory() {
        /**
         * 负责对流程静态文件的管理, 如流程 xml 流程图片
         * 部署对象， 资源对象
         * 部署对象和资源对象是 一对多的关系
         */
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        // 流程部署对象
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.name("测试部署资源1")
                .addClasspathResource("my-process.bpmn20.xml")
                .addClasspathResource("second_approve.bpmn20.xml");
        Deployment deploy = deploymentBuilder.deploy();
        logger.info("deploy ={}", deploy);

        DeploymentBuilder deployment1 = repositoryService.createDeployment();
        deployment1.name("测试部署资源2")
                .addClasspathResource("my-process.bpmn20.xml")
                .addClasspathResource("second_approve.bpmn20.xml");
        Deployment deploy1 = deployment1.deploy();

        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        List<Deployment> deploymentList = deploymentQuery
                .orderByDeploymenTime().asc().listPage(0, 100);

        for (Deployment deployment : deploymentList) {
            logger.info("deployment = {}",deployment);
        }
        logger.info("deploymentList size = {}", deploymentList.size());
        List<ProcessDefinition> definitionList = repositoryService
                .createProcessDefinitionQuery()
//                .deploymentId(deploy.getId())
                .orderByProcessDefinitionKey().asc()
                .listPage(0, 100);
        for (ProcessDefinition processDefinition : definitionList) {
            logger.info("processDefinition = {},version = {},key ={},id = {}",
                    processDefinition,
                    processDefinition.getVersion(),
                    processDefinition.getKey(),
                    processDefinition.getId());
        }
    }

}
