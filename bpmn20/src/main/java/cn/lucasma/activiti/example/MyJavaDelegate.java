package cn.lucasma.activiti.example;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @Author Lucas Ma
 */
public class MyJavaDelegate implements JavaDelegate,Serializable {
    private static final Logger logger = LoggerFactory.getLogger(MyJavaDelegate.class);

    private Expression name;
    private Expression desc;
    @Override
    public void execute(DelegateExecution execution) {
        if (name != null) {
            Object value = name.getValue(execution);
            logger.info("name = {}", value);
        }

        if (desc != null) {
            Object value = desc.getValue(execution);
            logger.info("desc = {}", value);
        }
        logger.info("run my java delegate {}", this);

    }
}
