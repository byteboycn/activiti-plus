package cn.byteboy.activitiplus;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @author hongshaochuan
 * @date 2021/6/12
 */
public class Listener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("Listener: " + delegateTask.getEventName());
    }
}
