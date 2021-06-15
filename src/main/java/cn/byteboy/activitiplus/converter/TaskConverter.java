package cn.byteboy.activitiplus.converter;

import cn.byteboy.activitiplus.business.BusinessTask;
import org.activiti.engine.task.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hongshaochuan
 * @Date 2021/6/10
 */
public class TaskConverter {

    public static BusinessTask toBusiness(Task task) {
        BusinessTask b = new BusinessTask();
        b.setId(task.getId());
        b.setBusinessKey(task.getBusinessKey());
        b.setName(task.getName());
        b.setAssignee(task.getAssignee());
        return b;
    }

    public static List<BusinessTask> toBusiness(List<Task> tasks) {
        List<BusinessTask> list = new ArrayList<>(tasks.size());
        tasks.forEach(t -> list.add(toBusiness(t)));
        return list;
    }
}
