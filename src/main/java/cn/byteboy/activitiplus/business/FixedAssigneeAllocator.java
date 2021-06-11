package cn.byteboy.activitiplus.business;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hongshaochuan
 * @Date 2021/6/11
 */
public class FixedAssigneeAllocator implements AssigneeAllocator {

    private final String ALLOCATOR_NAME = "固定分配";

    private final List<String> list = new ArrayList<>();

    @Override
    public String getAllocatorName() {
        return ALLOCATOR_NAME;
    }

    @Override
    public List<String> getSelectList() {
        return list;
    }
}
