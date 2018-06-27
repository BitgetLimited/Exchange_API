<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>bitget</title>
</head>
<?php

$bitget=new bitgetAPI();
#$Fond_bitget=$bitget->ticker('iost_btc');
#$Fond_bitget=$bitget->depth('iost_btc', '20', '0.1');
#$Fond_bitget=$bitget->trades('iost_btc');
#$Fond_bitget=$bitget->kline('iost_btc','1','1');


#$Fond_bitget=$bitget->order('12.12','1','0','iost_btc');
#$Fond_bitget=$bitget->cancel('39****760','iost_btc');
#$Fond_bitget=$bitget->getOrder('391*****1093760','iost_btc');
$Fond_bitget=$bitget->getOrders('0','iost_btc','1','40');
#$Fond_bitget=$bitget->getAccountInfo();
#$Fond_bitget=$bitget->getUserAddress('btc');
#$Fond_bitget=$bitget->getWithdrawRecord('btc','1','40');
#$Fond_bitget=$bitget->getChargeRecord('btc','1','40');
#$Fond_bitget=$bitget->withdraw('2','btc','1','1Pa*******FGxB4n99CaDNYi');

var_dump($Fond_bitget);

class bitgetAPI {
    var  $access_key="ak***********00f";
    var  $secret_key="dd9**************b89788463";
    //行情接口的url
    var $market_URL="http://api.bitget.com/data/v2";
    //交易类接口url
    var $trade_URL="http://api.bitget.com/api/v2";

