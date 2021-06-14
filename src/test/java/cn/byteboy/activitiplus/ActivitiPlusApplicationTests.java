package cn.byteboy.activitiplus;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 资料参考
 * 监听器参考：https://blog.csdn.net/worn_xiao/article/details/81058266
 * BpmnModel对象参考：https://blog.csdn.net/qq_43719932/article/details/113820874
 */
@SpringBootTest
class ActivitiPlusApplicationTests {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Before
    public void init() {

    }

    @Test
    public void test() {

        DeploymentBuilder builder = repositoryService.createDeployment()
                .addClasspathResource("pro_manual/demo.bpmn")
                .name("出差申请流程");

        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("pro_manual/demo.bpmn")
                .name("出差申请流程")
                .deploy();
//        4、输出部署信息
        System.out.println("流程部署id：" + deployment.getId());
        System.out.println("流程部署名称：" + deployment.getName());
    }

    @Test
    public void test2() {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        List<ProcessDefinition> definitionList = processDefinitionQuery.processDefinitionKey("demo")
                .orderByProcessDefinitionVersion()
                .desc()
                .list();
//      输出流程定义信息
        for (ProcessDefinition processDefinition : definitionList) {
            System.out.println("流程定义 id="+processDefinition.getId());
            System.out.println("流程定义 name="+processDefinition.getName());
            System.out.println("流程定义 key="+processDefinition.getKey());
            System.out.println("流程定义 Version="+processDefinition.getVersion());
            System.out.println("流程部署ID ="+processDefinition.getDeploymentId());
        }
    }

    @Test
    public void parse() throws IOException, XMLStreamException {
//        Thread.currentThread().getContextClassLoader()

        InputStream inputStream = new ClassPathResource("pro_manual/demo.bpmn").getInputStream();
        BpmnXMLConverter converter = new BpmnXMLConverter();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(inputStream);//createXmlStreamReader
        //将xml文件转换成BpmnModel
        BpmnModel bpmnModel = converter.convertToBpmnModel(reader);

        List<Process> processes = bpmnModel.getProcesses();
        for (Process process : processes) {
            Collection<FlowElement> flowElements = process.getFlowElements();
            for (FlowElement flowElement : flowElements) {
                if (flowElement instanceof UserTask) {
                    UserTask userTask = (UserTask) flowElement;
                    userTask.setTaskListeners(Collections.singletonList(createListenerModel()));
                }
            }
        }

        repositoryService.createDeployment()
                .name("bpmn")
                .addBpmnModel("modify.bpmn", bpmnModel)
                .deploy();

//        repositoryService.createDeployment()
//                .name("请假申请1")
//                .addBpmnModel("demo1.bpmn", bpmnModel)
//                .deploy();


    }

    @Test
    public void deploy() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("pro_manual/MyProcess.bpmn")
                .name("test")
                .deploy();
    }

    @Test
    public void test3() {

        Authentication.setAuthenticatedUserId("hsc");
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("demo", "bizKey");

        System.out.println(processInstance.getStartUserId());
        System.out.println(processInstance);
    }

    @Test
    public void test4() {
        List<Task> tasks= taskService.createTaskQuery()
                .processDefinitionKey("demo") //流程Key
                .list();
        for (Task task : tasks) {
            if (task != null) {
                System.out.println(task);
                taskService.complete(task.getId());
            }
        }

    }

    @Test
    public void test5() {
        Task task= taskService.createTaskQuery()
                .processDefinitionKey("demo") //流程Key
                .singleResult();
        if (task != null) {
            System.out.println(task);
            taskService.complete(task.getId());
        }
    }

    public ActivitiListener createListenerModel() {

        ActivitiListener listener = new ActivitiListener();
        listener.setEvent("all");
        listener.setImplementation("cn.byteboy.activitiplus.Listener");
        listener.setImplementationType("class");
        listener.setInstance(new Listener());
        return listener;
    }

    public void t() {


//        Thread.currentThread()
    }

}
