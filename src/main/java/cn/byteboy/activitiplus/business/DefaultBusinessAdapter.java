package cn.byteboy.activitiplus.business;

import cn.byteboy.activitiplus.aware.RuntimeServiceAware;
import cn.byteboy.activitiplus.aware.TaskServiceAware;
import cn.byteboy.activitiplus.converter.TaskConverter;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;

import java.util.List;

/**
 * @author hongshaochuan
 * @Date 2021/6/10
 */
public class DefaultBusinessAdapter implements BusinessAdapter, TaskServiceAware, RuntimeServiceAware {

    private final String businessName;

    private final ProcessDefinition processDefinition;

    private TaskService taskService;

    private RuntimeService runtimeService;

    public DefaultBusinessAdapter(String businessName, ProcessDefinition processDefinition) {
        if (businessName == null || processDefinition == null) {
            throw new RuntimeException();
        }
        this.businessName = businessName;
        this.processDefinition = processDefinition;
    }

    @Override
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @Override
    public String getBusinessName() {
        return this.businessName;
    }

    @Override
    public List<BusinessTask> getTasks(String assignee) {
        List<Task> tasks = taskService.createTaskQuery()
                .processDefinitionId(processDefinition.getId())
                .taskAssignee(assignee)
                .list();
        return TaskConverter.INSTANCE.toBusiness(tasks);
    }

    @Override
    public void complete(BusinessTask task) {
        if (task != null) {
            taskService.complete(task.id);
        }
    }

    @Override
    public void startProcessInstance(String businessKey) {
        runtimeService.startProcessInstanceById(processDefinition.getId(), businessKey);
    }
}
