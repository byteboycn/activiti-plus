package cn.byteboy.activitiplus.business;

import cn.byteboy.activitiplus.business.BusinessTask;
import org.activiti.engine.task.Task;

import java.util.List;

/**
 * @author hongshaochuan
 * @Date 2021/6/10
 */
public interface BusinessAdapter {

    String getBusinessName();

    List<BusinessTask> getTasks(String assignee);

    void complete(BusinessTask task);

    void startProcessInstance(String businessKey);
}
