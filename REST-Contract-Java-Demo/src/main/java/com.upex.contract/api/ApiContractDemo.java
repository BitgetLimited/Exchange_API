package com.upex.contract.api;


import com.upex.contract.Utils.BitgitSwapClient;
import com.upex.contract.Utils.HttpUtil;
import com.upex.contract.dto.PpBatchOrder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ApiContractDemo {

    private static String baseUrl = "https://capi.bitget.cc";
    private static BitgitSwapClient bitgitSwapClient = new BitgitSwapClient("ak3565fe42ed3d4d03测试", "3e1c9b8cba5b4640a9bb3985936c2d71测试", baseUrl, 1000);

    public static void main(String[] args) {
        String result = "";
        // TODO 获取深度
//          result = getDepth();
        //TODO  获取历史资金费率
//          result = getHistoryRate();
        //TODO  获取K线数据
//        result = getKLine();
//         TODO 合理标记价格
//          result = getMarkPrice();
        // TODO 所有ticker数据
//           result = getAllTicker();
        // TODO 获取单个Ticker数据
//           result = getTicker();
        // TODO 平台的成交数据
//          result = getTrades();
        // TODO 获取指数价格
//           result = getIndex();
        // TODO 获取平台总持仓量
        //   result = getOpenInterest();
        // TODO 当前限价
        //  result = getPriceLimit();
        // TODO 获取合约的下一次结算时间
//          result = getFundingTime();
        // TODO 获取合约信息
//        result = getInstruments();
        /*******************userInfo yes****************************/
        // TODO 所有合约持仓信息
//        result = getAllProductPosition();
//        // TODO 获取单个合约的仓位
//        result = getProductPosition();
//        // TODO 所有币种合约账户信息
//        result = getAllAccount();
//        // TODO 单个币种合约账户信息
//        result = getSingleAccount();
//        // TODO 获取某个合约的用户配置
//        result = getSingleSettings();
//        // TODO 设定某个合约的杠杆
//        result = settingLeverage();
//        // TODO 查询主账户流水
//        result = getLedger();
//        // TODO 查询保障金账户流水
//        result = getLedgerMargin();
        // TODO 下单
//        result = order();
//        // TODO 批量下单
//        result = batchOrder();
//        // TODO 撤单
//        result = cancleOrder();
//        // TODO 批量撤单
//        result = cancleBatchOrder();
//        // TODO 获取所有订单列表
//        result = getOrders();
//        // TODO 获取订单信息
//        result = getOrderDetail();
//        // TODO 获取成交明细
//        result = getFills();
//        // TODO 获取合约挂单冻结数量
//  result = getHolds();

        System.out.println(result);
    }

    /**
     * 获取深度
     *
     * @return
     */
    public static String getDepth() {
        String productCode = "btcusd";
        // 最多查200
        Integer size = 1;
        String s = HttpUtil.syncGetString(baseUrl + "/api/swap/v1/instruments/" + productCode + "/depth?size=" + size);
        return s;
    }

    /**
     * 获取历史资金费率
     */
    public static String getHistoryRate() {
        String productCode = "btcusd";
        // from 和 to决定查询起始页
        Integer from = 15;
        Integer to = 1;
        // 最多查100
        Integer limit = 100;
        String s = HttpUtil.syncGetString(baseUrl + "/api/swap/v1/instruments/" + productCode + "/historical_funding_rate?from=" + 2.7 + "&to=" + to + "&limit=" + limit);
        return s;
    }

    /**
     * 获取K线数据
     */
    public static String getKLine() {
        String productCode = "btcusd";
        // start和end是查询起始时间转换成国标时间后的时间，格式为(必须是ISO8601格式的时间)：yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
        String start = getTimeStamp("2019-04-12 12:12:12");
        String end = getTimeStamp("2019-04-18 12:12:12");
        /**
         *                 ONE_MINUTE("1", 60),
         *                 FIVE_MINUTE("5", 300),
         *                 FIFTEEN_MINUTE("15", 900),
         *                 HALF_ONE_HOUR("30", 1800),
         *                 ONE_HOUR("60", 3600),
         *                 FOUR_HOUR("4*60", 14400),
         *                 TWELVE_HOUR("12*60", 43200),
         *                 ONE_DAY("24*60", 86400),
         *                 SEVEN_DAY("7*24*60", 604800);
         *                 这些是granularity取值
         */
        // 查询粒度
        Integer granularity = 60;
        String s = HttpUtil.syncGetString(baseUrl + "/api/swap/v1/instruments/" + productCode + "/candles?start=" + start + "&end=" + end + "&granularity=" + granularity);
        return s;
    }

    /**
     * 获取合理标记价格
     */
    public static String getMarkPrice() {
        String productCode = "btcusd";
        String s = HttpUtil.syncGetString(baseUrl + "/api/swap/v1/instruments/" + productCode + "/mark_price");
        return s;
    }

    /**
     * 获取全部ticker信息
     */
    public static String getAllTicker() {
        String s = HttpUtil.syncGetString(baseUrl + "/api/swap/v1/instruments/ticker");
        return s;
    }

    /**
     * 获取某个ticker信息
     */
    public static String getTicker() {
        String productCode = "btcusd";
        String s = HttpUtil.syncGetString(baseUrl + "/api/swap/v1/instruments/" + productCode + "/ticker");
        return s;
    }

    /**
     * 获取成交数据
     */
    public static String getTrades() {
        String productCode = "btcusd";
        // 最大为100
        Integer limit = 12;
        String s = HttpUtil.syncGetString(baseUrl + "/api/swap/v1/instruments/" + productCode + "/trades?limit=" + limit);
        return s;
    }

    /**
     * 获取指数信息
     */
    public static String getIndex() {
        String productCode = "btcusd";
        String s = HttpUtil.syncGetString(baseUrl + "/api/swap/v1/instruments/" + productCode + "/index");
        return s;
    }

    /**
     * TODO 获取平台总的持仓量
     */
    public static String getOpenInterest() {
        String productCode = "btcusd";
        String s = HttpUtil.syncGetString(baseUrl + "/api/swap/v1/instruments/" + productCode + "/open_interest");
        return s;
    }


    /**
     * 当前限价
     */
    public static String getPriceLimit() {
        String productCode = "btcusd";
        String s = HttpUtil.syncGetString(baseUrl + "/api/swap/v1/instruments/" + productCode + "/price_limit");
        return s;
    }

    /**
     * 获取合约下一次结算时间
     */
    public static String getFundingTime() {
        String productCode = "btcusd";
        String s = HttpUtil.syncGetString(baseUrl + "/api/swap/v1/instruments/" + productCode + "/funding_time");
        return s;
    }
    /*******************userInfo yes****************************/


    /**
     * 所有合约持仓信息
     *
     * @return
     */
    static String getAllProductPosition() {
        String allProductPosition = bitgitSwapClient.getAllProductPosition();

        return allProductPosition;
    }

    /**
     * 获取单个合约的仓位
     *
     * @return
     */
    static String getProductPosition() {
        String productCode = "btcusd";
        String position = bitgitSwapClient.getPosition(productCode);
        return position;
    }

    /**
     * 所有币种合约账户信息
     *
     * @return
     */
    static String getAllAccount() {
        //  String s = HttpUtil.syncGetString(baseUrl + "api/swap/v1/accounts");
        String allAccount = bitgitSwapClient.getAllAccount();
        return allAccount;
    }

    /**
     * 单个币种合约账户信息
     *
     * @return
     */
    static String getSingleAccount() {
        String productCode = "btcusd";
        String string = bitgitSwapClient.getAccount(productCode);
        return string;
    }

    /**
     * 获取某个合约的用户配置
     *
     * @return
     */
    static String getSingleSettings() {
        String productCode = "btcusd";
        String result = bitgitSwapClient.getSingleSettings(productCode);
        return result;
    }

    /**
     * 设定某个合约的杠杆
     *
     * @return
     */
    static String settingLeverage() {
        String productCode = "btcusd";
        // 杠杆倍数，可填写1-100之间的整数：minValue = 1,maxValue = 100
        Integer leverage = 1;
        // 1:多仓；2：空仓
        Integer side = 1;
        String result = bitgitSwapClient.settingLeverage(productCode, leverage, side);

        return result;

    }


    /**
     * 查询主账户流水
     *
     * @return
     */
    static String getLedger() {
        String productCode = "btcusd";
        // from 和to 主要是查第几页的数据
        Integer from = 1;
        Integer to = 2;
        // 每页显示多少数据，最大为100
        Integer limit = 3;
        String result = bitgitSwapClient.getLedger(productCode, from, to, limit);
        return result;
    }

    /**
     * 查询保障金账户流水
     */
    static String getLedgerMargin() {
        String productCode = "btcusd";
        // from 和to 主要是查第几页的数据
        Integer from = null;
        Integer to = 2;
        // 每页显示多少数据，最大为100
        Integer limit = 3;
        String result = bitgitSwapClient.getLedgerMargin(productCode, from, to, limit);

        return result;
    }

    /**
     * 下单
     */
    static String order() {
        String client_oid = "dxdanzi";
        // 下单数量
        String size = "12";
        // 1:开多 2:开空 3:平多 4:平空
        String type = "1";
        // 0:普通，1：只做maker;2:全部成交或立即取消FOK;3:立即成交并取消剩余IOC
        String order_type = "2";
        //   LIMIT("0","限价"),
        //    MARKET("1","市价"),
        String match_price = "0";
        // 委托价格
        String price = "12";
        //合约名称
        String instrument_id = "btcusd";
        String orderResult = bitgitSwapClient.addOrder(client_oid, instrument_id, price, size, type, order_type, match_price);
        return orderResult;
    }

    /**
     * 批量下单
     */
    static String batchOrder() {
        String productCode = "btcusd";
        String order_type1 = "0";
        List<PpBatchOrder> orderList = new ArrayList<PpBatchOrder>();

        PpBatchOrder ppBatchOrder1 = new PpBatchOrder();
        ppBatchOrder1.setClient_oid("dxdanzi");
        ppBatchOrder1.setMatch_price("0");
        ppBatchOrder1.setOrder_type("0");
        ppBatchOrder1.setPrice("12");
        ppBatchOrder1.setSize("12");
        ppBatchOrder1.setType("1");
        orderList.add(ppBatchOrder1);

        PpBatchOrder ppBatchOrder2 = new PpBatchOrder();
        ppBatchOrder2.setClient_oid("dxdanzi");
        ppBatchOrder2.setMatch_price("0");
        ppBatchOrder2.setOrder_type("0");
        ppBatchOrder2.setPrice("12");
        ppBatchOrder2.setSize("12");
        ppBatchOrder2.setType("1");
        orderList.add(ppBatchOrder2);
        // 0:普通，1：只做maker;2:全部成交或立即取消;3:立即成交并取消剩余
        String orderListResult = bitgitSwapClient.addOrderList(productCode, orderList, order_type1);
        return orderListResult;
    }

    /**
     * 撤单
     */
    static String cancleOrder() {
        String productCode = "btcusd";
        String orderId = "513468410013679613";
        String clientOid = "dxdanzi";
        String result = bitgitSwapClient.cancelOrder(productCode, orderId, clientOid);
        return result;
    }

    /**
     * 批量撤单
     */
    static String cancleBatchOrder() {
        String productCode = "btcusd";
        List<String> list = new ArrayList<String>();
        list.add("502285028512760313");
        list.add("502284593123033013");
        String result = bitgitSwapClient.cancelOrderList(productCode, list, null);
        return result;
    }

    /**
     * 获取所有订单列表
     */
    static String getOrders() {
        String productCode = "btcusd";
        // -1:已撤单，0:未成交，1:部分成交，2：完全成交， 3:未成交或部分成交，4：已撤单或完全成交
        Integer status = -1;
        // from 和to主要是组成查第几页的数据
        Integer from = null;
        Integer to = 5;
        // 每页数量
        Integer limit = 5;
        String result = bitgitSwapClient.getOrders(productCode, status + "", from + "", to + "", limit + "");
        return result;
    }

    /**
     * 获取订单信息
     */
    static String getOrderDetail() {
        String productCode = "btcusd";
        String orderId = "513468410013679613";
        String orderDetail = bitgitSwapClient.getOrderDetail(productCode, orderId);
        return orderDetail;
    }

    /**
     * 获取成交明细
     */
    static String getFills() {
        String orderId = "502285028512760313";
        String productCode = "btcusd";
        String result = bitgitSwapClient.getFills(productCode, orderId);
        return result;
    }

    /**
     * 获取合约信息
     */
    static String getInstruments() {
        String s = HttpUtil.syncGetString(baseUrl + "/api/swap/v1/instruments");
        return s;
    }


    /**
     * 获取合约挂单冻结数量
     */
    static String getHolds() {
        String productCode = "btcusd";
        // String s = HttpUtil.syncGetString(baseUrl + "api/swap/v1/" + productCode + "/holds");
        String result = bitgitSwapClient.getHolds(productCode);
        return result;
    }


    /**
     * 2019-04-09 12:12:12
     *
     * @return
     */
    private static String getTimeStamp(String date) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = null;
        try {
            parse = sd.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(tz);
        String nowAsISO = df.format(parse);
        return nowAsISO;
    }

}
