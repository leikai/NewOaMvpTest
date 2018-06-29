package com.bs.lk.newoamvptest.bean;

import java.util.List;

public class DepositDataListsRoot {
    private int TotalCount;

    private int Page;

    private int PageSize;

    private List<DepositDataListsBean> data ;

    public void setTotalCount(int TotalCount){
        this.TotalCount = TotalCount;
    }
    public int getTotalCount(){
        return this.TotalCount;
    }
    public void setPage(int Page){
        this.Page = Page;
    }
    public int getPage(){
        return this.Page;
    }
    public void setPageSize(int PageSize){
        this.PageSize = PageSize;
    }
    public int getPageSize(){
        return this.PageSize;
    }
    public void setData(List<DepositDataListsBean> data){
        this.data = data;
    }
    public List<DepositDataListsBean> getData(){
        return this.data;
    }
}
