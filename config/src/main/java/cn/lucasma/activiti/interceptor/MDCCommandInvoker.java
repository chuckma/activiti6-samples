package cn.lucasma.activiti.interceptor;

import org.activiti.engine.debug.ExecutionTreeUtil;
import org.activiti.engine.impl.agenda.AbstractOperation;
import org.activiti.engine.impl.interceptor.DebugCommandInvoker;
import org.activiti.engine.logging.LogMDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author Lucas Ma
 */
public class MDCCommandInvoker extends DebugCommandInvoker {

    private static final Logger logger = LoggerFactory.getLogger(DebugCommandInvoker.class);

    @Override
    public void executeOperation(Runnable runnable) {
        // 获取之前的 MDC 是否生效， 生效说明 MDC 有内容
        boolean mdcEnabled = LogMDC.isMDCEnabled();
        // MDC 生效
        LogMDC.setMDCEnabled(true);
        if (runnable instanceof AbstractOperation) {
            AbstractOperation operation = (AbstractOperation) runnable;

            if (operation.getExecution() != null) {
                // 如果是一个可操作的流程对象，就把信息放到MDC上下文里
                // 在上下文执行的过程中，在里面操作日志都可以打印出上下文信息
                LogMDC.putMDCExecution(operation.getExecution());

            }

        }

        super.executeOperation(runnable);
        // 清理上下文信息
        LogMDC.clear();
        // 如果原来 MDC 是不生效的，则还原为它本来的状态
        if (!mdcEnabled) {
            LogMDC.setMDCEnabled(false);
        }
    }
}
