package main

import (
	"net/http"
	"os"
	"io"
	"fmt"
	"crypto/sha1"
	"encoding/hex"
	"crypto/md5"
	"crypto/hmac"
	"time"
	"strconv"
)
const accessKey string="ak**0f"
const secretKey string="dd***463"
const marketUrl string="http://devapi.upex.com:8083/api-server/api/v2/"
const dataUrl string="http://devapi.upex.com:8083/api-server/data/v2/"


func main() {
	//ticker( "iost_btc")
	//depth("iost_btc","20", "0.1")
	//trades("iost_btc")
	//kline("iost_btc","0", "20")



	//fmt.Println(hmacSign("method=order&accesskey=akdf619e727f20400f&price=1&amount=2&tradeType=0&currency=iost_btc"))
	//order("0.12","1","0","iost_btc")
	//cancel("391400717711093760","iost_btc")
	//getOrder("391400717711093760","iost_btc")
	//getOrders("0","iost_btc","1","40")
	//getAccountInfo()
	//getUserAddress("btc")
	//getWithdrawRecord("btc","1","40")
	//getChargeRecord("btc","1","40")
	//withdraw("3","btc","0.01","1PaHiYCBFXuotKSSg7ZFGxB4n99CaDNYi")

}

/**** 交易类接口示例 start *****/
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
func order(price string,amount string,tradeType string,currency string){
	orderParam := "method=order&accesskey="+accessKey+"&price="+price+"&amount="+amount+"&tradeType="+tradeType+"&currency="+currency
	orderSign := hmacSign(orderParam)
	reTime := strconv.FormatInt(time.Now().UnixNano()/1000000, 10)
	orderURL := marketUrl+"order?"+orderParam+"&sign="+orderSign+"&reqTime="+reTime
	httpGet(orderURL)
}
/**
 * 取消下单
 * menthod:固定值，写cancel就好
 * accesskey:自己的accesskey
 * id:下单的编号
 * currency:交易对，ps:输入的交易对确保是本系统所支持的。
 * @throws Exception
 */
func cancel(id string,currency string){
	orderParam := "method=cancel&accesskey="+accessKey+"&id="+id+"&currency="+currency
	orderSign := hmacSign(orderParam)
	reTime := strconv.FormatInt(time.Now().UnixNano()/1000000, 10)
	orderURL := marketUrl+"cancel?"+orderParam+"&sign="+orderSign+"&reqTime="+reTime
	httpGet(orderURL)
}
/**
 * 获取某个交易单内容
 * method:固定值，写getOrder就好
 * accesskey:自己的accesskey
 * id:交易单编号
 * currency:交易对，ps:输入的交易对确保是本系统所支持的。
 * @throws Exception
 */
