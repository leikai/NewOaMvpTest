package com.bs.lk.newoamvptest.Interface;

import com.bs.lk.newoamvptest.bean.SessionBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * 网络接口
 * @author lk
 */
public interface IOAInterfaceV3Service {
    /**
     * eg:
     * @GET
     * Observable<NewsRequestModel> getNewList(@Url String url,
     * @QueryMap Map<String, String> params);
     *
     */
    @POST
    Observable<SessionBean>getServiceAndroidversion(@Url String url,
                                                    @QueryMap Map<String,String> params);

}
