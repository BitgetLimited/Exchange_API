using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Net;
using System.IO;
namespace bitget {
  class bitget_api {
    private static String encodingCharset = "UTF-8";
    /**
     *
     * @param aValue  要加密的文字
     * @param aKey  密钥
     * @return
     */
    public static String hmacSign(String aValue, String aKey) {
      byte[] k_ipad = new byte[64];
      byte[] k_opad = new byte[64];
      byte[] keyb;
      byte[] value;
      Encoding coding = Encoding.GetEncoding(encodingCharset);
      try {
        keyb = coding.GetBytes(aKey);
        value = coding.GetBytes(aValue);
      }
      catch (Exception e) {
        keyb = null;
        value =null;
        //throw;
      }
      for (int i = keyb.Length; i < 64; i++) {
        k_ipad[i] = (byte)54;
        k_opad[i] = (byte)92;
      }
      for (int i = 0; i < keyb.Length; i++) {
        k_ipad[i] = (byte) (keyb[i] ^ 0x36);
        k_opad[i] = (byte) (keyb[i] ^ 0x5c);
      }
      byte[] sMd5_1 = MakeMD5(k_ipad.Concat(value).ToArray());
      byte[] dg = MakeMD5(k_opad.Concat(sMd5_1).ToArray());
      return toHex(dg);
    }
    public static String toHex(byte[] input) {
      if (input == null)
              return null;
      StringBuilder output = new StringBuilder(input.Length * 2);
      for (int i = 0; i < input.Length; i++) {
        int current = input[i] & 0xff;
        if (current < 16)
                  output.Append('0');
        output.Append( current.ToString("x"));
      }
      return output.ToString();
    }
    /**
     * 
     * @param args
     * @param key
     * @return
     */
    public static String getHmac(String[] args, String key) {
      if (args == null || args.Length == 0) {
        return (null);
      }
      StringBuilder str = new StringBuilder();
      for (int i = 0; i < args.Length; i++) {
        str.Append(args[i]);
      }
      return (hmacSign(str.ToString(), key));
    }
    /// <summary>
    /// 生成MD5摘要
    /// </summary>
    /// <param name="original">数据源</param>
    /// <returns>摘要</returns>
    public static byte[] MakeMD5(byte[] original) {
      MD5CryptoServiceProvider hashmd5 = new MD5CryptoServiceProvider();
      byte[] keyhash = hashmd5.ComputeHash(original);
      hashmd5 = null;
      return keyhash;
    }
    /**
     * SHA加密
     * @param aValue
     * @return
     */
    public static String digest(String aValue) {
      aValue = aValue.Trim();
      byte[] value;
      SHA1 sha = null;
      Encoding coding = Encoding.GetEncoding(encodingCharset);
      try {
        value = coding.GetBytes(aValue);
        HashAlgorithm ha=(HashAlgorithm) CryptoConfig.CreateFromName("SHA");
        value= ha.ComputeHash(value);
      }
      catch (Exception e) {
        throw;
      }
      return toHex(value);
    }
    /**
     * HttpConnectToServer
     */
    public static string HttpConnectToServer(string url) {
      //创建请求
      HttpWebRequest request = (HttpWebRequest)HttpWebRequest.Create(url);
      request.Method = "GET";
      request.ContentType = "application/x-www-form-urlencoded";
      //读取返回消息
      string res = string.Empty;
      try {
        HttpWebResponse response = (HttpWebResponse)request.GetResponse();
        StreamReader reader = new StreamReader(response.GetResponseStream(), Encoding.UTF8);
        res = reader.ReadToEnd();
        reader.Close();
      }
      catch (Exception ex) {
        return null;
        //连接服务器失败
      }
      return res;
    }
    
