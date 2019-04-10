package com.bs.lk.newoamvptest.base.mvp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
//import rx.Subscription;
//import rx.exceptions.Exceptions;

/**
 * @Description Subscription集合，方便后期引入darger
 * @author lk
 */
public final class CompositeDisposable implements Disposable {
    private Set<Disposable> disposables;
    private volatile boolean unsubscribed;

    public CompositeDisposable() {
    }

    public CompositeDisposable(final Disposable... disposables) {
        this.disposables = new HashSet<Disposable>(Arrays.asList(disposables));
    }

//    /**
//     * @description 取消本身和所有内部订阅
//     * @author Andy.fang
//     * @createDate
//     * @version 1.0
//     */
//    @Override
//    public void unsubscribe() {
//        if (!unsubscribed) {
//            Collection<Disposable> unsubscribe = null;
//            synchronized (this) {
//                if (unsubscribed) {
//                    return;
//                }
//                unsubscribed = true;
//                unsubscribe = disposables;
//                disposables = null;
//            }
//            unsubscribeFromAll(unsubscribe);//只执行一次
//        }
//    }
//
//    @Override
//    public boolean isUnsubscribed() {
//        return unsubscribed;
//    }

    /**
     * @description 单个订阅加入集合
     * @author Andy.fang
     * @createDate
     * @version 1.0
     */
    public void add(Disposable d) {
        if (d.isDisposed()) {
            return;
        }
        if (!unsubscribed) {
            synchronized (this) {
                if (!unsubscribed) {
                    if (disposables == null) {
                        disposables = new HashSet<Disposable>(4);
                    }
                    disposables.add(d);
                    return;
                }
            }
        }
        d.dispose();
    }

    /**
     * @description 把订阅添加到集合
     * @author Andy.fang
     * @createDate
     * @version 1.0
     */
    public void addAll(final Disposable... disposables) {
        if (!unsubscribed) {
            synchronized (this) {
                if (!unsubscribed) {
                    if (this.disposables == null) {
                        this.disposables = new HashSet<Disposable>(disposables.length);
                    }

                    for (Disposable s : disposables) {
                        if (!s.isDisposed()) {
                            this.disposables.add(s);
                        }
                    }
                    return;
                }
            }
        }

        for (Disposable s : disposables) {
            s.dispose();
        }
    }

    /**
     * @description 删除一个订阅
     * @author Andy.fang
     * @createDate
     * @version 1.0
     */
    public void remove(final Disposable d) {
        if (!unsubscribed) {
            boolean unsubscribe = false;
            synchronized (this) {
                if (unsubscribed || disposables == null) {
                    return;
                }
                unsubscribe = disposables.remove(d);
            }
            if (unsubscribe) {
                d.dispose();
            }
        }
    }

    /**
     * @description 取消所有订阅
     * @author Andy.fang
     * @createDate
     * @version 1.0
     */
    public void clear() {
        if (!unsubscribed) {
            Collection<Disposable> unsubscribe = null;
            synchronized (this) {
                if (unsubscribed || disposables == null) {
                    return;
                } else {
                    unsubscribe = disposables;
                    disposables = null;
                }
            }
            unsubscribeFromAll(unsubscribe);
        }
    }


    /**
     * @description
     * @author Andy.fang
     * @createDate
     * @version 1.0
     */
    private static void unsubscribeFromAll(Collection<Disposable> subscriptions) {
        if (subscriptions == null) {
            return;
        }
        for (Disposable s : subscriptions) {
            try {
                s.dispose();
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
            }
        }
    }

    /**
     * @description 返回true,如果这个组合不是unsubscribed和subscriptions
     * @author Andy.fang
     * @createDate
     * @version 1.0
     */
    public boolean hasSubscriptions() {
        if (!unsubscribed) {
            synchronized (this) {
                return !unsubscribed && disposables != null && !disposables.isEmpty();
            }
        }
        return false;
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean isDisposed() {
        return false;
    }
}
