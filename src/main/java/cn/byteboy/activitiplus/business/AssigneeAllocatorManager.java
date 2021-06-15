package cn.byteboy.activitiplus.business;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author hongshaochuan
 * @date 2021/6/12
 */
public interface AssigneeAllocatorManager {

    void register(AssigneeAllocator allocator);

    Map<String, Collection<String>> getSelectList();

    AssigneeAllocator getAllocator(String expression);

}
