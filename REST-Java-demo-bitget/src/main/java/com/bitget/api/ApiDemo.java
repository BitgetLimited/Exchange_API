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
import org.junit.Test;

public class ApiDemo {
    private static String encodingCharset = "UTF-8";

    private static String domainUrl = "https://api.bitget.com/api/v1";

    private static String accessKey = "ake662123c38d442c0";
    private static String secretKey = "53736212334285922bc4d4e7b260b9";


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
    }
    /**
     * 获取个人ID
     */
    @Test
    public void accounts(){
        TreeMap<String, Object> paramMap = new TreeMap();
        paramMap.put("method", "accounts");
        signUpTreeMap(paramMap,"get");
        String method = "/account/accounts?";
        String s = getTreeMap(method,paramMap);
        System.out.println(s);
    }

    /**
     * 获取个人资产
     */
    @Test
    public void balance() throws IOException{
        TreeMap<String, Object> paramMap = new TreeMap();
        paramMap.put("method", "balance");
        signUpTreeMap(paramMap,"get");

        String method = "/accounts/390350274889256960/balance?";
        String s = getTreeMap(method,paramMap);
        System.out.println(s);
    }

    /**
     * 委单
     * @throws IOException
     */
    @Test
    public void place() throws IOException {
        TreeMap<String, Object> paramMap = new TreeMap();
        paramMap.put("account_id","390350274889256960");
        paramMap.put("amount","1.5");
        paramMap.put("price","0.9");//402631028969091072
        paramMap.put("method","place");
        paramMap.put("symbol","btc_usdt");
        paramMap.put("type","buy-limit");

        String signs = signUpTreeMap(paramMap,"post");
        String method = "/order/orders/place";
        System.out.println(signs);
        String s = postTreeMap(method,paramMap,signs);
        System.out.println(s);
    }

    @Test
    public void submitcancel(){
        TreeMap<String, Object> paramMap = new TreeMap();
        paramMap.put("method","submitcancel");
        String signs = signUpTreeMap(paramMap,"post");
        String method = "/order/orders/406382037843357696/submitcancel";
        String s = postTreeMap(method,paramMap,signs);
        System.out.println(s);
    }

    private static String postTreeMap(String method,TreeMap<String, Object> paramMap,String signs){
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
            System.out.println("1");
            System.err.println(s);
            return s;
        }catch (IOException e) {
            return ApiReturnResult.error(ErrorCodeConstant.BAD_REQUEST, ErrorCodeConstant.INVALID_PARAMETER).toJsonString();
        }
    }

    private static String getTreeMap(String method,TreeMap<String, Object> paramMap){
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

    @Test
    public  void batchcancel(){
        List list = new ArrayList();
        list.add("404473009517146112");
        list.add("404473006388195328");
        list.add("404473005377368064");
        TreeMap<String, Object> paramMap = new TreeMap();
        paramMap.put("method","batchcancel");
        paramMap.put("order_ids", list);
        String signs = signUpTreeMap(paramMap,"post");
        String method = "/order/orders/batchcancel";
        String s = postTreeMap(method,paramMap,signs);
        System.out.println(s);

    }
    /**
     * 查询某个订单详情
     * return
     */
    @Test
    public void order() throws IOException{
        TreeMap<String, Object> paramMap = new TreeMap();
        paramMap.put("method","order");
        String signs = signUpTreeMap(paramMap,"post");
        String method = "/order/orders/406382037843357696";
        String s = postTreeMap(method,paramMap,signs);
        System.out.println(s);

    }
    /**
     * 查询某个订单的成交明细
     * return
     */
    @Test
    public void matchresults(){
        TreeMap<String, Object> paramMap = new TreeMap();
        paramMap.put("method","orderMatchresults");
        String signs = signUpTreeMap(paramMap,"post");
        String method = "/order/orders/406382037843357696/matchresults";
        String s = postTreeMap(method,paramMap,signs);
        System.out.println(s);
    }


    @Test
    public void matchresultsHistory(){
        TreeMap<String, Object> paramMap = new TreeMap();
        paramMap.put("method","matchresults");
        paramMap.put("symbol","iost_eth");
        paramMap.put("states","submitted");
        paramMap.put("types","buy-limit");
        paramMap.put("start_date","2018-06-01");
        paramMap.put("end_date","2018-07-23");

        paramMap.put("size","10");
        /*paramMap.put("from","402954178734895104");
        paramMap.put("direct","next");*/
        String signs = signUpTreeMap(paramMap,"post");
        String method = "/order/matchresults";
        String s = postTreeMap(method,paramMap,signs);

        System.out.println(s);
    }



    /**
     * 查询当前委托、历史委托
     * return
     */
    @Test
    public void orders(){
        TreeMap<String, Object> paramMap = new TreeMap();
        paramMap.put("method","orders");
        paramMap.put("types","buy-limit");
        paramMap.put("symbol","iost_eth");

        paramMap.put("start_date","2018-06-01");
        paramMap.put("end_date","2018-07-25");
        paramMap.put("states","submitted");
        paramMap.put("size","1000");
//        paramMap.put("from","403463925435248640");
//        paramMap.put("direct", "next");
        signUpTreeMap(paramMap,"get");
        String method = "/order/orders?";
        String s = getTreeMap(method,paramMap);
        System.out.println(s);
    }

    @Test
    public void withdrawCreate(){
        TreeMap<String, Object> paramMap = new TreeMap();
        paramMap.put("method","withdrawCreate");
        paramMap.put("address","1PaHiYCBFXuotKSSg7ZFGxB4n99CaDNYi");
        paramMap.put("amount","10");
        paramMap.put("currency","btc");
        String signs = signUpTreeMap(paramMap,"post");
        String method = "/dw/withdraw/api/create";
        String s = postTreeMap(method,paramMap,signs);
        System.out.println(s);
    }

    //191
    @Test
    public void withdrawCancel(){
        TreeMap<String, Object> paramMap = new TreeMap();
        paramMap.put("method", "withdrawCancel");
        String signs = signUpTreeMap(paramMap,"post");
        String method = "/dw/withdraw-virtual/344/cancel";
        String s = postTreeMap(method,paramMap,signs);
        System.out.println(s);
    }

    @Test
    public void withdrawSelect(){
        TreeMap<String, Object> paramMap = new TreeMap();
        paramMap.put("method","withdrawSelect");
        paramMap.put("currency","btc");

        paramMap.put("size","1");
        paramMap.put("type","withdraw");
        signUpTreeMap(paramMap,"get");
        String method = "/order/deposit_withdraw?";
        String s = getTreeMap(method,paramMap);
        System.out.println(s);
    }

    public static String serializeComplexSource(TreeMap<String, Object> params) {
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

    private static String signUpTreeMap(TreeMap paramMap,String methodReq) {
        try {
            //先进行数据的转换
            String params = signUpParamTree(paramMap);
            //通过sha对secretKey进行加密
            String secret = digest(secretKey);
            //在将要加密的数据和加密后的secretKey进行hmac加密
            String sign = sign(params, secret);
            //paramMap.put("sign", sign);
            long req_time = System.currentTimeMillis();
            //paramMap.put("req_time", req_time);
            if("get".equals(methodReq)){
                System.out.println("get");
                //paramMap.put("accesskey",accessKey);
                paramMap.put("sign", sign);
                paramMap.put("req_time", req_time);
                paramMap.put("accesskey",accessKey);
                System.out.println(params+"&sign="+sign+"&req_time="+req_time+"&accesskey="+accessKey);
                return "";
            }else{
                System.out.println("post");
                return "sign="+sign+"&req_time="+req_time+"&accesskey="+accessKey;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

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
