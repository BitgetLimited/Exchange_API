package com.upex.contract.dto;

public class PpBatchOrder {
    //String s = HttpUtil.syncGetString(baseUrl + "api/swap/v1/btcusd/ledgerMargin");
    private String client_oid;
    // 下单数量
    private String size;
    // 1:开多 2:开空 3:平多 4:平空
    private String type;
    // 0:普通，1：只做maker;2:全部成交或立即取消(FOK);3:立即成交并取消剩余(IOC)
    private String order_type;
    // 以对手价下单
    private String match_price;
    // 委托价格
    private String price;

    public String getClient_oid() {
        return client_oid;
    }

    public void setClient_oid(String client_oid) {
        this.client_oid = client_oid;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getMatch_price() {
        return match_price;
    }

    public void setMatch_price(String match_price) {
        this.match_price = match_price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
