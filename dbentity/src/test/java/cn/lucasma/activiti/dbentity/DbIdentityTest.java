package cn.lucasma.activiti.dbentity;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author Lucas Ma
 */
public class DbIdentityTest {
    private static final Logger logger = LoggerFactory.getLogger(DbIdentityTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql.cfg.xml");

    @Test
    public void testIdentity(){
        IdentityService identityService = activitiRule.getIdentityService();

        User user1 = identityService.newUser("user1");
        user1.setFirstName("firstName");
        user1.setLastName("lastName");
        user1.setEmail("user1@126.com");
        user1.setPassword("pwd");
        identityService.saveUser(user1);

        User user2 = identityService.newUser("user2");
        identityService.saveUser(user2);
        Group group1 = identityService.newGroup("group1");
        group1.setName("for test");
        identityService.saveGroup(group1);

        identityService.createMembership(user1.getId(),group1.getId());
        identityService.createMembership(user2.getId(),group1.getId());

        identityService.setUserInfo(user1.getId(),"age","18");
        identityService.setUserInfo(user1.getId(),"address","bei jing");


    }


}
