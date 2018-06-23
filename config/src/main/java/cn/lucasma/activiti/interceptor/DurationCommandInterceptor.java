package cn.lucasma.activiti.interceptor;

import org.activiti.engine.impl.interceptor.AbstractCommandInterceptor;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author Lucas Ma
 */
public class DurationCommandInterceptor extends AbstractCommandInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(DurationCommandInterceptor.class);

    @Override
    public <T> T execute(CommandConfig config, Command<T> command) {
        long start = System.currentTimeMillis();
        try {
            return this.next.execute(config, command);
        }finally {
             long duration = System.currentTimeMillis() - start;
            logger.info("{},执行时长{} 毫秒", command.getClass().getSimpleName(), duration);
        }
    }
}