func getOrder(id string,currency string){
	orderParam := "method=getOrder&accesskey="+accessKey+"&id="+id+"&currency="+currency
	orderSign := hmacSign(orderParam)
	reTime := strconv.FormatInt(time.Now().UnixNano()/1000000, 10)
	orderURL := marketUrl+"getOrder?"+orderParam+"&sign="+orderSign+"&reqTime="+reTime
	httpGet(orderURL)
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
func getOrders(tradeType string,currency string,pageIndex string,pageSize string){
	orderParam := "method=getOrders&accesskey="+accessKey+"&tradeType="+tradeType+"&currency="+currency+"&pageIndex="+pageIndex+"&pageSize="+pageSize
	orderSign := hmacSign(orderParam)
	reTime := strconv.FormatInt(time.Now().UnixNano()/1000000, 10)
	orderURL := marketUrl+"getOrders?"+orderParam+"&sign="+orderSign+"&reqTime="+reTime
	httpGet(orderURL)
}
/**
 * 获取用户信息
 * method:固定值，写getAccountInfo就好
 * accesskey:自己的accesskey
 * @throws Exception
 */
func getAccountInfo(){
	orderParam := "method=getAccountInfo&accesskey="+accessKey
	orderSign := hmacSign(orderParam)
	reTime := strconv.FormatInt(time.Now().UnixNano()/1000000, 10)
	orderURL := marketUrl+"getAccountInfo?"+orderParam+"&sign="+orderSign+"&reqTime="+reTime
	httpGet(orderURL)
}
/**
 * 获取用户充值地址
 * method:固定值，写getUserAddress就好
 * accesskey:自己的accesskey
 * currency:系统所支持的货币类型，
 * @throws Exception
 */
func getUserAddress(currency string){
	orderParam := "method=getUserAddress&accesskey="+accessKey+"&currency="+currency
	orderSign := hmacSign(orderParam)
	reTime := strconv.FormatInt(time.Now().UnixNano()/1000000, 10)
	orderURL := marketUrl+"getUserAddress?"+orderParam+"&sign="+orderSign+"&reqTime="+reTime
	httpGet(orderURL)
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
func getWithdrawRecord(currency string,pageIndex string,pageSize string){
	orderParam := "method=getWithdrawRecord&accesskey="+accessKey+"&currency="+currency+"&pageIndex="+pageIndex+"&pageSize="+pageSize
	orderSign := hmacSign(orderParam)
	reTime := strconv.FormatInt(time.Now().UnixNano()/1000000, 10)
	orderURL := marketUrl+"getWithdrawRecord?"+orderParam+"&sign="+orderSign+"&reqTime="+reTime
	httpGet(orderURL)
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
func getChargeRecord(currency string,pageIndex string,pageSize string){
	orderParam := "method=getChargeRecord&accesskey="+accessKey+"&currency="+currency+"&pageIndex="+pageIndex+"&pageSize="+pageSize
	orderSign := hmacSign(orderParam)
	reTime := strconv.FormatInt(time.Now().UnixNano()/1000000, 10)
	orderURL := marketUrl+"getChargeRecord?"+orderParam+"&sign="+orderSign+"&reqTime="+reTime
	httpGet(orderURL)
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
func withdraw(amount string,currency string,fees string,receiveAddress string){
	orderParam := "method=withdraw&accesskey="+accessKey+"&amount="+amount+"&currency="+currency+"&fees="+fees+"&receiveAddress="+receiveAddress
	orderSign := hmacSign(orderParam)
	reTime := strconv.FormatInt(time.Now().UnixNano()/1000000, 10)
	orderURL := marketUrl+"withdraw?"+orderParam+"&sign="+orderSign+"&reqTime="+reTime
	httpGet(orderURL)
}

/**** 交易类接口示例 end *****/


/**** 行情类接口示例 start *****/
/**
* 行情
*/
func ticker(currency string){
	tickerDataURL := dataUrl + "ticker?currency=" + currency
	httpGet(tickerDataURL)
}
/**
* 市场深度
*/
func depth( currency string, size string, merge string){
	depthDataURL := dataUrl + "depth?currency=" + currency + "&size=" +size+ "&merge=" +merge
	httpGet(depthDataURL)
}
/**
* 历史成交
*/
func trades( currency string){
	tradesDataURL := dataUrl + "trades?currency=" + currency
	httpGet(tradesDataURL)
}
/**
* k线
*/
func kline( currency string, tradeType string, size string){
	klineDataURL := dataUrl + "kline?currency=" + currency + "&type=" +tradeType+ "&size=" +size
	httpGet(klineDataURL)
}
/**** 行情类接口示例 end *****/

/********** util ******************/

func digest() string {
	hash := sha1.New()
	hash.Write([]byte(secretKey))
	return hex.EncodeToString(hash.Sum(nil))
}

// hmac MD5
func hmacSign(message string) string {
	hmac := hmac.New(md5.New, []byte(digest()))
	hmac.Write([]byte(message))
	return hex.EncodeToString(hmac.Sum(nil))
}


func httpGet(url1 string) {
	//生成client 参数为默认
	client := &http.Client{}

	//生成要访问的url
	url := url1

	//提交请求
	reqest, err := http.NewRequest("GET", url, nil)

	if err != nil {
		panic(err)
	}

	//处理返回结果
	response, _ := client.Do(reqest)

	//将结果定位到标准输出 也可以直接打印出来 或者定位到其他地方进行相应的处理
	stdout := os.Stdout
	_, err = io.Copy(stdout, response.Body)

	//返回的状态码
	status := response.StatusCode

	fmt.Println(status)
}
