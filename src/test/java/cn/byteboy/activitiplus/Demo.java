package cn.byteboy.activitiplus;

import cn.byteboy.activitiplus.activiti.Deploy;
import cn.byteboy.activitiplus.business.*;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author hongshaochuan
 * @date 2021/6/14
 */
@SpringBootTest
public class Demo {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;


    private final BusinessRegistry registry = new DefaultBusinessRegistry();

    {
        GlobalTaskListener.INSTANCE.setBusinessRegistry(registry);
    }

    @Before
    public void init() throws IOException, XMLStreamException {
        // 部署流程
        InputStream inputStream = new ClassPathResource("pro_manual/demo.bpmn").getInputStream();
        Deploy deploy = new Deploy();
        deploy.deploy("demo流程", "demo.bpmn", inputStream);

        // 获取最新版本的流程定义
        Optional<ProcessDefinition> processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("demo")
                .orderByProcessDefinitionVersion()
                .desc()
                .list()
                .stream().findFirst();

        if (!processDefinition.isPresent()) {
            return;
        }

        // 创建分配策略
        AssigneeAllocatorManager manager = new AssigneeAllocatorManager();
        manager.register(new FixedAssigneeAllocator("张三", "zhangsan"));
        manager.register(new FixedAssigneeAllocator("李四", "lisi"));
        manager.register(new StrategyAssigneeAllocator("任何人都交给老板处理", startUserId -> "boss"));

        // 业务适配器
        BusinessAdapter adapter = new DefaultBusinessAdapter("demo", processDefinition.get());
        adapter.setTaskService(taskService);
        adapter.setRuntimeService(runtimeService);
        adapter.setAssigneeAllocatorManager(manager);

        registry.registerBusiness(adapter);
    }

    @Test
    public void test() {

        BusinessAdapter demo = registry.getBusinessAdapter("demo");
        demo.startProcessInstance("test", "hsc");
//        demo.getTasks()

    }
}
