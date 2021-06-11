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

    /** 业务相关的变量 */
    private String assignee;

    private String name;

    private String businessKey;

}
