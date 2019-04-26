package com.bs.lk.newoamvptest.bean;

public class TokenRoot {
    /**
     * opresult : true
     * msg : 登录成功!
     * msginfo : {"token":"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJqd3QiLCJpYXQiOjE1NTUzOTU0NjQsInN1YiI6IntcIm9yZ0lkXCI6XCIwNDgwODY4MzRiNzIxNWU3MDE0YjcyODljYTg2MDA3OFwiLFwiZGVwdE5hbWVcIjpcIui9r-S7tuW8gOWPkemDqFwiLFwicHdkXCI6XCIxMjM0NTY3XCIsXCJwaG9uZVwiOlwiMTgzMzUxODI5MzhcIixcIm9yZ0NvZGVcIjpcIjAwMVwiLFwidXNlcklkXCI6XCIwNGE0ODE5NjVkMWM4YmE1MDE1ZDJiYzA5YTViMDA0MlwiLFwidXNlck5hbWVcIjpcIumbt-WHr1wiLFwiZGVwdENvZGVcIjpcIjAwNVwiLFwib3JnTmFtZVwiOlwi5bGx6KW_55m-5pav5aWl5qC856eR5oqA5pyJ6ZmQ5YWs5Y-4XCIsXCJkZXB0SWRcIjpcIjJjOWE1ZTljNjk2YWE3NGMwMTY5NmFiOTA5ZGMwMDA2XCIsXCJ1c2VyTG9naW5OYW1lXCI6XCJsZWlrYWlcIn0iLCJleHAiOjE1NTU4Mjc0NjR9.RTl_YM9VJFdKq24vsPg8E7kfRDHPxZI4vn52pHtOXDE"}
     */
    private boolean opresult;

    private String msg;

    private String msginfo;

    public void setOpresult(boolean opresult){
        this.opresult = opresult;
    }
    public boolean getOpresult(){
        return this.opresult;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
    public void setMsginfo(String msginfo){
        this.msginfo = msginfo;
    }
    public String getMsginfo(){
        return this.msginfo;
    }
}