    public static string HttpPost(string url, string paramData, Dictionary<string, string> headerDic = null)
    {
      string result = string.Empty;
      try
      {
        HttpWebRequest wbRequest = (HttpWebRequest)WebRequest.Create(url);
        wbRequest.Method = "POST";
        wbRequest.ContentType = "application/x-www-form-urlencoded";
        wbRequest.ContentLength = Encoding.UTF8.GetByteCount(paramData);
        if (headerDic != null && headerDic.Count > 0)
        {
          foreach (var item in headerDic)
          {
            wbRequest.Headers.Add(item.Key, item.Value);
          }
        }
        using (Stream requestStream = wbRequest.GetRequestStream())
        {
          using (StreamWriter swrite = new StreamWriter(requestStream))
          {
            swrite.Write(paramData);
          }
        }
        HttpWebResponse wbResponse = (HttpWebResponse)wbRequest.GetResponse();
        using (Stream responseStream = wbResponse.GetResponseStream())
        {
          using (StreamReader sread = new StreamReader(responseStream))
          {
            result = sread.ReadToEnd();
          }
        }
      }
      catch (Exception ex)
      { }
         
      return result;
    }
    
    /**
     * 取消委托下单
     */
    public static String submitcancel(string accesskey,string secretkey,string tradeURL,long stamp,string order_id) {
      Dictionary<string, string> dic = new Dictionary<string, string>();
      dic.Add("method", "submitcancel");
      String param = "";
      foreach (KeyValuePair<string,string> kv in dic)
      {
        param += kv.Key + "=" + kv.Value + "&";
      }
      param=param.Substring(0,param.Length-1);
      Console.WriteLine(param);
      secretkey = digest(secretkey);
      String sign = hmacSign(param, secretkey);
      tradeURL += "/order/orders/"+order_id+"/submitcancel?sign=" + sign + "&req_time=" + stamp + "&accesskey=" + accesskey;
      Console.WriteLine(tradeURL);
      String result = HttpPost(tradeURL,param,null);
      //Console.WriteLine(result);
      return result;
    }
    /**
     * 下单
     */
    public static String place(string accesskey,string secretkey,string tradeURL,long stamp,string price,string amount,string tradeType,string currency,string account_id) {
      Dictionary<string, string> dic = new Dictionary<string, string>();
      dic.Add("amount", amount);
      dic.Add("type", tradeType);
      dic.Add("symbol", currency);
      dic.Add("account_id", account_id);
      if (!string.IsNullOrEmpty(price))
      {
        dic.Add("price", price);
      }
      String param = "";
      foreach (KeyValuePair<string,string> kv in dic)
      {
        param += kv.Key + "=" + kv.Value + "&";
      }
      param=param.Substring(0,param.Length-1);
      Console.WriteLine(param);
      secretkey = digest(secretkey);
      String sign = hmacSign(param, secretkey);
      tradeURL += "/order/orders/place?sign=" + sign + "&req_time=" + stamp + "&accesskey=" + accesskey;
      Console.WriteLine(tradeURL);
      String result = HttpPost(tradeURL,param,null);
      Console.WriteLine(result);
      return result;
    }
    /**
     * 查询某个订单详情 所有状态的订单详情
     */
    public static String order(string accesskey,string secretkey,string tradeURL,long stamp,string order_id) {
      Dictionary<string, string> dic = new Dictionary<string, string>();
      dic.Add("method", "order");
      String param = "";
      foreach (KeyValuePair<string,string> kv in dic)
      {
        param += kv.Key + "=" + kv.Value + "&";
      }
      param=param.Substring(0,param.Length-1);
      Console.WriteLine(param);
      secretkey = digest(secretkey);
      String sign = hmacSign(param, secretkey);
      tradeURL += "/order/orders/"+order_id+"?sign=" + sign + "&req_time=" + stamp + "&accesskey=" + accesskey;
      Console.WriteLine(tradeURL);
      String result = HttpPost(tradeURL,param,null);
      //Console.WriteLine(result);
      return result;
    }
    /**
     * 查询某个订单详情 必须有撮合的才可以
     */
    public static String matchresults(string accesskey,string secretkey,string tradeURL,long stamp,string order_id) {
      Dictionary<string, string> dic = new Dictionary<string, string>();
      dic.Add("method", "orderMatchresults");
      String param = "";
      foreach (KeyValuePair<string,string> kv in dic)
      {
        param += kv.Key + "=" + kv.Value + "&";
      }
      param=param.Substring(0,param.Length-1);
      Console.WriteLine(param);
      secretkey = digest(secretkey);
      String sign = hmacSign(param, secretkey);
      tradeURL += "/order/orders/"+order_id+"/matchresults?sign=" + sign + "&req_time=" + stamp + "&accesskey=" + accesskey;
      Console.WriteLine(tradeURL);
      String result = HttpPost(tradeURL,param,null);
      //Console.WriteLine(result);
      return result;
    }
    /**
     * 查询历史订单。post方式
     */
    public static String matchresultsHistory(string accesskey,string secretkey,string tradeURL,long stamp,
      string symbol,string types,string start_date,string end_date,string states,string size,string from,string direct) {
      Dictionary<string, string> dic = new Dictionary<string, string>();
      dic.Add("method", "orderMatchresults");
      dic.Add("symbol", symbol);
      dic.Add("types", types);
      dic.Add("start_date", start_date);
      dic.Add("end_date", end_date);
      dic.Add("states", states);
      dic.Add("size", size);
      if (!string.IsNullOrEmpty(from))
      {
        if (!string.IsNullOrEmpty(direct))
        {
          dic.Add("from", from);
          dic.Add("direct", direct);
        }
      }
      
      String param = "";
      foreach (KeyValuePair<string,string> kv in dic)
      {
        param += kv.Key + "=" + kv.Value + "&";
      }
      param=param.Substring(0,param.Length-1);
      Console.WriteLine(param);
      secretkey = digest(secretkey);
      String sign = hmacSign(param, secretkey);
      tradeURL += "/order/matchresults?sign=" + sign + "&req_time=" + stamp + "&accesskey=" + accesskey;
      Console.WriteLine(tradeURL);
      String result = HttpPost(tradeURL,param,null);
      //Console.WriteLine(result);
      return result;
    }
    /**
     * 查询历史订单，get方式
     */
    public static String orders(string accesskey,string secretkey,string tradeURL,long stamp,
      string symbol,string types,string start_date,string end_date,string states,string size,string from,string direct) {
      Dictionary<string, string> dic = new Dictionary<string, string>();
      dic.Add("method", "orderMatchresults");
      dic.Add("symbol", symbol);
      dic.Add("types", types);
      dic.Add("start_date", start_date);
      dic.Add("end_date", end_date);
      dic.Add("states", states);
      dic.Add("size", size);
      if (!string.IsNullOrEmpty(from))
      {
        if (!string.IsNullOrEmpty(direct))
        {
          dic.Add("from", from);
          dic.Add("direct", direct);
        }
      }
      
      String param = "";
      foreach (KeyValuePair<string,string> kv in dic)
      {
        param += kv.Key + "=" + kv.Value + "&";
      }
      param=param.Substring(0,param.Length-1);
      Console.WriteLine(param);
      secretkey = digest(secretkey);
      String sign = hmacSign(param, secretkey);
      tradeURL += "/order/orders?"+param+"&sign=" + sign + "&req_time=" + stamp + "&accesskey=" + accesskey;
      Console.WriteLine(tradeURL);
      String result = HttpConnectToServer(tradeURL);
      //Console.WriteLine(result);
      return result;
    }
    /**
     * 提现
     */
    public static String withdrawCreate(string accesskey,string secretkey,string tradeURL,long stamp,
      string address,string amount,string currency,string fees) {
      Dictionary<string, string> dic = new Dictionary<string, string>();
      dic.Add("method", "withdrawCreate");
      dic.Add("address", address);
      dic.Add("amount", amount);
      dic.Add("currency", currency);
      dic.Add("fees", fees);
      
      String param = "";
      foreach (KeyValuePair<string,string> kv in dic)
      {
        param += kv.Key + "=" + kv.Value + "&";
      }
      param=param.Substring(0,param.Length-1);
      Console.WriteLine(param);
      secretkey = digest(secretkey);
      String sign = hmacSign(param, secretkey);
      tradeURL += "/dw/withdraw/api/create?sign=" + sign + "&req_time=" + stamp + "&accesskey=" + accesskey;
      Console.WriteLine(tradeURL);
      String result = HttpPost(tradeURL,param,null);
      //Console.WriteLine(result);
      return result;
    }
    /**
     * 取消提现
     */
    public static String withdrawCancel(string accesskey,string secretkey,string tradeURL,long stamp,string withdraw_id) {
      Dictionary<string, string> dic = new Dictionary<string, string>();
      dic.Add("method", "withdrawCancel");
      
      String param = "";
      foreach (KeyValuePair<string,string> kv in dic)
      {
        param += kv.Key + "=" + kv.Value + "&";
      }
      param=param.Substring(0,param.Length-1);
      Console.WriteLine(param);
      secretkey = digest(secretkey);
      String sign = hmacSign(param, secretkey);
      tradeURL += "/dw/withdraw-virtual/"+withdraw_id+"/cancel?sign=" + sign + "&req_time=" + stamp + "&accesskey=" + accesskey;
      Console.WriteLine(tradeURL);
      String result = HttpPost(tradeURL,param,null);
      //Console.WriteLine(result);
      return result;
    }
    /**
     * 查询提现
     */
    public static String withdrawSelect(string accesskey,string secretkey,string tradeURL,long stamp,string currency,string type,string size) {
      Dictionary<string, string> dic = new Dictionary<string, string>();
      dic.Add("method", "withdrawSelect");
      dic.Add("currency", currency);
      dic.Add("type", type);
      dic.Add("size", size);
      String param = "";
      foreach (KeyValuePair<string,string> kv in dic)
      {
        param += kv.Key + "=" + kv.Value + "&";
      }
      param=param.Substring(0,param.Length-1);
      Console.WriteLine(param);
      secretkey = digest(secretkey);
      String sign = hmacSign(param, secretkey);
      tradeURL += "/order/deposit_withdraw?"+param+"&sign=" + sign + "&req_time=" + stamp + "&accesskey=" + accesskey;
      Console.WriteLine(tradeURL);
      String result = HttpConnectToServer(tradeURL);
      //Console.WriteLine(result);
      return result;
    }
    
    
    *
     public static String kline(string marketURL,string symbol,string period,string size) {
      String param = "method=kline&symbol="+symbol+"&period="+period+"&size="+size; 
      marketURL += "/market/history/kline?" + param ;
      Console.WriteLine(marketURL);
      String result = HttpConnectToServer(marketURL);
      return result;
    }
    public static String merged(string marketURL,string symbol) {
      String param = "method=merged&symbol="+symbol; 
      marketURL += "/market/detail/merged?" + param ;
      Console.WriteLine(marketURL);
      String result = HttpConnectToServer(marketURL);
      return result;
    }
    public static String tickers(string marketURL) {
      String param = "method=tickers"; 
      marketURL += "/market/tickers?" + param ;
      Console.WriteLine(marketURL);
      String result = HttpConnectToServer(marketURL);
      return result;
    }
    public static String depth(string marketURL,string symbol,string type) {
      String param = "method=depth&symbol="+symbol+"&type="+type; 
      marketURL += "/market/depth?" + param ;
      Console.WriteLine(marketURL);
      String result = HttpConnectToServer(marketURL);
      return result;
    }
    public static String trade(string marketURL,string symbol) {
      String param = "method=trade&symbol="+symbol; 
      marketURL += "/market/trade?" + param ;
      Console.WriteLine(marketURL);
      String result = HttpConnectToServer(marketURL);
      return result;
    }
    public static String trades(string marketURL,string symbol,string size) {
      String param = "method=trades&symbol="+symbol; 
      marketURL += "/market/history/trade?" + param ;
      Console.WriteLine(marketURL);
      String result = HttpConnectToServer(marketURL);
      return result;
    }
    public static String detail(string marketURL,string symbol) {
      String param = "method=detail&symbol="+symbol; 
      marketURL += "/market/detail?" + param ;
      Console.WriteLine(marketURL);
      String result = HttpConnectToServer(marketURL);
      return result;
    }
    public static String symbols(string marketURL) {
      String param = "method=symbols&"; 
      marketURL += "/common/symbols?" + param ;
      Console.WriteLine(marketURL);
      String result = HttpConnectToServer(marketURL);
      return result;
    }
    public static String currencys(string marketURL) {
      String param = "method=currencys&"; 
      marketURL += "/common/currencys?" + param ;
      Console.WriteLine(marketURL);
      String result = HttpConnectToServer(marketURL);
      return result;
    }
    public static String timestamp(string marketURL) {
      String param = "method=timestamp&"; 
      marketURL += "/common/timestamp?" + param ;
      Console.WriteLine(marketURL);
      String result = HttpConnectToServer(marketURL);
      return result;
    }
    
    
    public static void Main(string[] args) {
      string accesskey = "ake66212c38d442c0";
      string secretkey = "5373621290731222bc4d4e7b260b9";
      string tradeURL = "https://api.bitget.com/api/v1";
      string marketURL = "https://api.bitget.com/data/v1";
      DateTime timeStamp=new DateTime(1970,1,1);
      //得到1970年的时间戳
      long stamp= (DateTime.UtcNow.Ticks - timeStamp.Ticks)/10000;
      Console.WriteLine(accesskey);
      Console.WriteLine(secretkey);
      Console.WriteLine(tradeURL);
      Console.WriteLine(marketURL);
      Console.WriteLine(stamp);
      //注意这里有时区问题，用now就要减掉8个小时

      String result;
      
      //string price,string amount,string tradeType,string currency
      //result = place(accesskey, secretkey, tradeURL, stamp, "0.003", "10", "buy-limit", "eth_btc","390350274889256960");
      //result = submitcancel(accesskey, secretkey, tradeURL, stamp, "403682766375399424");
      //result = order(accesskey, secretkey, tradeURL, stamp, "403682766375399424");
      //result = matchresults(accesskey, secretkey, tradeURL, stamp, "403682766375399424");
      //string symbol,string types,string start_date,string end_date,string states,string size,string from,string direct
      //result = matchresultsHistory(accesskey, secretkey, tradeURL, stamp, "iost_eth","buy-limit","2018-06-01","2018-07-20","submitted","50","403743336575315968","prev");
      //result = orders(accesskey, secretkey, tradeURL, stamp, "iost_eth","buy-limit","2018-06-01","2018-07-20","submitted","50","403743336575315968","next");
      //string address,string amount,string currency,string fees
      //result = withdrawCreate(accesskey, secretkey, tradeURL, stamp, "1PaHiYCBFXuotKSSg7ZFGxB4n99CaDNYi","10","btc","0.0001");
      //result = withdrawCancel(accesskey, secretkey, tradeURL, stamp, "281");
      result = withdrawSelect(accesskey, secretkey, tradeURL, stamp, "btc","withdraw","10");
      
      
      //result = kline(marketURL,"iost_btc","1min","2");
      //result = merged(marketURL,"iost_btc");
      //result = tickers(marketURL);
      //result = depth(marketURL,"btc_usdt","step0");
      //result = trade(marketURL,"btc_usdt");
      //result = trades(marketURL,"btc_usdt","10");
      //result = detail(marketURL,"btc_usdt");
      //result = symbols(marketURL);
      //result = currencys(marketURL);
      //result = timestamp(marketURL);
      
      Console.WriteLine(result);
    }
  }
}