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
    
    
    /**
     * 获取用户信息
     * method:固定值，写getAccountInfo就好
     * accesskey:自己的accesskey
     * @throws Exception
     */
    public static String testGetAccountInfo(string accesskey,string secretkey,string tradeURL,long stamp) {
      String param = "method=getAccountInfo&accesskey="+accesskey;
      secretkey = digest(secretkey);
      String sign = hmacSign(param, secretkey);
      tradeURL += "getAccountInfo?" + param + "&sign=" + sign + "&reqTime=" + stamp;
      Console.WriteLine(tradeURL);
      String result = HttpConnectToServer(tradeURL);
      return result;
    }
    /**
     * 委托下单
     * method:固定值，写order就好
     * accesskey:自己的accesskey
     * price:单价，最多8位小数
     * amout:数量，最多12位小数
     * tradeType:交易类型，1:卖，0:买
     * currency:交易对，ps:输入的交易对确保是本系统所支持的。
     * @throws Exception
     */
    public static String order(string accesskey,string secretkey,string tradeURL,long stamp,string price,string amount,string tradeType,string currency) {
      String param = "method=order&accesskey="+accesskey+"&price="+price+"&amount="+amount+"&tradeType="+tradeType+"&currency="+currency;
      secretkey = digest(secretkey);
      String sign = hmacSign(param, secretkey);
      tradeURL += "order?" + param + "&sign=" + sign + "&reqTime=" + stamp;
      Console.WriteLine(tradeURL);
      String result = HttpConnectToServer(tradeURL);
      return result;
    }
    /**
     * 取消下单
     * menthod:固定值，写cancel就好
     * accesskey:自己的accesskey
     * id:下单的编号
     * currency:交易对，ps:输入的交易对确保是本系统所支持的。
     * @throws Exception
     */
    public static String cancel(string accesskey,string secretkey,string tradeURL,long stamp,string id,string currency) {
      String param = "method=cancel&accesskey="+accesskey+"&id="+id+"&currency="+currency;
      secretkey = digest(secretkey);
      String sign = hmacSign(param, secretkey);
      tradeURL += "cancel?" + param + "&sign=" + sign + "&reqTime=" + stamp;
      Console.WriteLine(tradeURL);
      String result = HttpConnectToServer(tradeURL);
      return result;
    }
    /**
     * 获取某个交易单内容
     * method:固定值，写getOrder就好
     * accesskey:自己的accesskey
     * id:交易单编号
     * currency:交易对，ps:输入的交易对确保是本系统所支持的。
     * @throws Exception
     */
    public static String getOrder(string accesskey,string secretkey,string tradeURL,long stamp,string id,string currency) {
      String param = "method=getOrder&accesskey="+accesskey+"&id="+id+"&currency="+currency;
      secretkey = digest(secretkey);
      String sign = hmacSign(param, secretkey);
      tradeURL += "getOrder?" + param + "&sign=" + sign + "&reqTime=" + stamp;
      Console.WriteLine(tradeURL);
      String result = HttpConnectToServer(tradeURL);
      return result;
    }
    /**
     * 获取自己名下某种类型交易的所以交易单
     * method:固定值，写getOrders就好
     * accesskey:自己的accesskey
     * tradeType:交易类型，1:卖，0:买
     * currency:交易对，ps:输入的交易对确保是本系统所支持的。
     * pageIndex:第几页
     * pageSize:每页多少条
     * @throws Exception
     */
    public static String getOrders(string accesskey,string secretkey,string tradeURL,long stamp,string currency,string pageIndex,string pageSize) {
      String param = "method=getOrders&accesskey="+accesskey+"&currency="+currency+"&pageIndex="+pageIndex+"&pageSize="+pageSize;
      secretkey = digest(secretkey);
      String sign = hmacSign(param, secretkey);
      tradeURL += "getOrders?" + param + "&sign=" + sign + "&reqTime=" + stamp;
      Console.WriteLine(tradeURL);
      String result = HttpConnectToServer(tradeURL);
      return result;
    }
    /**
     * 获取用户充值地址
     * method:固定值，写getUserAddress就好
     * accesskey:自己的accesskey
     * currency:系统所支持的货币类型，
     * @throws Exception
     */
    public static String getUserAddress(string accesskey,string secretkey,string tradeURL,long stamp,string currency) {
      String param = "method=getUserAddress&accesskey="+accesskey+"&currency="+currency;
      secretkey = digest(secretkey);
      String sign = hmacSign(param, secretkey);
      tradeURL += "getUserAddress?" + param + "&sign=" + sign + "&reqTime=" + stamp;
      Console.WriteLine(tradeURL);
      String result = HttpConnectToServer(tradeURL);
      return result;
    }
    /**
     * 获取数字资产提现记录
     * method:固定值，写getWithdrawRecord就好
     * accesskey:自己的accesskey
     * currency:系统所支持的货币类型，
     * pageIndex:第几页
     * pageSize:每页有多少条数据
     * @throws Exception
     */
    public static String getWithdrawRecord(string accesskey,string secretkey,string tradeURL,long stamp,string currency,string pageIndex,string pageSize) {
      String param = "method=getWithdrawRecord&accesskey="+accesskey+"&currency="+currency+"&pageIndex="+pageIndex+"&pageSize="+pageSize;
      secretkey = digest(secretkey);
      String sign = hmacSign(param, secretkey);
      tradeURL += "getWithdrawRecord?" + param + "&sign=" + sign + "&reqTime=" + stamp;
      Console.WriteLine(tradeURL);
      String result = HttpConnectToServer(tradeURL);
      return result;
    }
    /**
     * 获取数字资产充值记录
     * method:固定值，写getChargeRecord就好
     * accesskey:自己的accesskey
     * currency:系统所支持的货币类型，
     * pageIndex:第几页
     * pageSize:每页有多少条数据
     * @throws Exception
     */
    public static String getChargeRecord(string accesskey,string secretkey,string tradeURL,long stamp,string currency,string pageIndex,string pageSize) {
      String param = "method=getChargeRecord&accesskey="+accesskey+"&currency="+currency+"&pageIndex="+pageIndex+"&pageSize="+pageSize;
      secretkey = digest(secretkey);
      String sign = hmacSign(param, secretkey);
      tradeURL += "getChargeRecord?" + param + "&sign=" + sign + "&reqTime=" + stamp;
      Console.WriteLine(tradeURL);
      String result = HttpConnectToServer(tradeURL);
      return result;
    }
    /**
     * 提现
     * method:固定值，withdraw
     * accesskey:自己的accesskey
     * amount：提现数量
     * currency：提现币种
     * fees：提现手续费
     * receiveAddress：提现地址
     * @throws Exception
     */
    public static String withdraw(string accesskey,string secretkey,string tradeURL,long stamp,string amount,string currency,string fees,string receiveAddress) {
      String param = "method=withdraw&accesskey="+accesskey+"&amount="+amount+"&currency="+currency+"&fees="+fees+"&receiveAddress="+receiveAddress;
      secretkey = digest(secretkey);
      String sign = hmacSign(param, secretkey);
      tradeURL += "withdraw?" + param + "&sign=" + sign + "&reqTime=" + stamp;
      Console.WriteLine(tradeURL);
      String result = HttpConnectToServer(tradeURL);
      return result;
    }
    /**
    * 行情
    */
    public static String ticker(string marketURL,string currency) {
      String param = "method=ticker&currency="+currency; 
      marketURL += "ticker?" + param ;
      Console.WriteLine(marketURL);
      String result = HttpConnectToServer(marketURL);
      return result;
    }
    /**
    * 市场深度
    */
    public static String depth(string marketURL,string currency,string size,string merge) {
      String param = "method=depth&currency="+currency+"&size="+size+"&merge="+merge; 
      marketURL += "depth?" + param ;
      Console.WriteLine(marketURL);
      String result = HttpConnectToServer(marketURL);
      return result;
    }
    /**
    * 历史成交
    */
    public static String trades(string marketURL,string currency) {
      String param = "method=trades&currency="+currency; 
      marketURL += "trades?" + param ;
      Console.WriteLine(marketURL);
      String result = HttpConnectToServer(marketURL);
      return result;
    }
    /**
    * k线
    */
    public static String kline(string marketURL,string currency,string type,string size) {
      String param = "method=kline&currency="+currency+"&type="+type+"&size="+size; 
      marketURL += "kline?" + param ;
      Console.WriteLine(marketURL);
      String result = HttpConnectToServer(marketURL);
      return result;
    }
    
    
    public static void Main(string[] args) {
      string accesskey = "ak****0f";
      string secretkey = "dd******88463";
      string tradeURL = "http://devapi.upex.com:8083/api-server/api/v2/";
      string marketURL = "http://devapi.upex.com:8083/api-server/data/v2/";
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
      
      //result = testGetAccountInfo(accesskey,secretkey,tradeURL,stamp);
      //result = order(accesskey, secretkey, tradeURL, stamp, "0.12", "1", "0", "iost_btc");
      //result = cancel(accesskey, secretkey, tradeURL, stamp, "391400717711093760", "iost_btc");
      //result = getOrder(accesskey, secretkey, tradeURL, stamp, "391400717711093760", "iost_btc");
      //result = getOrders(accesskey, secretkey, tradeURL, stamp, "iost_btc","1","40");
      //result = getUserAddress(accesskey, secretkey, tradeURL, stamp, "btc");
      //result = getWithdrawRecord(accesskey, secretkey, tradeURL, stamp, "btc","1","40");
      //result = getChargeRecord(accesskey, secretkey, tradeURL, stamp, "btc","1","40");
      //result = withdraw(accesskey, secretkey, tradeURL, stamp, "2","btc","0.01","1PaHiYCBFXuotKSSg7ZFGxB4n99CaDNYi");
      
      
      
      //result = ticker(marketURL,"iost_btc");
      //result = depth(marketURL,"iost_btc","20","0.01");
      //result = trades(marketURL,"iost_btc");
      result = kline(marketURL,"iost_btc","1","0");
      
      Console.WriteLine(result);
    }
  }
}