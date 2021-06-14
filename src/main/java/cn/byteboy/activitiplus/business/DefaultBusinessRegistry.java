package cn.byteboy.activitiplus.business;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hongshaochuan
 * @Date 2021/6/10
 */
@Slf4j
public class DefaultBusinessRegistry implements BusinessRegistry {

    private final ConcurrentHashMap<String, BusinessAdapter> map = new ConcurrentHashMap<>();

    @Override
    public void registerBusiness(BusinessAdapter businessAdapter) {
        if (map.containsKey(businessAdapter.getBusinessName())) {
            log.warn(businessAdapter.getBusinessName() + " adapter is existed, will be cover");
        }
        map.put(businessAdapter.getBusinessName(), businessAdapter);
    }

    @Override
    public BusinessAdapter getBusinessAdapter(String businessName) {
        return this.map.get(businessName);
    }

    @Override
    public BusinessAdapter getBusinessAdapterByProcessDefinition(String key) {
        for (Map.Entry<String, BusinessAdapter> entry : map.entrySet()) {
            if (entry.getValue().getProcessDefinition().getId().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }
}
