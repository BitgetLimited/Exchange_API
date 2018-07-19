<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>bitget</title>
</head>
<?php

$bitget=new bitgetAPI();
#$Fond_bitget=$bitget->depth('eth_btc', 'step0');
#$Fond_bitget=$bitget->trades('iost_btc');
#$Fond_bitget=$bitget->kline('btc_usdt','1min','2');
#$Fond_bitget=$bitget->tickers();
#$Fond_bitget=$bitget->merged('eth_btc');
#$Fond_bitget=$bitget->trade('eth_btc','10');
#$Fond_bitget=$bitget->detail('btc_usdt');
#$Fond_bitget=$bitget->symbols();
#$Fond_bitget=$bitget->currencys();
#$Fond_bitget=$bitget->timestamp();


#$Fond_bitget=$bitget->place_order('390350274889256960','10','0.0009','eth_btc','buy-limit');
#$Fond_bitget=$bitget->accounts();
//390350274889256960
#$Fond_bitget=$bitget->balance('390350274889256960');
#$Fond_bitget=$bitget->submitcancel('402988673034924032');
$Fond_bitget=$bitget->batchcancel();
#$Fond_bitget=$bitget->orderSingle('402988673034924032');
#$Fond_bitget=$bitget->matchresults('402988673034924032');
//matchresultsHistory($symbol='',$types='',$start_date='',$end_date='',$states='',$size=0,$from='',$direct='')
#$Fond_bitget=$bitget->matchresultsHistory('eth_btc','buy-market','2018-06-01','2018-07-18','submitted','10','','');
#$Fond_bitget=$bitget->orders('eth_btc','buy-market','2018-06-01','2018-07-18','submitted','20','','');
//$Fond_bitget=$bitget->withdrawCreate('2','btc','0.001','1PaHiYCBFXuotKSSg7ZFGxB4n99CaDNYi');
//$Fond_bitget=$bitget->withdrawCancel('250');
#$Fond_bitget=$bitget->withdrawSelect('btc','withdraw','10');


var_dump($Fond_bitget);

class bitgetAPI {
    public $api_method = '';
    public $req_method = '';
    var  $access_key="akdf12327f20400f";
    var  $secret_key="dd906aaa62347593ee1b8b89788463";
    //行情接口的url52.193.224.131
    var $market_URL="https://api.bitget.com/data/v1";
    //交易类接口url
    var $trade_URL="https://api.bitget.com/api/v1";

