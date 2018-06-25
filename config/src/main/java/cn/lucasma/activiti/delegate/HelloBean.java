package cn.lucasma.activiti.delegate;

import cn.lucasma.activiti.event.JobEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author Lucas Ma
 */
public class HelloBean {

    private static final Logger logger = LoggerFactory.getLogger(HelloBean.class);
    public void sayHello(){
        logger.info("sayHello");
    }

}
