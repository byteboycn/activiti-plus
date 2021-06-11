package cn.byteboy.activitiplus.aware;

import org.activiti.engine.RuntimeService;

/**
 * @author hongshaochuan
 * @Date 2021/6/10
 */
public interface RuntimeServiceAware {

    void setRuntimeService(RuntimeService runtimeService);
}
