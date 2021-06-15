package cn.byteboy.activitiplus.business.impl;

import cn.byteboy.activitiplus.aware.AssigneeAllocatorManagerAware;
import cn.byteboy.activitiplus.aware.RuntimeServiceAware;
import cn.byteboy.activitiplus.aware.TaskServiceAware;
import cn.byteboy.activitiplus.business.AssigneeAllocator;
import cn.byteboy.activitiplus.business.AssigneeAllocatorManager;
import cn.byteboy.activitiplus.business.BusinessAdapter;
import cn.byteboy.activitiplus.business.BusinessTask;
import cn.byteboy.activitiplus.converter.TaskConverter;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
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
                .processDefinitionKey(processDefinition.getKey())
                .taskAssignee(assignee)
                .list();
        return TaskConverter.toBusiness(tasks);
    }

    @Override
    public void complete(BusinessTask task) {
        if (task != null) {
            taskService.complete(task.getId());
        }
    }

    @Override
    public void startProcessInstance(String businessKey, String startUserId) {
        Authentication.setAuthenticatedUserId(startUserId);
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), businessKey);
    }

    @Override
    public ProcessDefinition getProcessDefinition() {
        return this.processDefinition;
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        if ("create".equals(delegateTask.getEventName())) {
            // 这个时候还查不出来
//            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
//                    .processInstanceId(delegateTask.getProcessInstanceId())
//                    .singleResult();

            ProcessInstance processInstance = null;
            if (delegateTask instanceof TaskEntity) {
                processInstance = ((TaskEntity) delegateTask).getProcessInstance();
            }
            if (processInstance == null) {
                return;
            }
            String startUserId = processInstance.getStartUserId();
            AssigneeAllocator allocator = manager.getAllocator(delegateTask.getAssignee());
            if (allocator != null) {
                String realAssignee = allocator.getValue(startUserId);
                delegateTask.setAssignee(realAssignee);
            }
        }

    }
}
