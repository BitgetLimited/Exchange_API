
var config = require('../config/default');
var CryptoJS = require('crypto-js');
var Promise = require('bluebird');
var http = require('../framework/httpClient');
var crypto = require('crypto');

const URL_BITGET_PRO = 'https://api.bitget.com';

//const URL_BITGET_PRO = 'https://capi.bitget.com';

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

function sign_sha(signParam) {
    //var p = require('querystring').stringify(data);
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
    var data1 = Buffer(signParam.length)
    for(var i = 0;i<secretkeySHA.length;i++){
        key1[i] = secretkeySHA[i].charCodeAt();
    }
    for(var i = 0;i<signParam.length;i++){
        data1[i] = signParam[i].charCodeAt();
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

function signSort(body){
    let keys = Object.keys(body);
    keys.sort();
    let str = '';
    for(let i of keys){
        str += i + '=' + body[i] + '&';
    }
    var signParams = str.slice(0,-1);
    return signParams;
}

function call_api(url) {
    return new Promise(resolve => {
        var headers = DEFAULT_HEADERS;
        headers.AuthData = get_auth();
        http.get(url, {
            timeout: 500000,
            headers: headers
        }).then(data => {
            console.log("data="+data)
        }).catch(ex => {
            console.log( '异常', ex);
            resolve(null);
        });
    });
}

function call_api_post(url,body){
    return new Promise(resolve => {
        var headers = DEFAULT_HEADERS;
        headers.AuthData = get_auth();
        http.form_post(url, body, {
            timeout: 500000,
            headers: headers
        }).then(data => {
            console.log(data);
        }).catch(ex => {
            console.log('异常', ex);
            resolve(null);
        });
    });
}

var BITGET_PRO = {
    accounts: function(accesskey){
        var path = '/api/v1/account/accounts';
        var body = {'method':'accounts'};
        //var p = require('querystring').stringify(body);
        var signParam = signSort(body);
        var payload = sign_sha(signParam);
        var longtime = Date.now();
        var url = URL_BITGET_PRO+path+"?"+signParam+"&accesskey="+accesskey+"&sign="+payload+"&req_time="+longtime;
        return call_api(url);

    },
    balance: function(accesskey,id){
        var path = '/api/v1/accounts/'+id+'/balance';
        var body = {'method':'balance'};
        //var p = require('querystring').stringify(body);
        var signParam = signSort(body);
        var payload = sign_sha(signParam);
        var longtime = Date.now();
        var url = URL_BITGET_PRO+path+"?"+signParam+"&accesskey="+accesskey+"&sign="+payload+"&req_time="+longtime;
        return call_api(url);

    },
    place: function(accesskey,account_id,amount,price,symbol,type){
        var path = '/api/v1/order/orders/place';
        var body = {
          'method':'place',
          'account_id':account_id,
          'type':type,
          'amount':amount,
            'symbol':symbol,
        }
        if(price){
            body['price'] = price;
        }
        // var p = require('querystring').stringify(body);
        // console.log("p="+p)
        var signParam = signSort(body);
        var payload = sign_sha(signParam);
        var longtime = Date.now();
        var url = URL_BITGET_PRO+path+"?accesskey="+accesskey+"&sign="+payload+"&req_time="+longtime;
        return call_api_post(url,body);

    },
    submitcancel: function(accesskey,order_id){
        var path = '/api/v1/order/orders/'+order_id+'/submitcancel';
        var body = {
          'method':'submitcancel'
        }
        //var p = require('querystring').stringify(body);
        var signParam = signSort(body);
        var payload = sign_sha(signParam);
        var longtime = Date.now();
        var url = URL_BITGET_PRO+path+"?accesskey="+accesskey+"&sign="+payload+"&req_time="+longtime;
        return call_api_post(url,body);

    },
    batchcancel: function(accesskey,order_ids){
        var path = '/api/v1/order/orders/batchcancel';
        var body = {
          'method':'batchcancel',
          'order_ids':order_ids
        }
        //var p = require('querystring').stringify(body);
        var signParam = signSort(body);
        var payload = sign_sha(signParam);
        var longtime = Date.now();
        var url = URL_BITGET_PRO+path+"?accesskey="+accesskey+"&sign="+payload+"&req_time="+longtime;
        return call_api_post(url,body);

    },
    order: function(accesskey,order_id){
        var path = '/api/v1/order/orders/'+order_id;
        var body = {
          'method':'order'
        }
        //var p = require('querystring').stringify(body);
        var signParam = signSort(body);
        var payload = sign_sha(signParam);
        var longtime = Date.now();
        var url = URL_BITGET_PRO+path+"?accesskey="+accesskey+"&sign="+payload+"&req_time="+longtime;
        return call_api_post(url,body);

    },
    matchresults: function(accesskey,order_id){
        var path = '/api/v1/order/orders/'+order_id+'/matchresults';
        var body = {
          'method':'orderMatchresults'
        }
        //var p = require('querystring').stringify(body);
        var signParam = signSort(body);
        var payload = sign_sha(signParam);
        var longtime = Date.now();
        var url = URL_BITGET_PRO+path+"?accesskey="+accesskey+"&sign="+payload+"&req_time="+longtime;
        return call_api_post(url,body);


    },
    matchresultsHistory: function(accesskey,symbol,types,start_date,end_date,states,size,fromId,direct){
        var path = '/api/v1/order/matchresults';
        var body = {
          'method':'matchresults',
          'symbol':symbol,
          'types':types,
          'start_date':start_date,
          'end_date':end_date,
          'states':states,
          'size':size
        }
        if(fromId){
            if(direct){
                body['from'] = fromId;
                body['direct'] = direct;
            }else{
                return false;
            }
        }
        //var p = require('querystring').stringify(body);
        var signParam = signSort(body);
        var payload = sign_sha(signParam);
        var longtime = Date.now();
        var url = URL_BITGET_PRO+path+"?accesskey="+accesskey+"&sign="+payload+"&req_time="+longtime;
        return call_api_post(url,body);

    },
    orders: function(accesskey,symbol,types,start_date,end_date,states,size,fromId,direct){
        var path = '/api/v1/order/orders';
        var body = {
          'method':'matchresults',
          'symbol':symbol,
          'types':types,
          'start_date':start_date,
          'end_date':end_date,
          'states':states,
          'size':size
        }
        if(fromId){
            if(direct){
                body['from'] = fromId;
                body['direct'] = direct;
            }else{
                return false;
            }
        }
        //var p = require('querystring').stringify(body);
        var signParam = signSort(body);
        var payload = sign_sha(signParam);
        var longtime = Date.now();
        var url = URL_BITGET_PRO+path+"?"+signParam+"&accesskey="+accesskey+"&sign="+payload+"&req_time="+longtime;
        return call_api(url);

    },
    withdrawCreate: function(accesskey,addressWithdraw,amount,currency){
        var path = '/api/v1/dw/withdraw/api/create';
        var body = {
          'method':'withdrawCreate',
          'address':addressWithdraw,
          'amount':amount,
          'currency':currency
        }
        //var p = require('querystring').stringify(body);
        var signParam = signSort(body);
        var payload = sign_sha(signParam);
        var longtime = Date.now();
        var url = URL_BITGET_PRO+path+"?accesskey="+accesskey+"&sign="+payload+"&req_time="+longtime;
        return call_api_post(url,body);

    },
    withdrawCancel: function(accesskey,withdraw_id){
        var path = '/api/v1/dw/withdraw-virtual/'+withdraw_id+'/cancel';
        var body = {
          'method':'withdrawCancel'
        }
        //var p = require('querystring').stringify(body);
        var signParam = signSort(body);
        var payload = sign_sha(signParam);
        var longtime = Date.now();
        var url = URL_BITGET_PRO+path+"?accesskey="+accesskey+"&sign="+payload+"&req_time="+longtime;
        return call_api_post(url,body);

    },
    withdrawSelect: function(accesskey,currency,type,size){
        var path = '/api/v1/order/deposit_withdraw';
        var body = {
          'method':'withdrawCancel',
          'currency':currency,
          'type':type,
          'size':size
        }
        //var p = require('querystring').stringify(body);
        var signParam = signSort(body);
        var payload = sign_sha(signParam);
        var longtime = Date.now();
        var url = URL_BITGET_PRO+path+"?"+signParam+"&accesskey="+accesskey+"&sign="+payload+"&req_time="+longtime;
        return call_api(url);

    },

    ticker: function(currency) {
        var path = '/data/v2/ticker';
        var body = "method=ticker&currency="+currency;
        var url = URL_BITGET_PRO+path+"?"+body;
        return call_api(url);
    },
    depth: function(currency,type) {
        var path = '/data/v1/market/depth';
        var body = "method=depth&symbol="+currency+"&type="+type;
        var url = URL_BITGET_PRO+path+"?"+body;
        return call_api(url);
    },
    trades: function(currency,size) {
        var path = '/data/v1/market/history/trade';
        var body = "method=trades&symbol="+currency+"&size="+size;
        var url = URL_BITGET_PRO+path+"?"+body;
        return call_api(url);
    },
    trade: function(currency) {
        var path = '/data/v1/market/trade';
        var body = "method=trade&symbol="+currency;
        var url = URL_BITGET_PRO+path+"?"+body;
        return call_api(url);
    },
    //k线
    kline: function(currency,type,size) {
        var path = '/data/v1/market/history/kline';
        var body = "method=kline&symbol="+currency+"&period="+type+"&size="+size;
        var url = URL_BITGET_PRO+path+"?"+body;
        return call_api(url);
    },

    merged: function(currency) {
        var path = '/data/v1/market/detail/merged';
        var body = "method=merged&symbol="+currency;
        var url = URL_BITGET_PRO+path+"?"+body;
        return call_api(url);
    },
    tickers: function() {
        var path = '/data/v1/market/tickers';
        var url = URL_BITGET_PRO+path;
        return call_api(url);
    },
    detail: function(currency) {
        var path = '/data/v1/market/detail';
        var body = "symbol="+currency;
        var url = URL_BITGET_PRO+path+'?'+body;
        return call_api(url);
    },
    symbols: function() {
        var path = '/data/v1/common/symbols';
        var url = URL_BITGET_PRO+path;
        return call_api(url);
    },
    currencys: function() {
        var path = '/data/v1/common/currencys';
        var url = URL_BITGET_PRO+path;
        return call_api(url);
    },
    timestamp: function() {
        var path = '/data/v1/common/timestamp';
        var url = URL_BITGET_PRO+path;
        return call_api(url);
    }
}

module.exports = BITGET_PRO;