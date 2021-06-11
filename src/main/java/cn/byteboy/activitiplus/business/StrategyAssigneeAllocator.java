package cn.byteboy.activitiplus.business;

import lombok.Data;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author hongshaochuan
 * @Date 2021/6/11
 */
public class StrategyAssigneeAllocator implements AssigneeAllocator {

    private final ConcurrentHashMap<Integer, Strategy> strategies = new ConcurrentHashMap<>();

    private final String ALLOCATOR_NAME = "策略分配";


    @Data
    public class Strategy {

        private final String name;

        private final Integer id;

        public Strategy(String name, Integer id) {
            this.name = name;
            this.id = id;
        }
    }


    @Override
    public String getAllocatorName() {
        return ALLOCATOR_NAME;
    }

    @Override
    public List<String> getSelectList() {
        return strategies.values().stream().map(Strategy::getName).collect(Collectors.toList());
    }
}
