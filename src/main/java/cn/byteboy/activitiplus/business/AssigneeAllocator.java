package cn.byteboy.activitiplus.business;

import java.util.List;

/**
 * @author hongshaochuan
 * @Date 2021/6/11
 */
public interface AssigneeAllocator {

    /**
     * 获取分类名称
     *
     * @return
     */
    String getAllocatorName();


    /**
     * 获取值名称
     *
     * @return
     */
    String getName();

    /**
     * 获取处理人
     *
     * @param startUserId 任务启动者
     * @return
     */
    String getValue(String startUserId);
}
