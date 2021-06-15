package cn.byteboy.activitiplus.activiti;

import cn.byteboy.activitiplus.aware.RepositoryServiceAware;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.RepositoryService;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author hongshaochuan
 * @date 2021/6/14
 */
public class Deploy implements RepositoryServiceAware {


    private RepositoryService repositoryService;


    @Override
    public void setRepositoryService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    // TODO 如何避免重复部署
    public void deploy(String name, String resourceName, InputStream inputStream) throws XMLStreamException {


        BpmnXMLConverter converter = new BpmnXMLConverter();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(inputStream);
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
                .name(name)
                .addBpmnModel(resourceName, bpmnModel)
                .deploy();
    }

    private ActivitiListener createListenerModel() {

        ActivitiListener listener = new ActivitiListener();
        listener.setEvent("all");
        listener.setImplementation("cn.byteboy.activitiplus.listener.GlobalTaskListener");
        listener.setImplementationType("class");
        return listener;
    }
}
