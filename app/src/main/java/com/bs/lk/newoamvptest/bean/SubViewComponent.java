package com.bs.lk.newoamvptest.bean;

/**
 * Created by baggio on 2017/3/2.
 */

public class SubViewComponent extends Component {
    public static final String TIME_INITVALUE = "nowtime";
    public static final String INPUTTIME = "inputtime";
    private String mName;
    private String mInitValue;
    public void setName(String mName) {
        this.mName = mName;
    }

    public String getName() {
        return mName;
    }

    public void setInitValue(String mInitValue) {
        this.mInitValue = mInitValue;
    }

    public String getInitValue() {
        return mInitValue;
    }
}
