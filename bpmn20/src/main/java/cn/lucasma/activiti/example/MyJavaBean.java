package cn.lucasma.activiti.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @Author Lucas Ma
 */
public class MyJavaBean implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(MyActivityBehavior.class);


    private String name;

    public String getName() {
        logger.info("run getName name:{}", name);
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MyJavaBean() {
    }

    public MyJavaBean(String name) {
        this.name = name;
    }

    public void sayHello() {
        logger.info("run sayHello");

    }
}
