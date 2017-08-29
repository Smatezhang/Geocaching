package com.zhuoxin.zhang.geocaching.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/8/29.
 */

public class NetClient {

    private static NetClient sNetClient;
    private Retrofit mRetrofit;

    private NetClient() {
        HttpLoggingInterceptor mHttpLoggingInterceptor = new HttpLoggingInterceptor();
        mHttpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(mHttpLoggingInterceptor)
                .build();
        Gson mGson = new GsonBuilder().setLenient().create();
        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://admin.syfeicuiedu.com")
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .build();

    }
    public static synchronized NetClient getInstance(){
        if (sNetClient == null){
            sNetClient = new NetClient();
        }
        return sNetClient;
    }

    public NetAPI getNetApi(){
        return mRetrofit.create(NetAPI.class);
    }


}
