package cn.byteboy.activitiplus.business.impl;

import cn.byteboy.activitiplus.business.AssigneeAllocator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author hongshaochuan
 * @Date 2021/6/11
 */
@Slf4j
public class StrategyAssigneeAllocator implements AssigneeAllocator {

//    private final ConcurrentHashMap<Integer, Strategy> strategies = new ConcurrentHashMap<>();

    private static final String ALLOCATOR_NAME = "策略分配";

    private final String name;

    private final Function<String, String> function;

    public StrategyAssigneeAllocator(String name, Function<String, String> function) {
        this.name = name;
        this.function = function;
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
        return function.apply(startUserId);
    }
}
