package com.bitget.api;

import com.bitget.request.*;
import com.bitget.response.ApiReturnResult;
import okhttp3.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;

public class ApiDemo {
    private static String encodingCharset = "UTF-8";

    private static String domainUrl = "https://api.bitget.com/api/v1";

    private static String accessKey = "ake6612c38d442c0";
    private static String secretKey = "537362121222bc4d4e7b260b9";


    static final int CONN_TIMEOUT = 50;
    static final int READ_TIMEOUT = 50;
    static final int WRITE_TIMEOUT = 500;
    static final OkHttpClient client = createOkHttpClient();
    static OkHttpClient createOkHttpClient() {
        return new OkHttpClient.Builder().connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }
    public static void main(String[] args) throws Exception {
        withdrawSelect();
    }
    /**
     * 获取个人ID
     */

    public static void accounts(){
        HashMap<String, Object> paramMap = new LinkedHashMap();
        paramMap.put("accesskey", accessKey);
        signUp(paramMap,"get");
        String method = "/account/accounts?";
        String s = get(method,paramMap);
        System.out.println(s);
    }

    /**
     * 获取个人资产
     */

    public static void balance() throws IOException{
        HashMap<String, Object> paramMap = new LinkedHashMap();
        paramMap.put("accesskey", accessKey);
        signUp(paramMap,"get");

        String method = "/accounts/3902344889256960/balance?";
        String s = get(method,paramMap);
        System.out.println(s);
    }

    /**
     * 委单
     * @throws IOException
     */

    public static void place() throws IOException {
        LinkedHashMap<String, Object> paramMap = new LinkedHashMap();
        paramMap.put("accesskey",accessKey);
        paramMap.put("account_id","390323889256960");
        paramMap.put("amount","10");
        //paramMap.put("price","0.03");
        paramMap.put("type","buy-market");
        paramMap.put("symbol","iost_btc");
        paramMap.put("method","place");
        String signs = signUp(paramMap,"post");
        String method = "/order/orders/place";
        String s = post(method,paramMap,signs);
        System.out.println(s);
    }


    public static void submitcancel(){
        LinkedHashMap<String, Object> paramMap = new LinkedHashMap();
        paramMap.put("accesskey",accessKey);
        String signs = signUp(paramMap,"post");
        String method = "/order/orders/40258239144310784/submitcancel";
        String s = post(method,paramMap,signs);
        System.out.println(s);
    }

    private static String post(String method,LinkedHashMap<String, Object> paramMap,String signs){
        try{
            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            for (HashMap.Entry<String, Object> entry : paramMap.entrySet()) {
                formBodyBuilder.add(entry.getKey(), String.valueOf(entry.getValue()));
            }
            RequestBody body = formBodyBuilder.build();
            Request.Builder builder = new Request.Builder().url(domainUrl + method +"?"+signs).post(body);
            Request request = builder.build();
            Response response = client.newCall(request).execute();
            String s = response.body().string();
            return s;
        }catch (IOException e) {
            return ApiReturnResult.error(ErrorCodeConstant.BAD_REQUEST, ErrorCodeConstant.INVALID_PARAMETER).toJsonString();
        }
    }

    private static String get(String method,HashMap<String, Object> paramMap){
        try{
            Request.Builder builder = new Request.Builder().url(domainUrl + method + serializeComplexSource(paramMap)).get();
            Request request = builder.build();
            Response response = client.newCall(request).execute();
            String s = response.body().string();
            return s;
        }catch (IOException e) {
            return ApiReturnResult.error(ErrorCodeConstant.BAD_REQUEST, ErrorCodeConstant.INVALID_PARAMETER).toJsonString();
        }
    }

    public static void batchcancel(){
        List list = new ArrayList();
        list.add("4025823740775620608");
        list.add("401282232342110080");
        list.add("4012343349185280");
        LinkedHashMap<String, Object> paramMap = new LinkedHashMap();
        paramMap.put("accesskey",accessKey);
        paramMap.put("order_ids", list);
        String signs = signUp(paramMap,"post");
        String method = "/order/orders/batchcancel";
        String s = post(method,paramMap,signs);
        System.out.println(s);

    }
    /**
     * 查询某个订单详情
     * return
     */

    public static void order() throws IOException{
        LinkedHashMap<String, Object> paramMap = new LinkedHashMap();
        paramMap.put("accesskey",accessKey);
        String signs = signUp(paramMap,"post");
        String method = "/order/orders/4012223569185280";
        String s = post(method,paramMap,signs);
        System.out.println(s);

    }
    /**
     * 查询某个订单的成交明细
     * return
     */
     // TODO 不通
    public static void matchresults(){
        LinkedHashMap<String, Object> paramMap = new LinkedHashMap();
        paramMap.put("accesskey",accessKey);
        String signs = signUp(paramMap,"post");
        String method = "/order/orders/401212334032/matchresults";
        String s = post(method,paramMap,signs);
        System.out.println(s);
    }



    public static void matchresultsHistory(){
        LinkedHashMap<String, Object> paramMap = new LinkedHashMap();
        paramMap.put("method","matchresults");
        paramMap.put("accesskey",accessKey);
        paramMap.put("symbol","iost_usdt");
        paramMap.put("types","buy-market");
        paramMap.put("start_date","2018-06-01");
        paramMap.put("end_date","2018-07-12");
        paramMap.put("states","submitted");
        paramMap.put("size","50");
        paramMap.put("from","401234880256");
        paramMap.put("direct","next");
        String signs = signUp(paramMap,"post");
        String method = "/order/matchresults";
        String s = post(method,paramMap,signs);
        System.out.println(s);
    }



    /**
     * 查询当前委托、历史委托
     * return
     */

