
var config = require('config');
var CryptoJS = require('crypto-js');
var Promise = require('bluebird');
var http = require('../framework/httpClient');
var crypto = require('crypto');

const URL_BITGET_PRO = 'devapi.upex.com:8083/api-server';

const DEFAULT_HEADERS = {
    "Content-Type": "application/json",
    "User-Agent": "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36"
}

function get_auth() {
    var sign = config.bitget.trade_password + 'hello, moto';
    var md5 = CryptoJS.MD5(sign).toString().toLowerCase();
    let ret = encodeURIComponent(JSON.stringify({
        assetPwd: md5
    }));
    return ret;
}

function sign_sha(data) {
    //加密dercretkey
    var secretkeySHA = encodeURIComponent(CryptoJS.SHA1(config.bitget.secretkey));

    var keyInput = Buffer(64);
    var keyOutput = Buffer(64);
    for(var i = 0;i<64;i++){
        if(i<secretkeySHA.length){
            keyInput[i] = 0;
            keyOutput[i] = 0;
        }else{
            keyInput[i] = 54;
            keyOutput[i] = 92;
        }
    }

    for(var j = 0;j<secretkeySHA.length;j++){
        keyInput[j] = (secretkeySHA[j].charCodeAt()) ^ 0x36;
        keyOutput[j] =  (secretkeySHA[j].charCodeAt()) ^ 0x5c;
    }


    var key1 = Buffer(secretkeySHA.length)
    var data1 = Buffer(data.length)
    for(var i = 0;i<secretkeySHA.length;i++){
        key1[i] = secretkeySHA[i].charCodeAt();
    }
    for(var i = 0;i<data.length;i++){
        data1[i] = data[i].charCodeAt();
    }

    var md5 = crypto.createHash('md5');
    md5.update(keyInput);
    md5.update(data1);
    var md = md5.digest();
    md5 = crypto.createHash('md5');
    md5.update(keyOutput);
    md5.update(md, 0, 16);
    md = md5.digest('hex');
    return md;
}

function call_api(url) {
    return new Promise(resolve => {
        var headers = DEFAULT_HEADERS;
        headers.AuthData = get_auth();
        http.get(url, {
            timeout: 1000,
            headers: headers
        }).then(data => {
            console.log("data="+data)
        }).catch(ex => {
            console.log(method, path, '异常', ex);
            resolve(null);
        });
    });
}

