package cn.byteboy.activitiplus.business;

/**
 * @author hongshaochuan
 * @Date 2021/6/10
 */
public interface BusinessRegistry {

    void registerBusiness(BusinessAdapter businessAdapter);

    BusinessAdapter getBusinessAdapter(String businessName);
}
