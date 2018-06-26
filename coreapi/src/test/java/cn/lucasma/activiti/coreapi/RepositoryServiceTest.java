package cn.lucasma.activiti.coreapi;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLink;
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



    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testSuspend(){
        RepositoryService repositoryService = activitiRule.getRepositoryService();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();

        logger.info("processDefinition.id = {}",processDefinition.getId());

        repositoryService.suspendProcessDefinitionById(processDefinition.getId());
        try {
            logger.info("开始启动");
            activitiRule.getRuntimeService().startProcessInstanceById(processDefinition.getId());
            logger.info("启动成功");
        } catch (Exception e) {
            logger.info("启动失败");
            logger.info(e.getMessage(),e);
        }

        repositoryService.activateProcessDefinitionById(processDefinition.getId());

        logger.info("开始启动");
        activitiRule.getRuntimeService().startProcessInstanceById(processDefinition.getId());
        logger.info("启动成功");

    }


    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testCandidateStarter(){
        RepositoryService repositoryService = activitiRule.getRepositoryService();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();

        logger.info("processDefinition.id = {}",processDefinition.getId());

        // 添加一个指定的用户 user
        repositoryService.addCandidateStarterUser(processDefinition.getId(),"user");
        // 添加一个用户组 用户组名称 groupM
        repositoryService.addCandidateStarterGroup(processDefinition.getId(),"groupM");

        List<IdentityLink> identityLinkList = repositoryService.getIdentityLinksForProcessDefinition(processDefinition.getId());

        for (IdentityLink identityLink : identityLinkList) {
            logger.info("identityLink =  {}",identityLink);
        }


        repositoryService.deleteCandidateStarterGroup(processDefinition.getId(),"groupM");
        repositoryService.deleteCandidateStarterUser(processDefinition.getId(),"user");


    }

}
