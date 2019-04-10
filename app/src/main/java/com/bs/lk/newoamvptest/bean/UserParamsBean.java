package com.bs.lk.newoamvptest.bean;

public class UserParamsBean {
    private String KEYDATA;


    public UserParamsBean() {
    }

    public UserParamsBean(String userName, String passWord) {
        this.KEYDATA = userName;
    }

    public String getUserName() {
        return KEYDATA;
    }

    public void setUserName(String userName) {
        this.KEYDATA = userName;
    }

}
