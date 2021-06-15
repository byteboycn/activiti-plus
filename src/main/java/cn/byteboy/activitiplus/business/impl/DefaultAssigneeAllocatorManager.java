package cn.byteboy.activitiplus.business.impl;

import cn.byteboy.activitiplus.business.AssigneeAllocator;
import cn.byteboy.activitiplus.business.AssigneeAllocatorManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hongshaochuan
 * @Date 2021/6/15
 */
public class DefaultAssigneeAllocatorManager implements AssigneeAllocatorManager {

    private final Map<String, Map<String, AssigneeAllocator>> map = new ConcurrentHashMap<>();

    @Override
    public void register(AssigneeAllocator allocator) {
        Map<String, AssigneeAllocator> m = map.get(allocator.getAllocatorName());
        if (m == null) {
            m = new ConcurrentHashMap<>();
            map.put(allocator.getAllocatorName(), m);
        }
        m.put(allocator.getName(), allocator);
    }

    @Override
    public Map<String, Collection<String>> getSelectList() {
        Map<String, Collection<String>> value = new HashMap<>();
        for (Map.Entry<String, Map<String, AssigneeAllocator>> entry : map.entrySet()) {
            value.put(entry.getKey(), entry.getValue().keySet());
        }
        return value;
    }

    @Override
    public AssigneeAllocator getAllocator(String expression) {
        String[] split = expression.split(":");
        String k1 = split[0];
        String k2 = split[1];
        Map<String, AssigneeAllocator> m = map.get(k1);
        if (m != null) {
            return m.get(k2);
        }
        return null;
    }
}
