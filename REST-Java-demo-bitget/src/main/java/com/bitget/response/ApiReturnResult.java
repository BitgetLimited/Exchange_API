package com.bitget.response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ApiReturnResult<T> implements Serializable {


    public static final String STATUS_OK = "ok";
    public static final String STATUS_ERROR = "error";

    private String status;
    private String ch;
    private Long ts;
    @SerializedName("err_code")
    private String errCode;
    @SerializedName("err_msg")
    private String errMsg;
    private T data;//返回数据


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public String toJsonString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }


    public static <T> ApiReturnResult<T> errorNoTsAndStatus(String errCode, String errMsg) {
        ApiReturnResult<T> apiReturnResult = new ApiReturnResult<>();
        apiReturnResult.setErrCode(errCode);
        apiReturnResult.setErrMsg(errMsg);
        return apiReturnResult;
    }


    public static <T> ApiReturnResult<T> error(String errCode, String errMsg) {
        ApiReturnResult<T> apiReturnResult = new ApiReturnResult<>();
        apiReturnResult.setStatus(ApiReturnResult.STATUS_ERROR);
        apiReturnResult.setTs(System.currentTimeMillis());
        apiReturnResult.setErrCode(errCode);
        apiReturnResult.setErrMsg(errMsg);
        return apiReturnResult;
    }


    public boolean isSuccess() {
        return this.status.equals(STATUS_OK);
    }

    public boolean isFail() {
        return !isSuccess();
    }

    public static ApiReturnResult ok() {
        return ok(null, null);
    }

    public static <T> ApiReturnResult<T> ok(T t) {
        return ok(t, null);
    }

    public static <T> ApiReturnResult<T> ok(T t, String ch) {
        ApiReturnResult<T> apiReturnResult = new ApiReturnResult<>();
        apiReturnResult.setData(t);
        apiReturnResult.setStatus(ApiReturnResult.STATUS_OK);
        apiReturnResult.setCh(ch);
        apiReturnResult.setTs(System.currentTimeMillis());
        return apiReturnResult;
    }

}
