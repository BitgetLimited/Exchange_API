package com.bitget.api;

import com.bitget.request.ErrorCodeConstant;
import com.bitget.response.ApiReturnResult;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class DataDemo {
    static final int CONN_TIMEOUT = 50;
    static final int READ_TIMEOUT = 50;
    static final int WRITE_TIMEOUT = 500;
    static final OkHttpClient client = createOkHttpClient();
    static OkHttpClient createOkHttpClient() {
        return new OkHttpClient.Builder().connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }
    private static String domainUrl = "https://api.bitget.com";

    public static void main(String[] args){
        kline();
    }
    
    public static void kline(){
        String method = "/data/v1/market/history/kline?symbol=btc_usdt&period=1min&size=2";
        String s = get(method);
        System.out.println(s);
    }

    
    public static void ticker(){
        String method = "/data/v1/ticker?symbol=btc_usdt";
        String s = get(method);
        System.out.println(s);
    }

    
    public static void merged(){
        String method = "/data/v1/market/detail/merged?symbol=btc_usdt";
        String s = get(method);
        System.out.println(s);
    }

    
    public static void tickers(){
        String method = "/data/v1/market/tickers";
        String s = get(method);
        System.out.println(s);
    }

    
    public static void depth(){
        String method = "/data/v1/market/depth?symbol=btc_usdt&type=step0";
        String s = get(method);
        System.out.println(s);
    }

    
    public static void trade(){
        String method = "/data/v1/market/trade?symbol=btc_usdt";
        String s = get(method);
        System.out.println(s);
    }

    
    public static void trades(){
        String method = "/data/v1/market/history/trade?symbol=btc_usdt&size=10";
        String s = get(method);
        System.out.println(s);
    }

    
    public static void detail(){
        String method = "/data/v1/market/detail?symbol=btc_usdt";
        String s = get(method);
        System.out.println(s);
    }

    
    public static void symbols(){
        String method = "/data/v1/common/symbols";
        String s = get(method);
        System.out.println(s);
    }

    
    public static void currencys(){
        String method = "/data/v1/common/currencys";
        String s = get(method);
        System.out.println(s);
    }

    
    public static void timestamp(){
        String method = "/data/v1/common/timestamp";
        String s = get(method);
        System.out.println(s);
    }

    private static String get(String method){
        try{
            Request.Builder builder = new Request.Builder().url(domainUrl + method ).get();
            Request request = builder.build();
            Response response = client.newCall(request).execute();
            String s = response.body().string();
            return s;
        }catch (IOException e) {
            return ApiReturnResult.error(ErrorCodeConstant.BAD_REQUEST, ErrorCodeConstant.INVALID_PARAMETER).toJsonString();
        }
    }
}
