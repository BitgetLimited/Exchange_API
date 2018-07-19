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
	"sort"
	"net/url"
	"io/ioutil"
)
const accessKey string="ake662d54c38d442c0"
const secretKey string="5373621290734285922bc4d4e7b260b9"
const marketUrl string="http://localhost:8081/api/v1"
const dataUrl string="http://localhost:8081/data/v1"


func main() {

	//kline("iost_btc","1min", "20")
	//merged("iost_btc")
	//tickers()
	//depth("iost_btc","step0")
	//trade("iost_btc")
	//trades("iost_btc","10")
	//detail("iost_btc")
	//symbols()
	//currencys()
	//timestamp()

	//accounts()
	//balance("390350274889256960")
	//place("390350274889256960","10","eth_btc","buy-limit","0.003")
	//submitcancel("403457624143605760")
	//order("403457624143605760")
	//matchresults("403457624143605760")
	//symbol string,types string,start_date string,end_date string,states string,size string,from string,direct string
	//matchresultsHistory("eth_btc","buy-limit","2018-06-01","2018-07-20","submitted","10","402727408014241792","prev")
	//orders("eth_btc","buy-limit","2018-06-01","2018-07-20","submitted","10","402727408014241792","prev")
	//address string,amount string,currency string,fees string
	//withdrawCreate("1PaHiYCBFXuotKSSg7ZFGxB4n99CaDNYi","10","btc","0.001")
	//withdrawCancel("275")
	withdrawSelect("btc","withdraw","10")

}

/**** 交易类接口示例 start *****/

