package cn.byteboy.activitiplus.business;

import lombok.Getter;
import lombok.Setter;

/**
 * @author hongshaochuan
 * @Date 2021/6/10
 */
@Getter
@Setter
public class BusinessTask extends AbstractBusinessTask {

    // TODO 系统相关的字段和业务相关的字段解耦
    private String id;

    /** 业务相关的变量 */
    private String assignee;

    private String name;

    private String businessKey;


    @Override
    public String toString() {
        return "BusinessTask{" +
                "id='" + id + '\'' +
                ", assignee='" + assignee + '\'' +
                ", name='" + name + '\'' +
                ", businessKey='" + businessKey + '\'' +
                '}';
    }
}
