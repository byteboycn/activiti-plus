package cn.byteboy.activitiplus.business;

import cn.byteboy.activitiplus.aware.AssigneeAllocatorManagerAware;
import cn.byteboy.activitiplus.aware.RuntimeServiceAware;
import cn.byteboy.activitiplus.aware.TaskServiceAware;
import cn.byteboy.activitiplus.business.BusinessTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;

import java.util.List;

/**
 * @author hongshaochuan
 * @Date 2021/6/10
 */
public interface BusinessAdapter extends TaskListener, TaskServiceAware, RuntimeServiceAware, AssigneeAllocatorManagerAware {

    String getBusinessName();

    List<BusinessTask> getTasks(String assignee);

    void complete(BusinessTask task);

    void startProcessInstance(String businessKey, String startUserId);


    ProcessDefinition getProcessDefinition();
}
