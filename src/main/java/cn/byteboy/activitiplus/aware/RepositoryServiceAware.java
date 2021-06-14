package cn.byteboy.activitiplus.aware;

import org.activiti.engine.RepositoryService;

/**
 * @author hongshaochuan
 * @date 2021/6/14
 */
public interface RepositoryServiceAware {

    void setRepositoryService(RepositoryService repositoryService);
}
