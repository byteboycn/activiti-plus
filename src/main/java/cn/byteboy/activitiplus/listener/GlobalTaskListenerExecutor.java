package cn.byteboy.activitiplus.listener;

import cn.byteboy.activitiplus.aware.BusinessRegistryAware;
import cn.byteboy.activitiplus.business.BusinessAdapter;
import cn.byteboy.activitiplus.business.BusinessRegistry;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @author hongshaochuan
 * @Date 2021/6/15
 */
public final class GlobalTaskListenerExecutor implements TaskListener, BusinessRegistryAware {

    public static volatile GlobalTaskListenerExecutor INSTANCE = new GlobalTaskListenerExecutor();

    private BusinessRegistry businessRegistry;

    private GlobalTaskListenerExecutor() {}

    @Override
    public void setBusinessRegistry(BusinessRegistry registry) {
        this.businessRegistry = registry;
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        if (businessRegistry == null) {
            throw new RuntimeException("businessRegistry not be null");
        }
        BusinessAdapter adapter = businessRegistry.getBusinessAdapterByProcessDefinition(delegateTask.getProcessDefinitionId());
        if (adapter == null) {
            System.out.println("adapter not found, ProcessDefinitionId:" + delegateTask.getProcessDefinitionId());
            return;
        }
        adapter.notify(delegateTask);
    }
}
