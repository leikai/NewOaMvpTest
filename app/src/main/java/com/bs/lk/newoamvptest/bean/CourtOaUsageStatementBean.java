package com.bs.lk.newoamvptest.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 法院OA使用情况表
 */
public class CourtOaUsageStatementBean extends DataSupport  implements Serializable {
    private int id;
    private String courtName;
    private String useType;
    private String useTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }
}