func accounts(){
	mapParams := make(map[string]string)
	mapParams["method"] = "accounts"
	strParams := Map2UrlQuery(mapParams)
	//orderParam := "method=order&accesskey="+accessKey+"&price="+price+"&amount="+amount+"&tradeType="+tradeType+"&currency="+currency
	orderSign := hmacSign(strParams)
	reTime := strconv.FormatInt(time.Now().UnixNano()/1000000, 10)
	accountsURL := marketUrl+"/account/accounts?"+strParams+"&accesskey="+accessKey+"&sign="+orderSign+"&req_time="+reTime
	httpGet(accountsURL)
}
func balance(account_id string){
	mapParams := make(map[string]string)
	mapParams["method"] = "balance"
	strParams := Map2UrlQuery(mapParams)
	//orderParam := "method=order&accesskey="+accessKey+"&price="+price+"&amount="+amount+"&tradeType="+tradeType+"&currency="+currency
	orderSign := hmacSign(strParams)
	reTime := strconv.FormatInt(time.Now().UnixNano()/1000000, 10)
	balanceURL := marketUrl+"/accounts/"+account_id+"/balance?"+strParams+"&accesskey="+accessKey+"&sign="+orderSign+"&req_time="+reTime
	httpGet(balanceURL)
}
//委单
func place(account_id string,amount string,symbol string,types string,price string){
	mapParams := make(map[string]string)
	mapParams["method"] = "place"
	mapParams["account_id"] = account_id
	mapParams["amount"] = amount
	mapParams["symbol"] = symbol
	mapParams["type"] = types
	if 0 < len(price) {
		mapParams["price"] = price
	}
	strParams := Map2UrlQuery(mapParams)
	orderSign := hmacSign(strParams)
	reTime := strconv.FormatInt(time.Now().UnixNano()/1000000, 10)
	placeURL := marketUrl+"/order/orders/place?accesskey="+accessKey+"&sign="+orderSign+"&req_time="+reTime
	body :=HttpPostRequest(placeURL,mapParams)
	fmt.Println(body)
}
//取消委单
func submitcancel(order_id string){
	mapParams := make(map[string]string)
	mapParams["method"] = "submitcancel"
	strParams := Map2UrlQuery(mapParams)
	orderSign := hmacSign(strParams)
	reTime := strconv.FormatInt(time.Now().UnixNano()/1000000, 10)
	placeURL := marketUrl+"/order/orders/"+order_id+"/submitcancel?accesskey="+accessKey+"&sign="+orderSign+"&req_time="+reTime
	body :=HttpPostRequest(placeURL,mapParams)
	fmt.Println(body)
}
//查询委单
func order(order_id string){
	mapParams := make(map[string]string)
	mapParams["method"] = "order"
	strParams := Map2UrlQuery(mapParams)
	orderSign := hmacSign(strParams)
	reTime := strconv.FormatInt(time.Now().UnixNano()/1000000, 10)
	placeURL := marketUrl+"/order/orders/"+order_id+"?accesskey="+accessKey+"&sign="+orderSign+"&req_time="+reTime
	body :=HttpPostRequest(placeURL,mapParams)
	fmt.Println(body)
}
//查询某个订单的成交明细
func matchresults(order_id string){
	mapParams := make(map[string]string)
	mapParams["method"] = "matchresults"
	strParams := Map2UrlQuery(mapParams)
	orderSign := hmacSign(strParams)
	reTime := strconv.FormatInt(time.Now().UnixNano()/1000000, 10)
	placeURL := marketUrl+"/order/orders/"+order_id+"/matchresults?accesskey="+accessKey+"&sign="+orderSign+"&req_time="+reTime
	body :=HttpPostRequest(placeURL,mapParams)
	fmt.Println(body)
}
//查询某个订单的成交明细
func matchresultsHistory(symbol string,types string,start_date string,end_date string,states string,size string,from string,direct string){
	mapParams := make(map[string]string)
	mapParams["method"] = "matchresultsHistory"
	mapParams["symbol"] = symbol
	mapParams["types"] = types
	mapParams["start_date"] = start_date
	mapParams["end_date"] = end_date
	mapParams["states"] = states
	mapParams["size"] = size
	if 0 < len(from) {
		if 0 < len(direct) {
			mapParams["from"] = from
			mapParams["direct"] = direct
		}
	}
	strParams := Map2UrlQuery(mapParams)
	orderSign := hmacSign(strParams)
	reTime := strconv.FormatInt(time.Now().UnixNano()/1000000, 10)
	placeURL := marketUrl+"/order/matchresults?accesskey="+accessKey+"&sign="+orderSign+"&req_time="+reTime
	body :=HttpPostRequest(placeURL,mapParams)
	fmt.Println(body)
}
//查询某个订单的成交明细 get方式
func orders(symbol string,types string,start_date string,end_date string,states string,size string,from string,direct string){
	mapParams := make(map[string]string)
	mapParams["method"] = "orders"
	mapParams["symbol"] = symbol
	mapParams["types"] = types
	mapParams["start_date"] = start_date
	mapParams["end_date"] = end_date
	mapParams["states"] = states
	mapParams["size"] = size
	if 0 < len(from) {
		if 0 < len(direct) {
			mapParams["from"] = from
			mapParams["direct"] = direct
		}
	}
	strParams := Map2UrlQuery(mapParams)
	orderSign := hmacSign(strParams)
	reTime := strconv.FormatInt(time.Now().UnixNano()/1000000, 10)
	placeURL := marketUrl+"/order/orders?"+strParams+"&accesskey="+accessKey+"&sign="+orderSign+"&req_time="+reTime
	httpGet(placeURL)
}
//提现
func withdrawCreate(address string,amount string,currency string,fees string){
	mapParams := make(map[string]string)
	mapParams["method"] = "withdrawCreate"
	mapParams["address"] = address
	mapParams["amount"] = amount
	mapParams["currency"] = currency
	mapParams["fees"] = fees
	strParams := Map2UrlQuery(mapParams)
	orderSign := hmacSign(strParams)
	reTime := strconv.FormatInt(time.Now().UnixNano()/1000000, 10)
	placeURL := marketUrl+"/dw/withdraw/api/create?accesskey="+accessKey+"&sign="+orderSign+"&req_time="+reTime
	body :=HttpPostRequest(placeURL,mapParams)
	fmt.Println(body)
}
//取消提现
func withdrawCancel(withdraw_id string){
	mapParams := make(map[string]string)
	mapParams["method"] = "withdrawCancel"
	strParams := Map2UrlQuery(mapParams)
	orderSign := hmacSign(strParams)
	reTime := strconv.FormatInt(time.Now().UnixNano()/1000000, 10)
	placeURL := marketUrl+"/dw/withdraw-virtual/"+withdraw_id+"/cancel?accesskey="+accessKey+"&sign="+orderSign+"&req_time="+reTime
	body :=HttpPostRequest(placeURL,mapParams)
	fmt.Println(body)
}
//查询提现记录
func withdrawSelect(currency string,types string,size string){
	mapParams := make(map[string]string)
	mapParams["method"] = "withdrawSelect"
	mapParams["currency"] = currency
	mapParams["type"] = types
	mapParams["size"] = size
	strParams := Map2UrlQuery(mapParams)
	orderSign := hmacSign(strParams)
	reTime := strconv.FormatInt(time.Now().UnixNano()/1000000, 10)
	placeURL := marketUrl+"/order/deposit_withdraw?"+strParams+"&accesskey="+accessKey+"&sign="+orderSign+"&req_time="+reTime
	httpGet(placeURL)
}



