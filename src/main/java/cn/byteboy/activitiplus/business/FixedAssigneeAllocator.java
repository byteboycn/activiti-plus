package cn.byteboy.activitiplus.business;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hongshaochuan
 * @date 2021/6/11
 */
public class FixedAssigneeAllocator implements AssigneeAllocator {

    private static final String ALLOCATOR_NAME = "固定分配";

    private final String name;

    private final String value;

    public FixedAssigneeAllocator(String name, String value) {
        this.name = name;
        this.value = value;
    }


    @Override
    public String getAllocatorName() {
        return ALLOCATOR_NAME;
    }

    @Override
    public String getName() {
        return this.name;
    }


    @Override
    public String getValue(String startUserId) {
        return value;
    }
}
