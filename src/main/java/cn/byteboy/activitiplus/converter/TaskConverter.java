package cn.byteboy.activitiplus.converter;

import cn.byteboy.activitiplus.business.BusinessTask;
import org.activiti.engine.task.Task;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author hongshaochuan
 * @Date 2021/6/10
 */
public interface TaskConverter {

    TaskConverter INSTANCE = Mappers.getMapper(TaskConverter.class);

    List<BusinessTask> toBusiness(List<Task> tasks);
}