/**** 交易类接口示例 end *****/


/**** 行情类接口示例 start *****/
/**
* k线
*/
func kline( currency string, tradeType string, size string){
	klineDataURL := dataUrl + "/market/history/kline?symbol=" + currency + "&period=" +tradeType+ "&size=" +size
	httpGet(klineDataURL)
}
func merged( currency string){
	klineDataURL := dataUrl + "/market/detail/merged?symbol=" + currency
	httpGet(klineDataURL)
}
func tickers(){
	klineDataURL := dataUrl + "/market/tickers"
	httpGet(klineDataURL)
}
func depth(currency string, types string){
	klineDataURL := dataUrl + "/market/depth?symbol="+currency+"&type="+types
	httpGet(klineDataURL)
}
func trade(currency string){
	klineDataURL := dataUrl + "/market/trade?symbol="+currency
	httpGet(klineDataURL)
}
func trades(currency string, size string){
	klineDataURL := dataUrl + "/market/history/trade?symbol="+currency+"&size="+size
	httpGet(klineDataURL)
}
func detail(currency string){
	klineDataURL := dataUrl + "/market/detail?symbol="+currency
	httpGet(klineDataURL)
}
func symbols(){
	klineDataURL := dataUrl + "/common/symbols"
	httpGet(klineDataURL)
}
func currencys(){
	klineDataURL := dataUrl + "/common/currencys"
	httpGet(klineDataURL)
}
func timestamp(){
	klineDataURL := dataUrl + "/common/timestamp"
	httpGet(klineDataURL)
}
/**** 行情类接口示例 end *****/

/********** util ******************/

func Map2UrlQuery(mapParams map[string]string) string {
	var keys []string
	for k := range mapParams {
		keys = append(keys, k)
	}
	sort.Strings(keys)
	//fmt.Println(keys)

	var strParams string
	for j := 0; j < len(keys); j++ {
		//fmt.Println(j)
		strParams += (keys[j] + "=" + mapParams[keys[j]] + "&")
	}

	if 0 < len(strParams) {
		strParams = string([]rune(strParams)[:len(strParams)-1])
	}
	//fmt.Println(strParams)
	return strParams
}

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


func HttpPostRequest(strUrl string, mapParams map[string]string) string {
	data := make(url.Values)
	var keys []string
	for k := range mapParams {
		keys = append(keys, k)
	}
	sort.Strings(keys)
	for j := 0; j < len(keys); j++ {
		data[keys[j]] = []string{mapParams[keys[j]]}
	}
	res, err := http.PostForm(strUrl, data)

	if err != nil {
		fmt.Println(err.Error())
		//return
	}
	defer res.Body.Close()
	body, err := ioutil.ReadAll(res.Body)
	if nil != err {
		return err.Error()
	}
	return string(body)

}
