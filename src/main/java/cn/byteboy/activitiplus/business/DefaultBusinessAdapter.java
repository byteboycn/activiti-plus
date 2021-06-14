package cn.byteboy.activitiplus.business;

import cn.byteboy.activitiplus.aware.AssigneeAllocatorManagerAware;
import cn.byteboy.activitiplus.aware.RuntimeServiceAware;
import cn.byteboy.activitiplus.aware.TaskServiceAware;
import cn.byteboy.activitiplus.converter.TaskConverter;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.List;

/**
 * @author hongshaochuan
 * @Date 2021/6/10
 */
public class DefaultBusinessAdapter implements BusinessAdapter {

    private final String businessName;

    private final ProcessDefinition processDefinition;

    private TaskService taskService;

    private RuntimeService runtimeService;

    private AssigneeAllocatorManager manager;

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
    public void setAssigneeAllocatorManager(AssigneeAllocatorManager manager) {
        this.manager = manager;
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
    public void startProcessInstance(String businessKey, String startUserId) {
        Authentication.setAuthenticatedUserId(startUserId);
        runtimeService.startProcessInstanceById(processDefinition.getId(), businessKey);
    }

    @Override
    public ProcessDefinition getProcessDefinition() {
        return this.processDefinition;
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        if ("create".equals(delegateTask.getEventName())) {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(delegateTask.getProcessInstanceId())
                    .singleResult();
            String startUserId = processInstance.getStartUserId();
            AssigneeAllocator allocator = manager.getAllocator(delegateTask.getAssignee());
            if (allocator != null) {
                String realAssignee = allocator.getValue(startUserId);
                delegateTask.setAssignee(realAssignee);
            }
        }

    }
}
