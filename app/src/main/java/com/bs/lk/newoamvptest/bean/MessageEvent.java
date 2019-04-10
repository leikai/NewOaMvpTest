package com.bs.lk.newoamvptest.bean;

/**
 * 待办信息传递实体类
 * @author lk
 */
public class MessageEvent {
    public final String message;
    public String taskCount;



    public MessageEvent(String message, String taskCount) {
        this.message = message;
        this.taskCount = taskCount;

    }

    public String getMessage() {
        return message;
    }
    public String getTaskCount() {
        return taskCount;
    }
}
