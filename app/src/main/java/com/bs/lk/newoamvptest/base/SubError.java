package com.bs.lk.newoamvptest.base;

import java.io.Serializable;

public class SubError implements Serializable {
    private static final long serialVersionUID = 1L;
    private String code;
    private String message;

    public String getCode()
    {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
