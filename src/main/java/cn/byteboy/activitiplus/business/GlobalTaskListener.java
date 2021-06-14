package cn.byteboy.activitiplus.business;

import cn.byteboy.activitiplus.aware.BusinessRegistryAware;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @author hongshaochuan
 * @date 2021/6/11
 */
public final class GlobalTaskListener implements TaskListener, BusinessRegistryAware {

    public static volatile GlobalTaskListener INSTANCE = new GlobalTaskListener();

    private BusinessRegistry businessRegistry;

    private GlobalTaskListener() {}

    @Override
    public void notify(DelegateTask delegateTask) {
        if (businessRegistry == null) {
            throw new RuntimeException("businessRegistry not be null");
        }
        BusinessAdapter adapter = businessRegistry.getBusinessAdapterByProcessDefinition(delegateTask.getProcessDefinitionId());
        adapter.notify(delegateTask);

    }

    @Override
    public void setBusinessRegistry(BusinessRegistry registry) {
        this.businessRegistry = registry;
    }
}
