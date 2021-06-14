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
public class AssigneeAllocatorManager {

    private final Map<String, Map<String, AssigneeAllocator>> map = new ConcurrentHashMap<>();


    public void register(AssigneeAllocator allocator) {
        Map<String, AssigneeAllocator> m = map.getOrDefault(allocator.getAllocatorName(), new ConcurrentHashMap<>());
        m.put(allocator.getName(), allocator);
    }

    public Map<String, Collection<String>> getSelectList() {
        Map<String, Collection<String>> value = new HashMap<>();
        for (Map.Entry<String, Map<String, AssigneeAllocator>> entry : map.entrySet()) {
            value.put(entry.getKey(), entry.getValue().keySet());
        }
        return value;
    }

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