    /** 发送请求 get 交易类get方法请求
     * @param $pUrl
     * @return mixed
     */
    function httpRequest_get($pUrl){
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
    /** 行情请求 行情类get请求
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

    // 组合参数
    function bind_param($param = array()) {
        $o = "";
        foreach ( $param as $k => $v )
        {
            $o.= "$k=" . urlencode( $v ). "&" ;
        }
        $post_data = substr($o,0,-1);
        return $post_data;
    }

    // 生成验签URL
    function create_sign_url($append_param = array()) {
        echo '$append_param:';
        var_dump($append_param);
        echo ':$append_param';
        $o = "";
        foreach ( $append_param as $k => $v )
        {
            $o.= "$k=" . urlencode( $v ). "&" ;
        }
        $post_data = substr($o,0,-1);
        echo '$post_data:';
        echo $post_data;
        echo ':$post_data';
        return $this->trade_URL .$this->api_method.'?'.$this->create_sig($post_data);
    }

    // 生成签名
    function create_sig($param) {
        echo '$param:';
        var_dump($param);
        echo ':$param';
        $SecretKey = sha1($this->secret_key);
        $tSign=hash_hmac('md5',$param,$SecretKey);
        $tResult = "sign=" . $tSign . "&req_time=" . time()*1000 . "&accesskey=" . $this->access_key;
        if($this->req_method == 'get'){
            $tResult = $param . '&' .$tResult;
        }
        return $tResult;
        //return base64_encode($signature);
    }
    // 发送请求
    function request_post($url = '', $post_data = array()) {
        if (empty($url) || empty($post_data)) {
            return false;
        }
        $o = "";
        foreach ( $post_data as $k => $v )
        {
            $o.= "$k=" . urlencode( $v ). "&" ;
        }
        $post_data = substr($o,0,-1);
        $postUrl = $url;
        $curlPost = $post_data;
        $ch = curl_init();//初始化curl
        curl_setopt($ch, CURLOPT_URL,$postUrl);//抓取指定网页
        curl_setopt($ch, CURLOPT_HEADER, 0);//设置header
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);//要求结果为字符串且输出到屏幕上
        curl_setopt($ch, CURLOPT_POST, 1);//post提交方式
        curl_setopt($ch, CURLOPT_POSTFIELDS, $curlPost);
        $data = curl_exec($ch);//运行curl
        curl_close($ch);
        return $data;
    }


    /********行情类API start ***********************/

    /** k线，
     */
    function kline($currency, $type, $size=0){
        $params = [
            'symbol' => $currency,
            'period' => $type
        ];
        if ($size) $params['size'] = $size;
        $param = $this->bind_param($params);
        $Url_kline = $this->market_URL . "/market/history/kline?" . $param;
        $res=$this->HangqingRequest($Url_kline);
        return $res;
    }

    /** 查询行情
     */
    function tickers(){
        $Url_ticker = $this->market_URL . "/market/tickers";
        $res = $this->HangqingRequest($Url_ticker);
        return $res;
    }

    function merged($currency){
        $params = [
            'symbol' => $currency
        ];
        $param = $this->bind_param($params);
        $Url_ticker = $this->market_URL . "/market/detail/merged?" . $param;
        $res = $this->HangqingRequest($Url_ticker);
        return $res;
    }

    /** 市场深度
     */
    function depth($currency, $type){
        $params = [
            'symbol' => $currency,
            'type' => $type
        ];
        $param = $this->bind_param($params);
        $Url_depth = $this->market_URL . "/market/depth?" . $param;
        $res=$this->HangqingRequest($Url_depth);
        return $res;
    }

    /** 历史成交
     */
    function trades($currency){
        $params = [
            'symbol' => $currency
        ];
        $param = $this->bind_param($params);
        $Url_trades = $this->market_URL . "/market/trade?" . $param;
        $res=$this->HangqingRequest($Url_trades);
        return $res;
    }



    function trade($currency, $size){
        $params = [
            'symbol' => $currency,
            'size' => $size
        ];
        $param = $this->bind_param($params);
        $Url_trades = $this->market_URL . "/market/history/trade?" . $param;
        $res=$this->HangqingRequest($Url_trades);
        return $res;
    }


    function detail($currency){
        $params = [
            'symbol' => $currency
        ];
        $param = $this->bind_param($params);
        $Url_trades = $this->market_URL . "/market/detail?" . $param;
        $res=$this->HangqingRequest($Url_trades);
        return $res;
    }

    function symbols(){
        $Url_trades = $this->market_URL . "/common/symbols";
        $res=$this->HangqingRequest($Url_trades);
        return $res;
    }


    function currencys(){
        $Url_trades = $this->market_URL . "/common/currencys";
        $res=$this->HangqingRequest($Url_trades);
        return $res;
    }


    function timestamp(){
        $Url_trades = $this->market_URL . "/common/timestamp";
        $res=$this->HangqingRequest($Url_trades);
        return $res;
    }



    /********行情类API end ***********************/


    /********交易类API start ***********************/

    function accounts(){
        $this->api_method = "/account/accounts";
        $this->req_method = 'get';
        $params['method'] = 'accounts';
        $url = $this->create_sign_url($params);
        $res = $this->httpRequest_get($url);
        return json_decode($res);
    }
    //390350274889256960
    function balance($account_id){
        $this->api_method = "/accounts/" . $account_id . "/balance";
        $this->req_method = 'get';
        $params['balance'] = 'balance';
        $url = $this->create_sign_url($params);
        $res = $this->httpRequest_get($url);
        return json_decode($res);
    }


    /** 委单
     */
    // 下单
    function place_order($account_id=0,$amount=0,$price=0,$symbol='',$type='') {
        $this->api_method = "/order/orders/place";
        $this->req_method = 'POST';
        // 数据参数
        $post_data['account_id'] = $account_id;
        $post_data['amount'] = $amount;

        $post_data['method'] = 'place';
        $post_data['symbol'] = $symbol;

        $post_data['type'] = $type;
        if ($price) {
            $post_data['price'] = $price;
        }
        $url = $this->create_sign_url($post_data);
        $res = $this->request_post($url, $post_data);
        return json_decode($res);
    }

    /** 取消委单
     */
    function submitcancel($id){
        $this->api_method = '/order/orders/' . $id . '/submitcancel';
        // 数据参数
        $post_data['method'] = 'submitcancel';
        $url = $this->create_sign_url($post_data);
        $res = $this->request_post($url, $post_data);
        return json_decode($res);
    }
    

    /** 获取单个委单详情
     */
    function orderSingle($id){
        $this->api_method = '/order/orders/' . $id ;
        // 数据参数
        $post_data['method'] = 'order';
        $url = $this->create_sign_url($post_data);
        $res = $this->request_post($url, $post_data);
        return json_decode($res);
    }


    function matchresults($id){
        $this->api_method = '/order/orders/'. $id .'/matchresults'  ;
        // 数据参数
        $post_data['method'] = 'matchresults';
        $url = $this->create_sign_url($post_data);
        $res = $this->request_post($url, $post_data);
        return json_decode($res);
    }


    function matchresultsHistory($symbol='',$types='',$start_date='',$end_date='',$states='',$size=0,$from='',$direct=''){
        $this->api_method = '/order/matchresults';
        // 数据参数
        $post_data['method'] = 'matchresults';
        $post_data['symbol'] = $symbol;
        $post_data['types'] = $types;
        $post_data['start_date'] = $start_date;
        $post_data['end_date'] = $end_date;
        $post_data['states'] = $states;
        $post_data['size'] = $size;
        if($from){
            $post_data['from'] = $from;
            if($direct){
                return false;
            }else{
                $post_data['direct'] = $direct;
            }
        }
        $url = $this->create_sign_url($post_data);
        $res = $this->request_post($url, $post_data);
        return json_decode($res);
    }

    function orders($symbol='',$types='',$start_date='',$end_date='',$states='',$size=0,$from='',$direct=''){
        $this->api_method = '/order/orders';
        $this->req_method = 'get';
        // 数据参数
        $post_data['method'] = 'orders';
        $post_data['symbol'] = $symbol;
        $post_data['types'] = $types;
        $post_data['start_date'] = $start_date;
        $post_data['end_date'] = $end_date;
        $post_data['states'] = $states;
        $post_data['size'] = $size;
        if($from){
            $post_data['from'] = $from;
            if($direct){
                return false;
            }else{
                $post_data['direct'] = $direct;
            }
        }
        $url = $this->create_sign_url($post_data);
        $res = $this->httpRequest_get($url);
        return json_decode($res);
    }

    /** 提现
     */
    function withdrawCreate($amount,$currency,$fees,$receiveAddress){
        $this->api_method = '/dw/withdraw/api/create';
        // 数据参数
        $post_data['method'] = 'withdrawCreate';
        $post_data['amount'] = $amount;
        $post_data['currency'] = $currency;
        $post_data['address'] = $receiveAddress;
        if($fees){
            $post_data['fees'] = $fees;
        }
        $url = $this->create_sign_url($post_data);
        $res = $this->request_post($url, $post_data);
        return json_decode($res);
    }

    function withdrawCancel($id=0){
        $this->api_method = '/dw/withdraw-virtual/'.$id.'/cancel';
        // 数据参数
        $post_data['method'] = 'withdrawCancel';
        $url = $this->create_sign_url($post_data);
        $res = $this->request_post($url, $post_data);
        return json_decode($res);
    }

    function withdrawSelect($currency='',$type='',$size=0){
        $this->api_method = '/order/deposit_withdraw';
        $this->req_method = 'get';
        // 数据参数
        $post_data['method'] = 'withdrawCancel';
        $post_data['currency'] = $currency;
        $post_data['type'] = $type;
        $post_data['size'] = $size;
        $url = $this->create_sign_url($post_data);
        $res = $this->httpRequest_get($url);
        return json_decode($res);
    }

    /********交易类API end ***********************/

}
?>
<body>
</body>
</html>