    /** 发送请求
     * @param $pUrl
     * @return mixed
     */
    function httpRequest($pUrl){
        $tCh = curl_init();
        curl_setopt($tCh, CURLOPT_URL, $pUrl);
        curl_setopt($tCh, CURLOPT_HEADER, true);
        curl_setopt($tCh, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($tCh, CURLOPT_TIMEOUT, 50);
        $tResult = curl_exec($tCh);
        curl_close($tCh);
        $tResult=json_decode ($tResult,true);
        return $tResult;
    }

    /** 行情请求
     * @param $pUrl
     * @return mixed
     */
    function HangqingRequest($pUrl){
        $tCh = curl_init();
        curl_setopt($tCh, CURLOPT_URL, $pUrl);
        curl_setopt($tCh, CURLOPT_HEADER, true);
        curl_setopt($tCh, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($tCh, CURLOPT_TIMEOUT, 50);
        $tResult = curl_exec($tCh);
        curl_close($tCh);
        $tResult = json_decode($tResult, true);
        echo $tResult;
        return $tResult;
    }


    /** 签名加密
     * @param array $pParams
     * @return string
     */
    function createSign($url,$parameters){
        $SecretKey = sha1($this->secret_key);
        $tSign=hash_hmac('md5',$parameters,$SecretKey);
        $tResult = $url . $parameters . "&sign=" . $tSign . "&reqTime=" . time()*1000;
        return $tResult;
    }

    /********行情类API start ***********************/

    /** 查询行情
     * currency：系统所支持的交易对类型
     * @return array|mixed
     */
    function ticker($currency){
        $Url_ticker = $this->market_URL . "/ticker?currency=" . $currency;
        $res = $this->HangqingRequest($Url_ticker);
        return $res;
    }

    /** 市场深度
     * @param $currency 交易对
     * @param $size 档位，1-50
     * @param $merge
     * @return mixed
     */
    function depth($currency, $size, $merge){
        $Url_depth = $this->market_URL . "/depth?currency=" . $currency . "&size=" . $size . "&merge=" . $merge;
        $res=$this->HangqingRequest($Url_depth);
        return $res;
    }

    /** 历史成交
     * @param $currency 交易对类型
     * @return mixed
     */
    function trades($currency){
        $Url_trades = $this->market_URL . "/trades?currency=" . $currency;
        $res=$this->HangqingRequest($Url_trades);
        return $res;
    }

    /** k线，
     * @param $currency 交易对类型
     * @param $type
     * @param $size
     * @return mixed
     */
    function kline($currency, $type, $size){
        $Url_kline = $this->market_URL . "/kline?currency=" . $currency . "&type=" . $type . "&size=" . $size;
        $res=$this->HangqingRequest($Url_kline);
        return $res;
    }

    /********行情类API end ***********************/


    /********交易类API start ***********************/

    /** 委单
     * @param $Price 单价
     * @param $Amount 数量
     * @param $TradeType 0(buy)/1(sell)
     * @param $currency 交易对类型(需系统所支持的交易对类型)
     * @return mixed
     */
    function order($Price,$Amount,$TradeType,$currency){
        $parameters = "method=order&accesskey=" . $this->access_key . "&price=" . $Price . "&amount=" . $Amount . "&tradeType=" . $TradeType . "&currency=" . $currency;
        $url= $this->trade_URL . "/order?";
        $post=$this->createSign($url,$parameters);
        $res=$this->httpRequest($post);
        return $res;
    }

    /** 取消委单
     * @param $id 委单ID
     * @param $currency 交易对类型，选填
     * @return mixed
     */
    function cancel($id,$currency){
        $parameters = "method=cancel&accesskey=" . $this->access_key . "&id=" . $id . "&currency=" . $currency;
        $url= $this->trade_URL . "/cancel?";
        $post=$this->createSign($url,$parameters);
        $res=$this->httpRequest($post);
        return $res;
    }

    /** 获取单个委单详情
     * @param $id 委单ID
     * @param $currency 交易对类型
     * @return mixed
     */
    function getOrder($id,$currency){
        $parameters = "method=getOrder&accesskey=" . $this->access_key . "&id=" . $id . "&currency=" . $currency;
        $url= $this->trade_URL . "/getOrder?";
        $post=$this->createSign($url,$parameters);
        $res=$this->httpRequest($post);
        return $res;
    }

    /** 查询委单列表
     * @param $tradeType 0(buy)/1(sell)
     * @param $currency 交易对类型
     * @param $pageIndex 第几页
     * @param $pageSize 每页多少条数据
     * @return mixed
     */
    function getOrders($tradeType,$currency,$pageIndex,$pageSize){
        $parameters = "method=getOrders&accesskey=" . $this->access_key . "&tradeType=" . $tradeType . "&currency=" . $currency . "&pageIndex=" . $pageIndex . "&pageSize=" . $pageSize;
        $url= $this->trade_URL . "/getOrders?";
        $post=$this->createSign($url,$parameters);
        $res=$this->httpRequest($post);
        return $res;
    }

    /** 查询用户信息
     * @return mixed
     */
    function getAccountInfo(){
        $parameters = "method=getAccountInfo&accesskey=" . $this->access_key;
        $url= $this->trade_URL . "/getAccountInfo?";
        $post=$this->createSign($url,$parameters);
        $res=$this->httpRequest($post);
        return $res;
    }

    /** 查询用户某种币的提币地址
     * @param $currency 币种
     * @return mixed
     */
    function getUserAddress($currency){
        $parameters = "method=getUserAddress&accesskey=" . $this->access_key . "&currency=" . $currency;
        $url= $this->trade_URL . "/getUserAddress?";
        $post=$this->createSign($url,$parameters);
        $res=$this->httpRequest($post);
        return $res;
    }

    /**
     * 获取数字资产提现记录
     * method:固定值，写getWithdrawRecord就好
     * accesskey:自己的accesskey
     * currency:系统所支持的货币类型，
     * pageIndex:第几页
     * pageSize:每页有多少条数据
     */
    function getWithdrawRecord($currency,$pageIndex,$pageSize){
        $parameters = "method=getWithdrawRecord&accesskey=" . $this->access_key . "&currency=" . $currency . "&pageIndex=" . $pageIndex . "&pageSize=" . $pageSize;
        $url= $this->trade_URL . "/getWithdrawRecord?";
        $post=$this->createSign($url,$parameters);
        $res=$this->httpRequest($post);
        return $res;
    }

    /**
     * 获取数字资产充值记录
     * method:固定值，写getChargeRecord就好
     * accesskey:自己的accesskey
     * currency:系统所支持的货币类型，
     * pageIndex:第几页
     * pageSize:每页有多少条数据
     */
    function getChargeRecord($currency,$pageIndex,$pageSize){
        $parameters = "method=getChargeRecord&accesskey=" . $this->access_key . "&currency=" . $currency . "&pageIndex=" . $pageIndex . "&pageSize=" . $pageSize;
        $url= $this->trade_URL . "/getChargeRecord?";
        $post=$this->createSign($url,$parameters);
        $res=$this->httpRequest($post);
        return $res;
    }

    /** 提现
     * @param $amount 提现数量
     * @param $currency 提现币种
     * @param $fees 提现手续费
     * @param $receiveAddress 提币地址
     * @return mixed
     */
    function withdraw($amount,$currency,$fees,$receiveAddress){
        $parameters = "method=withdraw&accesskey=" . $this->access_key . "&amount=" . $amount . "&currency=" . $currency . "&fees=" . $fees . "&receiveAddress=" . $receiveAddress;
        $url= $this->trade_URL . "/withdraw?";
        $post=$this->createSign($url,$parameters);
        $res=$this->httpRequest($post);
        return $res;
    }

    /********交易类API end ***********************/

}
?>
<body>
</body>
</html>
