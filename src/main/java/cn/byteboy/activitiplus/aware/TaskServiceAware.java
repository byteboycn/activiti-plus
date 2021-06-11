package cn.byteboy.activitiplus.aware;

import org.activiti.engine.TaskService;

/**
 * @author hongshaochuan
 * @Date 2021/6/10
 */
public interface TaskServiceAware {

    void setTaskService(TaskService taskService);
}
