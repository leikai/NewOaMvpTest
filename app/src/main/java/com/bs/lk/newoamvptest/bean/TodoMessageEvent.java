package com.bs.lk.newoamvptest.bean;

/**
 * 待办通知实体类
 * 作用：服务数据发生变化时，通知页面更新数据
 */
public class TodoMessageEvent {
    private String message;
    public  TodoMessageEvent(String message){
        this.message=message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
