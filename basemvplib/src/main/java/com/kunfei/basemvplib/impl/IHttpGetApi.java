package com.kunfei.basemvplib.impl;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by GKF on 2018/1/21.
 * get web content
 */

public interface IHttpGetApi {
    @GET
    Observable<Response<String>> get(@Url String url,
                                     @HeaderMap Map<String, String> headers);

    @GET
    Observable<Response<String>> getMap(@Url String url,
                                        @QueryMap(encoded = true) Map<String, String> queryMap,
                                        @HeaderMap Map<String, String> headers);


    /**
     * 下载文件
     *
     * @param fileUrl 文件地址
     * @return ResponseBody
     */
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

}
