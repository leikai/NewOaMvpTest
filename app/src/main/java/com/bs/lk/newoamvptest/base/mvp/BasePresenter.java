package com.bs.lk.newoamvptest.base.mvp;


import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.Observable;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.internal.util.SubscriptionList;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Description 抽象的公用Presenter
 * @author lk
 */
public abstract class BasePresenter <T extends Iview> implements Ipresenter<T> {
    protected T mMvpView;//所有View
    protected CompositeDisposable mSubscriptions;//rx注册中心
    protected DataRepository mDataCenter;//数据中心
    //protected abstract SubscriptionList createSubscriptionList();//引入darger后取缔

    /**
     * @description 获取V
     * @author ydc
     * @createDate
     * @version 1.0
     */
    public T getMvpView() {
        return mMvpView;
    }

    /**
     * @description view绑定P的时候初始化
     * @author ydc
     * @createDate
     * @version 1.0
     */
    @Override
    public void attachView(T view) {
        this.mMvpView = view;
        this.mSubscriptions = new CompositeDisposable();
        this.mDataCenter = DataRepository.getInstance();
    }

    /**
     * @description view失去绑定清除
     * @author ydc
     * @createDate
     * @version 1.0
     */
    @Override
    public void detachView() {
        unsubscribe();
        this.mMvpView = null;
        this.mSubscriptions = null;
        this.mDataCenter = null;
    }

    @Override
    public void unsubscribe(){
        if(mSubscriptions!=null){
            mSubscriptions.clear();
        }
    }

    /**
     * @description 当前的view（fragemnt&activity是否存在）
     * @author ydc
     * @createDate
     * @version 1.0
     */
    public boolean isViewAttached() {
        return mMvpView != null;
    }

    /**
     * @description 是否viewb绑定过P
     * @author ydc
     * @createDate
     * @version 1.0
     */
    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    /**
     * @description p&v没有绑定的异常
     * @author ydc
     * @createDate
     * @version 1.0
     */
    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before requesting data to the Presenter");
        }
    }

    /**
     * @description 统一添加订阅关联被观察者和观察者
     * Observable：被观察者
     * Subscriber：观察者
     * @author ydc
     * @createDate
     * @version 1.0
     */
    public void addSubscription(Observable observable, Observer observer) {
        if( observable!=null && observer!=null ){
            if (mSubscriptions == null) {
                mSubscriptions = new CompositeDisposable();
            }
            mSubscriptions.clear();
            mSubscriptions.add(observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe());
        }
    }
}
