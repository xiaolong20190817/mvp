package com.kunfei.basemvplib.base;

import com.kunfei.basemvplib.help.EncodeConverter;
import com.kunfei.basemvplib.help.FunCommon;
import com.kunfei.basemvplib.help.SSLSocketClient;
import com.kunfei.basemvplib.impl.IHttpGetApi;
import com.kunfei.basemvplib.impl.IHttpPostApi;
import com.kunfei.basemvplib.progress.ProgressHelper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Dns;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class BaseModelImpl {
    private static OkHttpClient.Builder clientBuilder;
    public static Dns dns;
    public static BaseModelImpl getInstance() {
        return new BaseModelImpl(dns);
    }

    public BaseModelImpl(Dns dns){
        this.dns = dns;
    }

    public Retrofit getRetrofitString(String url) {
        return new Retrofit.Builder().baseUrl(url)
                //增加返回值为字符串的支持(以实体类返回)
                .addConverterFactory(EncodeConverter.create())
                //增加返回值为Observable<T>的支持
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getClientBuilder().build())
                .build();
    }

    public Retrofit getRetrofitString(String url, String encode) {
        try{
            return new Retrofit.Builder().baseUrl(url)
                    //增加返回值为字符串的支持(以实体类返回)
                    .addConverterFactory(EncodeConverter.create(encode))
                    //增加返回值为Observable<T>的支持
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getClientBuilder().build())
                    .build();
        }catch (Exception e){
            return  null;
        }

    }

    private static OkHttpClient.Builder getClientBuilder() {
        if (clientBuilder == null) {
            clientBuilder = new OkHttpClient.Builder().dns(dns)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.createTrustAllManager())
                    .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                    .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                    .addInterceptor(getHeaderInterceptor());
            clientBuilder = ProgressHelper.addProgress(clientBuilder);
        }
        return clientBuilder;
    }

    private static Interceptor getHeaderInterceptor() {
        return chain -> {
            Request request = chain.request()
                    .newBuilder()
                    .addHeader("Keep-Alive", "300")
                    .addHeader("Connection", "Keep-Alive")
                    .addHeader("Cache-Control", "no-cache")
                    .build();
            return chain.proceed(request);
        };
    }

    /*
    protected Observable<Response<String>> setCookie(Response<String> response, String tag) {
        return Observable.create(e -> {
            if (!response.raw().headers("Set-Cookie").isEmpty()) {
                StringBuilder cookieBuilder = new StringBuilder();
                for (String s : response.raw().headers("Set-Cookie")) {
                    String[] x = s.split(";");
                    for (String y : x) {
                        if (!TextUtils.isEmpty(y)) {
                            cookieBuilder.append(y).append(";");
                        }
                    }
                }
                String cookie = cookieBuilder.toString();
                if (!TextUtils.isEmpty(cookie)) {
                    DbHelper.getDaoSession().getCookieBeanDao().insertOrReplace(new CookieBean(tag, cookie));
                }
            }
            e.onNext(response);
            e.onComplete();
        });
    }
    */

    private class Web {
        private String content;
        private String js = "document.documentElement.outerHTML";

        Web(String content) {
            this.content = content;
        }
    }

    //get方式获取远程内容
    public interface GetHtmlCallBack{
        void onNext(String s);
        void onError(Throwable e);
        void onComplete();
    }

    public void gethtml(String url,GetHtmlCallBack getHtmlCallBack){
        gethtml(url, "UTF-8", getHtmlCallBack);
    }

    public void gethtml(String url,String encode,GetHtmlCallBack getHtmlCallBack) {

        try {
            getRetrofitString(FunCommon.getInstance().GetUrlHome(url), encode).create(IHttpGetApi.class)
                    .get(url.replaceAll(FunCommon.getInstance().GetUrlHome(url), ""), headerMap)
                    .flatMap(new Function<Response<String>, ObservableSource<String>>() {
                        @Override
                        public ObservableSource<String> apply(Response<String> stringResponse) throws Exception {
                            return Observable.create(emitter -> {
                                try {
                                    emitter.onNext(stringResponse.body());
                                    emitter.onComplete();
                                } catch (Exception e) {
                                    emitter.onError(e);
                                    emitter.onComplete();
                                }
                            });
                        }
                    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String s) {
                            getHtmlCallBack.onNext(s);
                        }

                        @Override
                        public void onError(Throwable e) {
                            getHtmlCallBack.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            getHtmlCallBack.onComplete();
                        }
                    });
        } catch (Exception e) {

        }
    }

    //post 获取远程内容
    public interface PostHtmlCallBack{
        void onNext(String s);
        void onError(Throwable e);
        void onComplete();
    }

    public void posthtml(String url,Map map,PostHtmlCallBack postHtmlCallBack){
        posthtml(url,map,"UTF-8",postHtmlCallBack);
    }

    public void posthtml(String url,Map map,String encode,PostHtmlCallBack postHtmlCallBack){

        getRetrofitString(FunCommon.getInstance().GetUrlHome(url),encode).create(IHttpPostApi.class)
                .postMap(url.replaceAll(FunCommon.getInstance().GetUrlHome(url),""),map,headerMap)
                .flatMap(new Function<Response<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Response<String> stringResponse) throws Exception {
                        return Observable.create(emitter -> {
                            try{
                                emitter.onNext(stringResponse.body());
                                emitter.onComplete();
                            }catch (Exception e){
                                emitter.onError(e);
                                emitter.onComplete();
                            }
                        });
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        postHtmlCallBack.onNext(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        postHtmlCallBack.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        postHtmlCallBack.onComplete();
                    }
                });
    }

    public static Map<String,String> headerMap = new HashMap<>(); //放请求头

}