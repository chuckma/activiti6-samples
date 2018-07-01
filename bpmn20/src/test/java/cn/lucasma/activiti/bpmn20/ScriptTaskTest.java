package cn.lucasma.activiti.bpmn20;

import com.google.common.collect.Maps;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;
import java.util.Map;

/**
 * @Author Lucas Ma
 */
public class ScriptTaskTest {

    private static final Logger logger = LoggerFactory.getLogger(ScriptTaskTest.class);


    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    /**
     * groovy 脚本测试
     */
    @Test
    @Deployment(resources = {"my-process-scripttask1.bpmn20.xml"})
    public void testScriptTask() {
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");

        HistoryService historyService = activitiRule.getHistoryService();
        List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId())
                .orderByVariableName().asc()
                .listPage(0, 100);

        for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
            logger.info("variable = {}", historicVariableInstance);
        }
        logger.info("variables.size = {}", historicVariableInstances.size());

    }

    /**
     * juel 脚本测试
     */
    @Test
    @Deployment(resources = {"my-process-scripttask2.bpmn20.xml"})
    public void testScriptTask2() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", 3);
        variables.put("key2", 5);
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process", variables);

        HistoryService historyService = activitiRule.getHistoryService();
        List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId())
                .orderByVariableName().asc()
                .listPage(0, 100);

        for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
            logger.info("variable = {}", historicVariableInstance);
        }
        logger.info("variables.size = {}", historicVariableInstances.size());

    }

    /**
     * javascript 脚本测试
     */
    @Test
    @Deployment(resources = {"my-process-scripttask3.bpmn20.xml"})
    public void testScriptTask3() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", 3);
        variables.put("key2", 5);
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process", variables);

        HistoryService historyService = activitiRule.getHistoryService();
        List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId())
                .orderByVariableName()
                .desc()
                .listPage(0, 100);

        for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
            logger.info("variable = {}", historicVariableInstance);
        }
        logger.info("variables.size = {}", historicVariableInstances.size());

    }


    /**
     * 脚本引擎测试
     * @throws ScriptException
     */
    @Test
    public void testScriptEngine() throws ScriptException {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("javascript");

        //Object eval1 = scriptEngine.eval("${1 + 2}");
        Object eval2 = scriptEngine.eval("1 + 2");
        //Object eval3 = scriptEngine.eval("${1 + 2}");

        logger.info("value = {}",eval2);
    }
}