var BITGET_PRO = {
    /**
     * 获取所有订单
     * @param accesskey
     * @param tradeType
     * @param currency
     * @param pageIndex
     * @param pageSize
     * @returns {Promise|Object}
     */
    getOrders: function(accesskey,tradeType,currency,pageIndex,pageSize) {
        var path = '/api/v2/getOrders';
        var body = "method=getOrders&accesskey="+accesskey+"&tradeType="+tradeType+"&currency="+currency+"&pageIndex="+pageIndex+"&pageSize="+pageSize;
        var payload = sign_sha(body);

        var longtime = Date.now();
        var url = 'http://'+URL_BITGET_PRO+path+"?"+body+"&sign="+payload+"&reqTime="+longtime;
        return call_api(url);
    },
    order: function(accesskey,price,amount,tradeType,currency){
        var path = '/api/v2/order';
        var body = "method=order&accesskey="+accesskey+"&price="+price+"&amount="+amount+"&tradeType="+tradeType+"&currency="+currency;
        var payload = sign_sha(body);
        var longtime = Date.now();
        var url = 'http://'+URL_BITGET_PRO+path+"?"+body+"&sign="+payload+"&reqTime="+longtime;
        return call_api(url);

    },
    cancel: function(accesskey,id,currency){
        var path = '/api/v2/cancel';
        var body = "method=cancel&accesskey="+accesskey+"&id="+id+"&currency="+currency;
        var payload = sign_sha(body);
        var longtime = Date.now();
        var url = 'http://'+URL_BITGET_PRO+path+"?"+body+"&sign="+payload+"&reqTime="+longtime;
        return call_api(url);

    },
    getOrder: function(accesskey,id,currency){
        var path = '/api/v2/getOrder';
        var body = "method=getOrder&accesskey="+accesskey+"&id="+id+"&currency="+currency;
        var payload = sign_sha(body);
        var longtime = Date.now();
        var url = 'http://'+URL_BITGET_PRO+path+"?"+body+"&sign="+payload+"&reqTime="+longtime;
        return call_api(url);

    },
    getAccountInfo: function(accesskey){
        var path = '/api/v2/getAccountInfo';
        var body = "method=getAccountInfo&accesskey="+accesskey;
        var payload = sign_sha(body);
        var longtime = Date.now();
        var url = 'http://'+URL_BITGET_PRO+path+"?"+body+"&sign="+payload+"&reqTime="+longtime;
        return call_api(url);

    },
    getUserAddress: function(accesskey,currency){
        var path = '/api/v2/getUserAddress';
        var body = "method=getUserAddress&accesskey="+accesskey+"&currency="+currency;
        var payload = sign_sha(body);
        var longtime = Date.now();
        var url = 'http://'+URL_BITGET_PRO+path+"?"+body+"&sign="+payload+"&reqTime="+longtime;
        return call_api(url);

    },
    getWithdrawRecord: function(accesskey,currency,pageIndex,pageSize) {
        var path = '/api/v2/getWithdrawRecord';
        var body = "method=getWithdrawRecord&accesskey="+accesskey+"&currency="+currency+"&pageIndex="+pageIndex+"&pageSize="+pageSize;
        var payload = sign_sha(body);

        var longtime = Date.now();
        var url = 'http://'+URL_BITGET_PRO+path+"?"+body+"&sign="+payload+"&reqTime="+longtime;
        return call_api(url);
    },
    getChargeRecord: function(accesskey,currency,pageIndex,pageSize) {
        var path = '/api/v2/getChargeRecord';
        var body = "method=getChargeRecord&accesskey="+accesskey+"&currency="+currency+"&pageIndex="+pageIndex+"&pageSize="+pageSize;
        var payload = sign_sha(body);

        var longtime = Date.now();
        var url = 'http://'+URL_BITGET_PRO+path+"?"+body+"&sign="+payload+"&reqTime="+longtime;
        return call_api(url);
    },
    withdraw: function(accesskey,amount,currency,fees,receiveAddress) {
        var path = '/api/v2/withdraw';
        var body = "method=withdraw&accesskey="+accesskey+"&currency="+currency+"&amount="+amount+"&fees="+fees+"&receiveAddress="+receiveAddress;
        var payload = sign_sha(body);

        var longtime = Date.now();
        var url = 'http://'+URL_BITGET_PRO+path+"?"+body+"&sign="+payload+"&reqTime="+longtime;
        return call_api(url);
    },
    ticker: function(currency) {
        var path = '/data/v2/ticker';
        var body = "method=ticker&currency="+currency;
        var url = 'http://'+URL_BITGET_PRO+path+"?"+body;
        return call_api(url);
    },
    depth: function(currency,size,merge) {
        var path = '/data/v2/depth';
        var body = "method=depth&currency="+currency+"&size="+size+"&merge="+merge;
        var url = 'http://'+URL_BITGET_PRO+path+"?"+body;
        return call_api(url);
    },
    trades: function(currency) {
        var path = '/data/v2/trades';
        var body = "method=trades&currency="+currency;
        var url = 'http://'+URL_BITGET_PRO+path+"?"+body;
        return call_api(url);
    },
    kline: function(currency,type,size) {
        var path = '/data/v2/kline';
        var body = "method=kline&currency="+currency+"&type="+type+"&size="+size;
        var url = 'http://'+URL_BITGET_PRO+path+"?"+body;
        return call_api(url);
    }
}

module.exports = BITGET_PRO;