    public static void orders(){
        HashMap<String, Object> paramMap = new LinkedHashMap();
        paramMap.put("method","orders");
        paramMap.put("accesskey",accessKey);
        paramMap.put("symbol","iost_usdt");
        paramMap.put("types","buy-market");
        paramMap.put("start_date","2018-06-01");
        paramMap.put("end_date","2018-07-12");
        paramMap.put("states","submitted");
        paramMap.put("size","50");
        paramMap.put("from","40122342880256");
        paramMap.put("direct", "prev");
        signUp(paramMap,"get");
        String method = "/order/orders?";
            String s = get(method,paramMap);
            System.out.println(s);
    }


    public static void withdrawCreate(){
        LinkedHashMap<String, Object> paramMap = new LinkedHashMap();
        paramMap.put("method","withdrawCreate");
        paramMap.put("accesskey",accessKey);
        paramMap.put("address","1PaHiYCBFXuotKSSg7ZFGxB4n99CaDNYi");
        paramMap.put("amount","10");
        paramMap.put("currency","btc");
        paramMap.put("fees","0.1");
        String signs = signUp(paramMap,"post");
        String method = "/dw/withdraw/api/create";
        String s = post(method,paramMap,signs);
        System.out.println(s);
    }

    //191
    public static void withdrawCancel(){
        LinkedHashMap<String, Object> paramMap = new LinkedHashMap();
        paramMap.put("method", "withdrawCancel");
        paramMap.put("accesskey", accessKey);
        String signs = signUp(paramMap,"post");
        String method = "/dw/withdraw-virtual/22226/cancel";
        String s = post(method,paramMap,signs);
        System.out.println(s);
    }


    public static void withdrawSelect(){
        HashMap<String, Object> paramMap = new LinkedHashMap();
        paramMap.put("method","withdrawSelect");
        paramMap.put("accesskey",accessKey);
        paramMap.put("currency","btc");
        paramMap.put("type","withdraw");
        paramMap.put("size","10");
        signUp(paramMap,"get");
        String method = "/order/deposit_withdraw?";
            String s = get(method,paramMap);
            System.out.println(s);
    }






    public static String serializeComplexSource(HashMap<String, Object> params) {
        StringBuffer rc = new StringBuffer(2048);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            Object value = entry.getValue();
            if (!StringUtils.isEmpty(value.toString())) {
                if (value instanceof List) {
                    List<String> col = (List<String>) value;
                    Collections.sort(col);
                    for (String s : col) {
                        rc.append("&").append(entry.getKey().trim()).append("=").append(s);
                    }
                }
                else {
                    rc.append("&").append(entry.getKey().trim()).append("=").append(value);
                }
            }
        }

        if (rc.length() > 0) {
            rc.deleteCharAt(0);
        }

        return rc.toString();
    }

    private static String signUp(HashMap paramMap,String methodReq) {
        try {
            //先进行数据的转换
            String params = signUpParam(paramMap);
            //通过sha对secretKey进行加密
            String secret = digest(secretKey);
            //在将要加密的数据和加密后的secretKey进行hmac加密
            String sign = sign(params, secret);
            //paramMap.put("sign", sign);
            long req_time = System.currentTimeMillis();
            //paramMap.put("req_time", req_time);
            if("get".equals(methodReq)){
                //paramMap.put("accesskey",accessKey);
                paramMap.put("sign", sign);
                paramMap.put("req_time", req_time);
                System.out.println(params+"&sign="+sign+"&req_time="+req_time);
                return "";
            }else{
                return "sign="+sign+"&req_time="+req_time;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public static String signUpParam(HashMap<String, Object> paramMap) {
        StringBuilder sb = new StringBuilder();
        for (String key : paramMap.keySet()) {
            sb.append(key).append("=").append(paramMap.get(key)).append("&");
        }
        return sb.substring(0, sb.length() - 1);
    }
    public static String signUpParamTree(TreeMap<String, Object> paramMap) {
        StringBuilder sb = new StringBuilder();
        for (String key : paramMap.keySet()) {
            sb.append(key).append("=").append(paramMap.get(key)).append("&");
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * SHA加密
     */
    private static String digest(String valueStr) {
        valueStr = valueStr.trim();
        byte value[];
        try {
            value = valueStr.getBytes(encodingCharset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return toHex(md.digest(value));

    }

    private static String toHex(byte input[]) {
        if (input == null)
            return null;
        StringBuffer output = new StringBuffer(input.length * 2);
        for (int i = 0; i < input.length; i++) {
            int current = input[i] & 0xff;
            if (current < 16)
                output.append("0");
            output.append(Integer.toString(current, 16));
        }

        return output.toString();
    }

    /**
     * 生成签名消息
     *
     * @param valueStr 要签名的字符串
     * @param keyStr   签名密钥
     * @return
     */
    public static String sign(String valueStr, String keyStr) {
        byte keyInput[] = new byte[64];
        byte keyOutput[] = new byte[64];
        byte key[];
        byte value[];
        try {
            key = keyStr.getBytes(encodingCharset);
            value = valueStr.getBytes(encodingCharset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        Arrays.fill(keyInput, key.length, 64, (byte) 54);
        Arrays.fill(keyOutput, key.length, 64, (byte) 92);
        for (int i = 0; i < key.length; i++) {
            keyInput[i] = (byte) (key[i] ^ 0x36);
            keyOutput[i] = (byte) (key[i] ^ 0x5c);
        }

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.update(keyInput);
        md.update(value);
        byte dg[] = md.digest();
        md.reset();
        md.update(keyOutput);
        md.update(dg, 0, 16);
        dg = md.digest();
        return toHex(dg);
    }
}
