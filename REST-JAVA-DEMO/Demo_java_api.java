import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class Demo_java_api {

    private RestTemplate restTemplate = new RestTemplate();
    private static String encodingCharset = "UTF-8";

    private String domainUrl = "http://localhost:8084/api/v2/";

    private String accessKey = "akxxxxxxxxxxxxxxxx";
    private String secretKey = "0cxxxxxxxxxxxxxxxxxxx73a4";


    

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
    @Test
    public void order() throws Exception {
        HashMap<String, Object> paramMap = new LinkedHashMap();
        paramMap.put("method", "order");
        paramMap.put("accesskey", accessKey);
        paramMap.put("price", "999.12345678");
        paramMap.put("amount", "1");
        paramMap.put("tradeType", "1");
        paramMap.put("currency", "usdt_btc");
        signUp(paramMap);
        String result = restTemplate.getForObject(domainUrl + "order?method={method}" +
                "&accesskey={accesskey}&price={price}&amount={amount}&tradeType={tradeType}&currency={currency}" +
                "&sign={sign}&reqTime={reqTime}", String.class, paramMap);
        System.out.println(result);
    }

    /**
     * 取消下单
     * menthod:固定值，写cancel就好
     * accesskey:自己的accesskey
     * id:下单的编号
     * currency:交易对，ps:输入的交易对确保是本系统所支持的。
     * @throws Exception
     */
    @Test
    public void cancel() throws Exception {
        HashMap<String, Object> paramMap = new LinkedHashMap();
        paramMap.put("method", "cancel");
        paramMap.put("accesskey", accessKey);
        paramMap.put("id", "388892590394617856");
        paramMap.put("currency", "btm_btc");
        signUp(paramMap);
        String result = restTemplate.getForObject(domainUrl + "cancel?method={method}" +
                "&accesskey={accesskey}&id={id}&currency={currency}" +
                "&sign={sign}&reqTime={reqTime}", String.class, paramMap);
        System.out.println(result);
    }

    /**
     * 获取某个交易单内容
     * method:固定值，写getOrder就好
     * accesskey:自己的accesskey
     * id:交易单编号
     * currency:交易对，ps:输入的交易对确保是本系统所支持的。
     * @throws Exception
     */
    @Test
    public void getOrder() throws Exception {
        HashMap<String, Object> paramMap = new LinkedHashMap();
        paramMap.put("method", "getOrder");
        paramMap.put("accesskey", accessKey);
        paramMap.put("id", "388892590394617856");
        paramMap.put("currency", "usdt_btc");
        signUp(paramMap);
        String result = restTemplate.getForObject(domainUrl + "getOrder?method={method}" +
                "&accesskey={accesskey}&id={id}&currency={currency}" +
                "&sign={sign}&reqTime={reqTime}", String.class, paramMap);
        System.out.println(result);
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
    @Test
    public void getOrders() throws Exception {
        HashMap<String, Object> paramMap = new LinkedHashMap();
        paramMap.put("method", "getOrders");
        paramMap.put("accesskey", accessKey);
        paramMap.put("tradeType", "1");
        paramMap.put("currency", "usdt_btc");
        paramMap.put("pageIndex", 1);
        paramMap.put("pageSize", 40);
        signUp(paramMap);
        String result = restTemplate.getForObject(domainUrl + "getOrders?method={method}" +
                "&accesskey={accesskey}&tradeType={tradeType}&currency={currency}&pageIndex={pageIndex}&pageSize={pageSize}" +
                "&sign={sign}&reqTime={reqTime}", String.class, paramMap);
        System.out.println(result);
    }

    /**
     * 获取用户信息
     * method:固定值，写getAccountInfo就好
     * accesskey:自己的accesskey
     * @throws Exception
     */
    @Test
    public void getAccountInfo() throws Exception {
        HashMap<String, Object> paramMap = new LinkedHashMap();
        paramMap.put("method", "getAccountInfo");
        paramMap.put("accesskey", accessKey);
        signUp(paramMap);
        String result = restTemplate.getForObject(domainUrl + "getAccountInfo?method={method}" +
                "&accesskey={accesskey}" +
                "&sign={sign}&reqTime={reqTime}", String.class, paramMap);
        System.out.println(result);
    }

    /**
     * 获取用户充值地址
     * method:固定值，写getUserAddress就好
     * accesskey:自己的accesskey
     * currency:系统所支持的货币类型，
     * @throws Exception
     */
    @Test
    public void getUserAddress() throws Exception {
        HashMap<String, Object> paramMap = new LinkedHashMap();
        paramMap.put("method", "getRechargeAddress");
        paramMap.put("accesskey", accessKey);
        paramMap.put("currency", "btc");
        signUp(paramMap);
        String result = restTemplate.getForObject(domainUrl + "getUserAddress?method={method}" +
                "&accesskey={accesskey}&currency={currency}" +
                "&sign={sign}&reqTime={reqTime}", String.class, paramMap);
        System.out.println(result);
    }

    @Test
    public void getWithdrawAddress() throws Exception {
        HashMap<String, Object> paramMap = new LinkedHashMap();
        paramMap.put("method", "getWithdrawAddress");
        paramMap.put("accesskey", accessKey);
        paramMap.put("currency", "btc");
        signUp(paramMap);
        String result = restTemplate.getForObject(domainUrl + "getWithdrawAddress?method={method}" +
                "&accesskey={accesskey}&currency={currency}" +
                "&sign={sign}&reqTime={reqTime}", String.class, paramMap);

        System.out.println(result);
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
    @Test
    public void getWithdrawRecord() throws Exception {
        HashMap<String, Object> paramMap = new LinkedHashMap();
        paramMap.put("method", "getWithdrawRecord");
        paramMap.put("accesskey", accessKey);
        paramMap.put("currency", "btc");
        paramMap.put("pageIndex", 1);
        paramMap.put("pageSize", 40);
        signUp(paramMap);
        String result = restTemplate.getForObject(domainUrl + "getWithdrawRecord?method={method}" +
                "&accesskey={accesskey}&currency={currency}&pageIndex={pageIndex}&pageSize={pageSize}" +
                "&sign={sign}&reqTime={reqTime}", String.class, paramMap);
        System.out.println(result);
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
    @Test
    public void getChargeRecord() throws Exception {
        HashMap<String, Object> paramMap = new LinkedHashMap();
        paramMap.put("method", "getRechargeRecord");
        paramMap.put("accesskey", accessKey);
        paramMap.put("currency", "btc");
        paramMap.put("pageIndex", 1);
        paramMap.put("pageSize", 40);
        signUp(paramMap);
        String result = restTemplate.getForObject(domainUrl + "getChargeRecord?method={method}" +
                "&accesskey={accesskey}&currency={currency}&pageIndex={pageIndex}&pageSize={pageSize}" +
                "&sign={sign}&reqTime={reqTime}", String.class, paramMap);

        System.out.println(result);
    }

    /**
     * 获取人民币提现记录
     * method:固定值，写getCnyWithdrawRecord就好
     * accesskey:自己的accesskey
     * pageIndex:第几页
     * pageSize:每页有多少条数据
     * @throws Exception
     */
    @Test
    public void getCnyWithdrawRecord() throws Exception {
        HashMap<String, Object> paramMap = new LinkedHashMap();
        paramMap.put("method", "getCnyWithdrawRecord");
        paramMap.put("accesskey", accessKey);
        paramMap.put("pageIndex", 1);
        paramMap.put("pageSize", 40);
        signUp(paramMap);
        String result = restTemplate.getForObject(domainUrl + "getCnyWithdrawRecord?method={method}" +
                "&accesskey={accesskey}&pageIndex={pageIndex}&pageSize={pageSize}" +
                "&sign={sign}&reqTime={reqTime}", String.class, paramMap);
        System.out.println(result);
    }

    /**
     * 获取人民币充值记录
     * method:固定值，写getCnyChargeRecord就好
     * accesskey:自己的accesskey
     * pageIndex:第几页
     * pageSize:每页有多少条数据
     * @throws Exception
     */
    @Test
    public void getCnyChargeRecord() throws Exception {
        HashMap<String, Object> paramMap = new LinkedHashMap();
        paramMap.put("method", "getCnyRechargeRecord");
        paramMap.put("accesskey", accessKey);
        paramMap.put("pageIndex", 1);
        paramMap.put("pageSize", 40);
        signUp(paramMap);
        String result = restTemplate.getForObject(domainUrl + "getCnyChargeRecord?method={method}" +
                "&accesskey={accesskey}&pageIndex={pageIndex}&pageSize={pageSize}" +
                "&sign={sign}&reqTime={reqTime}", String.class, paramMap);
        System.out.println(result);
    }

    @Test
    public void withdraw() throws Exception {
        HashMap<String, Object> paramMap = new LinkedHashMap();
        paramMap.put("accesskey", accessKey);
        paramMap.put("amount", "2");
        paramMap.put("currency", "etc");
        paramMap.put("fees", "1");
        paramMap.put("method", "withdraw");
        paramMap.put("receiveAddr", "0x992c9dc9c0bcffa42f69b98d53800c1748febaa9");
        paramMap.put("safePwd", "123456qq");
        signUp(paramMap);
        String result = restTemplate.getForObject(domainUrl + "withdraw?" +
                "accesskey={accesskey}&amount={amount}&currency={currency}&fees={fees}&method={method}&receiveAddr={receiveAddr}&safePwd={safePwd}" +
                "&sign={sign}&reqTime={reqTime}", String.class, paramMap);
        System.out.println(result);
    }


    private void signUp(HashMap paramMap) {
        try {
            //......
            String params = signUpParam(paramMap);
            String secret = this.digest(secretKey);
            String sign = this.hmacSign(params, secret);
            paramMap.put("sign", sign);
            paramMap.put("reqTime", System.currentTimeMillis());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public  String signUpParam(HashMap<String,Object> paramMap){
        StringBuilder sb=new StringBuilder();
        for (String key: paramMap.keySet()) {
            sb.append(key).append("=").append(paramMap.get(key)).append("&");
        }
        return  sb.substring(0,sb.length()-1);
    }

    /**
     * SHA加密
     */
    private String digest(String valueStr) {
        valueStr = valueStr.trim();
        byte value[];
        try {
            value = valueStr.getBytes(encodingCharset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return toHex(md.digest(value));

    }

    private String toHex(byte input[]) {
        if (input == null)
            return null;
        StringBuffer output = new StringBuffer(input.length * 2);
        for (int i = 0; i < input.length; i++) {
            int current = input[i] & 0xff;
            if (current < 16)
                output.append("0");
            output.append(Integer.toString(current, 16));
        }

        return output.toString();
    }

    /**
     * 生成签名消息
     * @param aValue  要签名的字符串
     * @param aKey  签名密钥
     * @return
     */
    private String hmacSign(String aValue, String aKey) {
        byte k_ipad[] = new byte[64];
        byte k_opad[] = new byte[64];
        byte keyb[];
        byte value[];
        try {
            keyb = aKey.getBytes(encodingCharset);
            value = aValue.getBytes(encodingCharset);
        } catch (UnsupportedEncodingException e) {
            keyb = aKey.getBytes();
            value = aValue.getBytes();
        }

        Arrays.fill(k_ipad, keyb.length, 64, (byte) 54);
        Arrays.fill(k_opad, keyb.length, 64, (byte) 92);
        for (int i = 0; i < keyb.length; i++) {
            k_ipad[i] = (byte) (keyb[i] ^ 0x36);
            k_opad[i] = (byte) (keyb[i] ^ 0x5c);
        }

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {

            return null;
        }
        md.update(k_ipad);
        md.update(value);
        byte dg[] = md.digest();
        md.reset();
        md.update(k_opad);
        md.update(dg, 0, 16);
        dg = md.digest();
        return toHex(dg);
    }
}