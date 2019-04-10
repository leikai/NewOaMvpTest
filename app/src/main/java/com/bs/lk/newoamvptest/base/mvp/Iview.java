package com.bs.lk.newoamvptest.base.mvp;

/**
 *  @Description MVP之V层 是所有VIEW的基类，其他类可以继承该类
 * @author lk
 */
public interface Iview<T> {
    /**
     * @description 全局的显示加载框
     * @author ydc
     * @createDate
     * @version 1.0
     */
    void showLoading();

    /**
     * @description 全局的显示加载框
     * @author ydc
     * @createDate
     * @version 1.0
     */
    void showLoading(String msg);
    /**
     * @description 全局的显示加载框
     * @author ydc
     * @createDate
     * @version 1.0
     */
    void showLoading(String msg, int progress);
    /**
     * @description 全局的隐藏加载框
     * @author ydc
     * @createDate
     * @version 1.0
     */
    void hideLoading();
    /**
     * @description 全局消息展示
     * @author ydc
     * @createDate
     * @version 1.0
     */
    void showMsg(String msg);
    /**
     * @description 全局错误消息展示
     * @author ydc
     * @createDate
     * @version 1.0
     */
    void showErrorMsg(String msg, String content);
    /**
     * @description 关闭界面信息
     * @author ydc
     * @createDate
     * @version 1.0
     */
    void close();

    /**
     * @description 当前fragment是否有效
     * @author ydc
     * @createDate
     * @version 1.0
     */
    boolean isActive();
}
