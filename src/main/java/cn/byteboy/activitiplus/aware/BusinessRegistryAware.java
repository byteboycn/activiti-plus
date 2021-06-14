package cn.byteboy.activitiplus.aware;

import cn.byteboy.activitiplus.business.BusinessRegistry;

/**
 * @author hongshaochuan
 * @date 2021/6/12
 */
public interface BusinessRegistryAware {

    void setBusinessRegistry(BusinessRegistry registry);
}
