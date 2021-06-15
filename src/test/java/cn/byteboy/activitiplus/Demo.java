package cn.byteboy.activitiplus;

import cn.byteboy.activitiplus.activiti.Deploy;
import cn.byteboy.activitiplus.business.*;
import cn.byteboy.activitiplus.business.impl.DefaultAssigneeAllocatorManager;
import cn.byteboy.activitiplus.business.impl.DefaultBusinessAdapter;
import cn.byteboy.activitiplus.business.impl.DefaultBusinessRegistry;
import cn.byteboy.activitiplus.business.impl.FixedAssigneeAllocator;
import cn.byteboy.activitiplus.business.impl.StrategyAssigneeAllocator;
import cn.byteboy.activitiplus.listener.GlobalTaskListenerExecutor;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

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
        GlobalTaskListenerExecutor.INSTANCE.setBusinessRegistry(registry);
    }

    public void deploy() throws IOException, XMLStreamException {
        InputStream inputStream = new ClassPathResource("pro_manual/MyProcess.bpmn").getInputStream();
        Deploy deploy = new Deploy();
        deploy.setRepositoryService(repositoryService);
        deploy.deploy("demo流程", "demo.bpmn", inputStream);
    }

    @BeforeEach
    public void init() throws IOException, XMLStreamException {
        // 部署流程
//        deploy();

        // 获取最新版本的流程定义
        Optional<ProcessDefinition> processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myProcess")
                .orderByProcessDefinitionVersion()
                .desc()
                .list()
                .stream().findFirst();

        if (!processDefinition.isPresent()) {
            return;
        }

        // 创建分配策略
        AssigneeAllocatorManager manager = new DefaultAssigneeAllocatorManager();
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

    @Test
    public void test1() {
        BusinessAdapter demo = registry.getBusinessAdapter("demo");
        List<BusinessTask> list = demo.getTasks("lisi");
        System.out.println(list);
    }

    @Test
    public void test2() {
        BusinessAdapter demo = registry.getBusinessAdapter("demo");
        List<BusinessTask> list = demo.getTasks("zhangsan");
        System.out.println(list);
        list.forEach(demo::complete);
    }


}
