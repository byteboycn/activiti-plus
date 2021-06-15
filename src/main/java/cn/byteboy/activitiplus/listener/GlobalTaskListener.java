package cn.byteboy.activitiplus.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @author hongshaochuan
 * @date 2021/6/11
 */
public class GlobalTaskListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        GlobalTaskListenerExecutor.INSTANCE.notify(delegateTask);
    }
}
