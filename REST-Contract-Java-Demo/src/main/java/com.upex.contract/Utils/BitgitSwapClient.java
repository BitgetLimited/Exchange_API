package com.upex.contract.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.upex.contract.dto.PpBatchOrder;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * bitget合约Http客户端
 */
public class BitgitSwapClient {

    private String accessKey;
    private String secretKey;
    private String baseUrl;
    private String accountId = null;
    private static final int CONN_TIMEOUT = 50;
    private static final int READ_TIMEOUT = 50;
    private static final int WRITE_TIMEOUT = 500;
    private static final OkHttpClient client = createOkHttpClient();
    private final Logger logger = LoggerFactory.getLogger(BitgitSwapClient.class);

    static OkHttpClient createOkHttpClient() {
        return new OkHttpClient.Builder().connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    public BitgitSwapClient (String accessKey, String secretKey, String baseUrl, double permitsPerSecond) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.baseUrl = baseUrl;
    }

    public String getAccessKey() {
        return accessKey;
    }

    /**
     * 所有合约持仓信息
     * @return
     */
    public String getAllProductPosition(){
        String method = null, res = null;
        TreeMap<String, Object> paramMap = new TreeMap<String, Object>();
        try {
            paramMap.put("method", "allPosition");
            signUpTreeMap(paramMap, "get");
            method = "/api/swap/v1/allPosition?";
            res = getTreeMap(method, paramMap);

            JSONObject jsonObject = JSON.parseObject(res);
            String status = jsonObject.getString("status");
            if (!"ok".equalsIgnoreCase(status)) {
                logger.warn("bitgitSwap getPosition error accountId={}, accessKey={}, method={}, res={}",
                        accountId, accessKey, method, res);
                return null;
            }
            String resData = jsonObject.getString("data");
            return resData;
        }catch (Exception e){
            logger.error("bitgitSwap getPosition error accountId={}, accessKey={}, paramMap={}, method={}, res={}, e={}",
                    accountId, accessKey, paramMap.toString(), method, res, e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取单个合约的仓位
     * @param instrument_id
     * @return
     */
    public String getPosition (String instrument_id) {
        String method = null, res = null;
        TreeMap<String, Object> paramMap = new TreeMap<>();
        try {
            if (StringUtils.isEmpty(instrument_id)) {
                throw new RuntimeException("symbol 是必传参数");
            }
            paramMap.put("method", "position");
            signUpTreeMap(paramMap, "get");
            method = "/api/swap/v1/"+ instrument_id +"/position?";
            res = getTreeMap(method, paramMap);

            JSONObject jsonObject = JSON.parseObject(res);
            String status = jsonObject.getString("status");
            if (!"ok".equalsIgnoreCase(status)) {
                logger.warn("bitgitSwap getPosition error accountId={}, accessKey={}, method={}, res={}",
                        accountId, accessKey, method, res);
                return null;
            }
            String resData = jsonObject.getString("data");
            return resData;
        }catch (Exception e){
            logger.error("bitgitSwap getPosition error accountId={}, accessKey={}, paramMap={}, method={}, res={}, e={}",
                    accountId, accessKey, paramMap.toString(), method, res, e.getMessage(), e);
        }
        return null;
    }

    /**
     * 所有币种合约账户信息
     *
     * @return
     */
    public String getAllAccount() {
        String method = null, res = null;
        TreeMap<String, Object> paramMap = new TreeMap<>();
        try {
            paramMap.put("method", "accounts");
            signUpTreeMap(paramMap, "get");
            method = "/api/swap/v1/accounts?";
            res = getTreeMap(method, paramMap);

            JSONObject jsonObject = JSON.parseObject(res);
            String status = jsonObject.getString("status");
            if (!"ok".equalsIgnoreCase(status)) {
                logger.warn("bitgitSwap getPosition error accountId={}, accessKey={}, method={}, res={}",
                        accountId, accessKey, method, res);
                return null;
            }
            String resData = jsonObject.getString("data");
            return resData;
        }catch (Exception e){
            logger.error("bitgitSwap getPosition error accountId={}, accessKey={}, paramMap={}, method={}, res={}, e={}",
                    accountId, accessKey, paramMap.toString(), method, res, e.getMessage(), e);
        }
        return null;
    }
    /**
     * 所有币种合约账户信息
     *
     * @return
     */
    public String getAccount(String productCode) {
        String method = null, res = null;
        TreeMap<String, Object> paramMap = new TreeMap<>();
        try {
            paramMap.put("method", "accounts");
            signUpTreeMap(paramMap, "get");
            method = "/api/swap/v1/"+productCode+"/accounts?";
            res = getTreeMap(method, paramMap);

            JSONObject jsonObject = JSON.parseObject(res);
            String status = jsonObject.getString("status");
            if (!"ok".equalsIgnoreCase(status)) {
                logger.warn("bitgitSwap getPosition error accountId={}, accessKey={}, method={}, res={}",
                        accountId, accessKey, method, res);
                return null;
            }
            String resData = jsonObject.getString("data");
            return resData;
        }catch (Exception e){
            logger.error("bitgitSwap getPosition error accountId={}, accessKey={}, paramMap={}, method={}, res={}, e={}",
                    accountId, accessKey, paramMap.toString(), method, res, e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取某个合约的用户配置
     * @param productCode
     * @return
     */
    public String getSingleSettings(String productCode) {
        String method = null, res = null;
        TreeMap<String, Object> paramMap = new TreeMap<>();
        try {
            paramMap.put("method", "settings");
            signUpTreeMap(paramMap, "get");
            method = "/api/swap/v1/"+productCode+"/settings?";
            res = getTreeMap(method, paramMap);

            JSONObject jsonObject = JSON.parseObject(res);
            String status = jsonObject.getString("status");
            if (!"ok".equalsIgnoreCase(status)) {
                logger.warn("bitgitSwap getPosition error accountId={}, accessKey={}, method={}, res={}",
                        accountId, accessKey, method, res);
                return null;
            }
            String resData = jsonObject.getString("data");
            return resData;
        }catch (Exception e){
            logger.error("bitgitSwap getPosition error accountId={}, accessKey={}, paramMap={}, method={}, res={}, e={}",
                    accountId, accessKey, paramMap.toString(), method, res, e.getMessage(), e);
        }
        return null;
    }
    /**
     * 设定某个合约的杠杆
     * @param productCode
     * @return
     */
    public String settingLeverage(String productCode,Integer leverage, Integer side) {
        String method = null, res = null;
        TreeMap<String, Object> paramMap = new TreeMap<>();
        try {
            paramMap.put("method", "leverage");
            paramMap.put("leverage",leverage);
            paramMap.put("side",side);
            signUpTreeMap(paramMap, "get");
            method = "/api/swap/v1/"+productCode+"/leverage?";
            res = getTreeMap(method, paramMap);

            JSONObject jsonObject = JSON.parseObject(res);
            String status = jsonObject.getString("status");
            if (!"ok".equalsIgnoreCase(status)) {
                logger.warn("bitgitSwap getPosition error accountId={}, accessKey={}, method={}, res={}",
                        accountId, accessKey, method, res);
                return null;
            }
            String resData = jsonObject.getString("data");
            return resData;
        }catch (Exception e){
            logger.error("bitgitSwap getPosition error accountId={}, accessKey={}, paramMap={}, method={}, res={}, e={}",
                    accountId, accessKey, paramMap.toString(), method, res, e.getMessage(), e);
        }
        return null;
    }
    /**
     * 查询主账户流水
     * @param productCode
     * @return
     */
    public String getLedger(String productCode,Integer from, Integer to,Integer limit) {
        String method = null, res = null;
        TreeMap<String, Object> paramMap = new TreeMap<>();
        try {
            paramMap.put("method", "ledger");
            paramMap.put("from",from);
            paramMap.put("to",to);
            paramMap.put("limit",limit);

            signUpTreeMap(paramMap, "get");
            method = "/api/swap/v1/"+productCode+"/ledger?";
            res = getTreeMap(method, paramMap);

            JSONObject jsonObject = JSON.parseObject(res);
            String status = jsonObject.getString("status");
            if (!"ok".equalsIgnoreCase(status)) {
                logger.warn("bitgitSwap getPosition error accountId={}, accessKey={}, method={}, res={}",
                        accountId, accessKey, method, res);
                return null;
            }
            String resData = jsonObject.getString("data");
            return resData;
        }catch (Exception e){
            logger.error("bitgitSwap getPosition error accountId={}, accessKey={}, paramMap={}, method={}, res={}, e={}",
                    accountId, accessKey, paramMap.toString(), method, res, e.getMessage(), e);
        }
        return null;
    }
    /**
     * 查询保障金账户流水
     * @param productCode
     * @return
     */
    public String getLedgerMargin(String productCode,Integer from, Integer to,Integer limit) {
        String method = null, res = null;
        TreeMap<String, Object> paramMap = new TreeMap<>();
        try {
            paramMap.put("method", "ledgerMargin");
            if(from != null){
                paramMap.put("from", from);
            }
            if(to != null){
                paramMap.put("to",to);
            }
            if(limit != null){
                paramMap.put("limit",limit);
            }

            signUpTreeMap(paramMap, "get");
            method = "/api/swap/v1/"+productCode+"/ledgerMargin?";
            res = getTreeMap(method, paramMap);
            return res;

        }catch (Exception e){
            logger.error("bitgitSwap getPosition error accountId={}, accessKey={}, paramMap={}, method={}, res={}, e={}",
                    accountId, accessKey, paramMap.toString(), method, res, e.getMessage(), e);
        }
        return null;
    }
    /**
     * 查询保障金账户流水
     * @param productCode
     * @return
     */
    public String getFills(String productCode,String orderId) {
        String method = null, res = null;
        TreeMap<String, Object> paramMap = new TreeMap<>();
        try {
            paramMap.put("method", "fills");
            paramMap.put("order_id",orderId);
            paramMap.put("instrument_id",productCode);

            signUpTreeMap(paramMap, "get");
            method = "/api/swap/v1/fills?";
            res = getTreeMap(method, paramMap);
            return res;
        }catch (Exception e){
            logger.error("bitgitSwap getPosition error accountId={}, accessKey={}, paramMap={}, method={}, res={}, e={}",
                    accountId, accessKey, paramMap.toString(), method, res, e.getMessage(), e);
        }
        return null;
    }
    /**
     * 获取合约挂单冻结数量
     * @param productCode
     * @return
     */
    public String getHolds(String productCode) {
        String method = null, res = null;
        TreeMap<String, Object> paramMap = new TreeMap<>();
        try {
            paramMap.put("method", "holds");
            signUpTreeMap(paramMap, "get");
            method = "/api/swap/v1/"+productCode+"/holds?";
            res = getTreeMap(method, paramMap);
            return res;
        }catch (Exception e){
            logger.error("bitgitSwap getPosition error accountId={}, accessKey={}, paramMap={}, method={}, res={}, e={}",
                    accountId, accessKey, paramMap.toString(), method, res, e.getMessage(), e);
        }
        return null;
    }
    public String getOrder (String order_id, String instrument_id, String client_oid) {
        String resData="";
        String method = null, res = null;
        TreeMap<String, Object> paramMap = new TreeMap<>();
        try {
            if (StringUtils.isEmpty(instrument_id)) {
                throw new RuntimeException("symbol 是必传参数");
            }
            if(StringUtils.isEmpty(order_id) && StringUtils.isEmpty(client_oid)){
                throw new RuntimeException("order_id, client_oid必传一个参数");
            }

            String tempId = !StringUtils.isEmpty(order_id) ? order_id : client_oid;
            paramMap.put("method", "orders");
            signUpTreeMap(paramMap, "get");
            method = "/api/swap/v1/orders/"+ instrument_id +"/"+ tempId+"?";
            res = getTreeMap(method, paramMap);

            JSONObject jsonObject = JSON.parseObject(res);
            String status = jsonObject.getString("status");
            if (!"ok".equalsIgnoreCase(status)) {
                logger.warn("bitgitSwap getOrder error accountId={}, accessKey={}, method={}, res={}",
                        accountId, accessKey, method, res);
                return null;
            }
              resData = jsonObject.getString("data");
        }catch (Exception e){
            logger.error("bitgitSwap getOrder error accountId={}, accessKey={}, paramMap={}, method={}, res={}, e={}",
                    accountId, accessKey, paramMap.toString(), method, res, e.getMessage(), e);
        }
        return resData;
    }

    /**
     * 获取订单列表
     * @param instrument_id
     * @param status
     * @param from
     * @param to
     * @param limit
     * @return
     */
    public String getOrders (String instrument_id, String status, String from, String to, String limit) {
        String method = null, res = null;
        TreeMap<String, Object> paramMap = new TreeMap<>();
        try {
            if (StringUtils.isEmpty(instrument_id)) {
                throw new RuntimeException("symbol 是必传参数");
            }
            paramMap.put("method", "orders");
            paramMap.put("status", status);
            if(from != null){
                paramMap.put("from", from);
            }
            if(to != null){
                paramMap.put("to", to);
            }
            if(limit != null){
                paramMap.put("limit", limit);
            }
            signUpTreeMap(paramMap, "get");
            method = "/api/swap/v1/orders/"+ instrument_id+"?";
            res = getTreeMap(method, paramMap);
            return res;

        }catch (Exception e){
            logger.error("bitgitSwap getOrders error accountId={}, accessKey={}, paramMap={}, method={}, res={}, e={}",
                    accountId, accessKey, paramMap.toString(), method, res, e.getMessage(), e);
        }
        return null;
    }
    /**
     * 获取订单信息
     * @param instrument_id
     * @return
     */
    public String getOrderDetail (String instrument_id, String orderId) {
        String method = null, res = null;
        TreeMap<String, Object> paramMap = new TreeMap<>();
        try {
            if (StringUtils.isEmpty(instrument_id)) {
                throw new RuntimeException("instrument_id 是必传参数");
            }
            if (StringUtils.isEmpty(orderId)) {
                throw new RuntimeException("orderId 是必传参数");
            }
            paramMap.put("method", "getOrderDetail");
            signUpTreeMap(paramMap, "get");
            method = "/api/swap/v1/orders/"+ instrument_id+"/"+orderId+"?";
            res = getTreeMap(method, paramMap);
            return res;
        }catch (Exception e){
            logger.error("bitgitSwap getOrders error accountId={}, accessKey={}, paramMap={}, method={}, res={}, e={}",
                    accountId, accessKey, paramMap.toString(), method, res, e.getMessage(), e);
        }
        return null;
    }
    /**
     * 下单
     * @param client_oid
     * @param instrument_id
     * @param price
     * @param size
     * @param type
     * @param order_type
     * @param match_price
     * @return
     */
    public String addOrder (String client_oid, String instrument_id, String price, String size,
                                String type, String order_type, String match_price) {
        String method = null, res = null;
        TreeMap<String, Object> paramMap = new TreeMap<>();
        try {
            paramMap.put("method", "order");
            paramMap.put("size", size);
            paramMap.put("type", type);
            paramMap.put("price", price);
            paramMap.put("instrument_id", instrument_id);
            if(client_oid != null){
                paramMap.put("client_oid", client_oid);
            }
            if(order_type != null){
                paramMap.put("order_type", order_type);
            }
            if(match_price != null){
                paramMap.put("match_price", match_price);
            }
            String signs = signUpTreeMap(paramMap, "post");
            method = "/api/swap/v1/order";
            res = postTreeMap(method, paramMap, signs);

            return res;
        }catch (Exception e){
            logger.error("bitgitSwap create order error accountId={}, accessKey={}, paramMap={}, method={}, res={}, e={}",
                    accountId, accessKey, paramMap.toString(), method, res, e.getMessage(), e);
        }
        return null;
    }

    public String addOrderList(String instrument_id, List<PpBatchOrder> orderList, String order_type){
        String method = null, res = null;
        TreeMap<String, Object> paramMap = new TreeMap<>();
        try {
            paramMap.put("method", "orders");
            paramMap.put("order_data", JSONArray.toJSONString(orderList));
            paramMap.put("instrument_id", instrument_id);
            if(order_type != null){
                paramMap.put("order_type", order_type);
            }
            String signs = signUpTreeMap(paramMap, "post");
            method = "/api/swap/v1/orders";
            res = postTreeMap(method, paramMap, signs);

            JSONObject jsonObject = JSONObject.parseObject(res);
            String resData = jsonObject.getString("data");
            if (resData == null || resData.isEmpty()) {
                logger.warn("bitgitSwap addOrderList fail accountId={}, accessKey={}, paramMap={}, method={}, res={}",
                        accountId, accessKey, paramMap.toString(), method, res);
                return null;
            }
            logger.info("bitgitSwap addOrderList accountId={}, accessKey={}, paramMap={}, method={}, res={}",
                    accountId, accessKey, paramMap.toString(), method, res);
            return resData;
        }catch (Exception e){
            logger.error("bitgitSwap addOrderList error accountId={}, accessKey={}, paramMap={}, method={}, res={}, e={}",
                    accountId, accessKey, paramMap.toString(), method, res, e.getMessage(), e);
        }
        return null;
    }
    
    public String cancelOrder (String instrument_id, String order_id, String client_oid) {
        String method = null, res = null;
        TreeMap<String, Object> paramMap = new TreeMap<>();
        try {
            if (StringUtils.isEmpty(instrument_id)) {
                throw new RuntimeException("symbol 是必传参数");
            }
            if(StringUtils.isEmpty(order_id) && StringUtils.isEmpty(client_oid)){
                throw new RuntimeException("order_id, client_oid必传一个参数");
            }
            String tempId = StringUtils.isNotEmpty(order_id) ? order_id : client_oid;
            paramMap.put("method", "cancel_order");
            String signs = signUpTreeMap(paramMap, "post");
            method = "/api/swap/v1/cancel_order/"+ instrument_id +"/"+ tempId;
            res = postTreeMap(method, paramMap, signs);

            JSONObject jsonObject = JSON.parseObject(res);
            String statusRs = jsonObject.getString("status");

            return res;
        }catch (Exception e){
            logger.error("bitgitSwap cancelOrder error accountId={}, accessKey={}, paramMap={}, method={}, res={}, e={}",
                    accountId, accessKey, paramMap.toString(), method, res, e.getMessage(), e);
        }
        return null;
    }

    public String  cancelOrderList (String instrument_id, List<String> ids, List<String> client_oids) {
        String method = null, res = null;
        TreeMap<String, Object> paramMap = new TreeMap<>();
        try {
            if (StringUtils.isEmpty(instrument_id)) {
                throw new RuntimeException("symbol 是必传参数");
            }
            if(ids == null && client_oids == null){
                throw new RuntimeException("ids, client_oids必传一个参数");
            }
            paramMap.put("method", "cancel_batch_orders");
            paramMap.put("ids", joinId(ids != null ? ids : client_oids));
            String signs = signUpTreeMap(paramMap, "post");
            method = "/api/swap/v1/cancel_batch_orders/"+ instrument_id;
            res = postTreeMap(method, paramMap, signs);

            JSONObject jsonObject = JSON.parseObject(res);
            String statusRs = jsonObject.getString("status");
            if(!"ok".equalsIgnoreCase(statusRs)){
                logger.warn("bitgitSwap cancelOrderList fail accountId={}, accessKey={}, paramMap={}, method={}, res={}",
                        accountId, accessKey, paramMap.toString(), method, res);
                return null;
            }
            String resData = jsonObject.getString("data");
            return resData;
        }catch (Exception e){
            logger.error("bitgitSwap cancelOrderList error accountId={}, accessKey={}, paramMap={}, method={}, res={}, e={}",
                    accountId, accessKey, paramMap.toString(), method, res, e.getMessage(), e);
        }
        return null;
    }



    public String getDepthVO(String instrument_id, Integer size, Boolean exclude) {
        String method = null, res = null;
        try {
            if (StringUtils.isEmpty(instrument_id)) {
                throw new RuntimeException("symbol 是必传参数");
            }
            if(!exclude){////请求盘口列表-完整数据
                method = "/api/swap/v1/instruments/"+ instrument_id +"/depth?size="+ size;
            }else {//请求盘口列表-排除maker做市
                method = "/api/swap/v1/instruments/"+ instrument_id +"/depthData?size="+ size;
            }
            res = get(method);
            JSONObject jsonObject = JSONObject.parseObject(res);
            String status = jsonObject.getString("status");
            if (!"ok".equalsIgnoreCase(status)) {
                logger.warn("bitgitSwap getBook error accountId={}, accessKey={}, method={}, res={}",
                        accountId, accessKey, method, res);
                return null;
            }
            String resData = jsonObject.getString("data");
            return resData;
        }catch (Exception e){
            logger.error("bitgitSwap getBook error accountId={}, accessKey={}, method={}, res={}, e={}",
                    accountId, accessKey, method, res, e.getMessage(), e);
        }
        return null;
    }

    public List<Object[]> getKLine (String instrument_id, String start, String end, Integer granularity) {
        String method = null, res = null;
        StringBuilder sb = new StringBuilder();
        try {
            if (StringUtils.isEmpty(instrument_id)) {
                throw new RuntimeException("symbol 是必传参数");
            }
            if (start != null) {
                sb.append("start=").append(start);
            }
            if (end != null) {
                if (sb.length() > 0) sb.append("&");
                sb.append("end=").append(end);
            }
            if (granularity != null) {
                if (sb.length() > 0) sb.append("&");
                sb.append("granularity=").append(granularity);
            }
            method = "/api/swap/v1/instruments/" + instrument_id + "/candles?" + sb.toString();
            res = get(method);
            JSONObject jsonObject = JSON.parseObject(res);
            String status = jsonObject.getString("status");
            if (!"ok".equalsIgnoreCase(status)) {
                logger.warn("bitgitSwap getKLine error accountId={}, accessKey={}, method={}, res={}",
                        accountId, accessKey, method, res);
                return null;
            }
            String resData = jsonObject.getString("data");
            JSONObject dataJson = JSONObject.parseObject(resData);
            return JSONArray.parseArray(dataJson.getString("data"), Object[].class);
        } catch (Exception e) {
            logger.error("bitgitSwap getKLine error accountId={}, accessKey={}, method={}, res={}, e={}",
                    accountId, accessKey, method, res, e.getMessage(), e);
        }
        return null;
    }


    public String getMarkPrice (String instrument_id) {
        String method = null, res = null;
        try {
            if (StringUtils.isEmpty(instrument_id)) {
                throw new RuntimeException("symbol 是必传参数");
            }
            method = "/api/swap/v1/instruments/"+instrument_id+"/mark_price";
            res = get(method);
            JSONObject jsonObject = JSON.parseObject(res);
            String status = jsonObject.getString("status");
            if (!"ok".equalsIgnoreCase(status)) {
                logger.warn("bitgitSwap getMarkPrice error accountId={}, accessKey={}, method={}, res={}",
                        accountId, accessKey, method, res);
                return null;
            }
            String resData = jsonObject.getString("data");
            return resData;
        }catch (Exception e){
            logger.error("bitgitSwap getMarkPrice error accountId={}, accessKey={}, symbol={}, method={}, res={}, e={}",
                    accountId, accessKey, instrument_id, method, res, e.getMessage(), e);
        }
        return null;
    }

    public String getCacheAccountId() {
        if (accountId == null || "".equalsIgnoreCase(accountId)) {
            accountId = getAccountId();
        }
        return accountId;
    }

    public String getAccountId() {
        String res = null;
        try {
            TreeMap<String, Object> paramMap = new TreeMap<>();
            paramMap.put("method", "accounts");
            signUpTreeMap(paramMap, "get");
            String method = "/api/v1/account/accounts?";
            res = getTreeMap(method, paramMap);

            JSONObject jsonObject = JSON.parseObject(res);
            String resData = jsonObject.getString("data");
            List<HashMap<String, String>> list = JSON.parseObject(resData, new TypeReference<List<HashMap<String, String>>>() {});
            Map<String, String> map = (list!=null && !list.isEmpty()) ? list.get(0) : Collections.EMPTY_MAP;
            return map.get("id");
        }catch (Exception e){
            logger.error("bitgitSwap getAccountId error res={}, e={}", res, e.getMessage(), e);
        }
        return null;
    }

    //----------------------------辅助封装--------------------------------

    private String get(String method) {
        String res = null;
        Response response = null;
        try {
            Request.Builder builder = new Request.Builder().url(baseUrl + method).get();
            Request request = builder.build();
            response = client.newCall(request).execute();
            res = response.body().string();
            JSONObject jsonObject = JSON.parseObject(res);
            String status = jsonObject.getString("status");
            if (!"ok".equalsIgnoreCase(status)) {
                logger.warn("bitgitSwap get status is not ok method={}, res={}", method, res);
            }
        }catch (Exception e){
             logger.error("bitgitSwap get error accountId={}, accessKey={}, method={}, res={}, code={}, msg={}, e={}",
                     accountId, accessKey, method, res, response.code(), response.message(), e.getMessage(), e);
        }
        return res;
    }

    private String postTreeMap(String method, TreeMap<String, Object> paramMap, String signs) {
        String res = null;
        try {
            RequestBody body = getRequestBody(paramMap);
            Request.Builder builder = new Request.Builder().url(baseUrl + method + "?" + signs).post(body);
            Request request = builder.build();
             Response response = client.newCall(request).execute();

            res = response.body().string();
            JSONObject jsonObject = JSON.parseObject(res);
            String status = jsonObject.getString("status");
            if (!"ok".equalsIgnoreCase(status)) {
                logger.warn("bitgitSwap postTreeMap fail method={}, paramMap={}, res={}, accountId={}, accessKey={}",
                        method, paramMap.toString(), res, accountId, accessKey);
            }
        }catch (Exception e){
            logger.error("bitgitSwap postTreeMap error method={}, paramMap={}, res={}, accountId={}, accessKey={}, e={}",
                    method, paramMap.toString(), res, accountId, accessKey, e.getMessage(), e);
        }
        return res;
    }

    private String getTreeMap(String method, TreeMap<String, Object> paramMap) {
        String res = null;
        try {
            Request.Builder builder = new Request.Builder().url(baseUrl + method + serializeComplexSource(paramMap)).get();
            Request request = builder.build();
            Response response = client.newCall(request).execute();
            res = response.body().string();
            JSONObject jsonObject = JSON.parseObject(res);
            String status = jsonObject.getString("status");
            if (!"ok".equalsIgnoreCase(status)) {
                logger.warn("bitgitSwap getTreeMap status is not ok method={}, paramMap={}, res={}, accountId={}, accessKey={}",
                        method, paramMap.toString(), res, accountId, accessKey);
            }
        }catch (Exception e){
            logger.error("bitgitSwap getTreeMap error method={}, paramMap={}, res={}, e={}",
                    method, paramMap.toString(), res, e.getMessage(), e);
        }
        return res;
    }

    private String serializeComplexSource(TreeMap<String, Object> params) {
        StringBuilder rc = new StringBuilder(2048);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            Object value = entry.getValue();
            if (StringUtils.isEmpty(value.toString())) {
                continue;
            }
            if (value instanceof List) {
                List<String> col = (List<String>) value;
                Collections.sort(col);
                for (String s : col) {
                    rc.append("&").append(entry.getKey().trim()).append("=").append(s);
                }
            }else {
                rc.append("&").append(entry.getKey()).append("=").append(value);
            }
        }
        if (rc.length() > 0) {
            rc.deleteCharAt(0);
        }
        return rc.toString();
    }


    private String signUpTreeMap(TreeMap<String, Object> paramMap, String methodReq) {
        try {
            String params = signUpParamTree(paramMap);
            String secret = digest(secretKey);
            String sign = sign(params, secret);
            long req_time = System.currentTimeMillis();
            if ("get".equals(methodReq)) {
                paramMap.put("sign", sign);
                paramMap.put("req_time", req_time);
                paramMap.put("accesskey", accessKey);
                // System.out.println(params + "&sign=" + sign + "&req_time=" + req_time + "&accesskey=" + accessKey);
                return "";
            }
            return "sign=" + sign + "&req_time=" + req_time + "&accesskey=" + accessKey;
        }catch (Exception e){
            logger.error("bitgitSwap signUpTreeMap error methodReq={}, params={}, secret={}, e={}",
                    methodReq, paramMap, secretKey, e.getMessage(), e);
        }
        return "";
    }

    /**
     * 生成签名消息
     * @param valueStr 要签名的字符串
     * @param keyStr   签名密钥
     * @return sign
     */
    private String sign(String valueStr, String keyStr) {
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

        MessageDigest md;
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

    private String encodingCharset = "UTF-8";
    private String digest(String valueStr) {
        valueStr = valueStr.trim();
        byte value[];
        try {
            value = valueStr.getBytes(encodingCharset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return toHex(md.digest(value));
    }

    private String toHex (byte input[]) {
        if (input == null) return null;
        StringBuilder output = new StringBuilder(input.length * 2);
        for (byte anInput : input) {
            int current = anInput & 0xff;
            if (current < 16) {
                output.append("0");
            }
            output.append(Integer.toString(current, 16));
        }
        return output.toString();
    }

    private String signUpParamTree(TreeMap<String, Object> paramMap) {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, Object> entry : paramMap.entrySet()){
            sb.append(entry.getKey()).append("=").append(entry.getValue().toString()).append("&");
        }
        return sb.substring(0, sb.length() - 1);
    }

    private RequestBody getRequestBody(Map<String, Object> paramMap){
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            formBodyBuilder.add(entry.getKey(), entry.getValue().toString());
        }
        return formBodyBuilder.build();
    }

    private String joinId(List<String> idList){
        StringBuilder sb = new StringBuilder();
        for(String str : idList){
            sb.append(str).append(",");
        }
        return sb.length() <= 0 ? sb.toString() : sb.substring(0, sb.length() - 1);
    }

    public String adjustMargin(String productCode, String amount, Integer positionType, Integer type) {
        String method = null, res = null;
        TreeMap<String, Object> paramMap = new TreeMap<>();
        try {
            paramMap.put("method", "adjustMargin");
            paramMap.put("amount",amount);
            paramMap.put("positionType",positionType);
            paramMap.put("type",type);
            signUpTreeMap(paramMap, "get");
            method = "/api/swap/v1/"+productCode+"/adjustMargin?";
            res = getTreeMap(method, paramMap);

            JSONObject jsonObject = JSON.parseObject(res);
            String status = jsonObject.getString("status");
            if (!"ok".equalsIgnoreCase(status)) {
                logger.warn("bitgitSwap getPosition error accountId={}, accessKey={}, method={}, res={}",
                        accountId, accessKey, method, res);
                return null;
            }
            String resData = jsonObject.getString("data");
            return resData;
        }catch (Exception e){
            logger.error("bitgitSwap getPosition error accountId={}, accessKey={}, paramMap={}, method={}, res={}, e={}",
                    accountId, accessKey, paramMap.toString(), method, res, e.getMessage(), e);
        }
        return null;
    }
}