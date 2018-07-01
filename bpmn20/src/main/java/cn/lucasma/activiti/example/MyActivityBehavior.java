package cn.lucasma.activiti.example;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.delegate.ActivityBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author Lucas Ma
 */
public class MyActivityBehavior implements ActivityBehavior {
    private static final Logger logger = LoggerFactory.getLogger(MyActivityBehavior.class);

    @Override
    public void execute(DelegateExecution execution) {
        logger.info("run my activity behavior");
    }
}
