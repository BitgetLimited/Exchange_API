# -*- coding:utf-8 -*-
import datetime
import urllib

import requests
import json
import time
import hashlib
import hmac
try:
    from urllib import urlencode, parse
except:
    from urllib.parse import urlencode

#行情类接口
BASE_API_PUBLIC = 'https://api.bitget.com/data/v1'
#交易类接口
BASE_API_TRADE = 'https://api.bitget.com/api/v1'

headers = {
            "Content-type": "application/x-www-form-urlencoded",
            'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36',
        }

class Client_BitGet():
    def __init__(self, apikey, secretkey):
        self._public_key = str(apikey)
        self._private_key = str(secretkey)
        self.sessn = requests.Session()
        self.adapter = requests.adapters.HTTPAdapter(pool_connections=5,
                                                     pool_maxsize=5, max_retries=5)
        self.sessn.mount('http://', self.adapter)
        self.sessn.mount('https://', self.adapter)
        self.order_list = []

    def http_post_request(self,url, params, add_to_headers=None):
        print(params)
        if add_to_headers:
            headers.update(add_to_headers)
        response = requests.post(url=url, data=params, headers=headers, timeout=30)
        try:

            if response.status_code == 200:
                return response.json()
            else:
                return
        except BaseException as e:
            print("httpPost failed, detail is:%s,%s" % (response.text, e))
            return

    def signature(self,message):
        sha_secretkey = hashlib.sha1(self._private_key.encode('utf-8')).hexdigest()
        signature = hmac.new(sha_secretkey.encode('utf-8'),message.encode('utf-8'),digestmod='MD5').hexdigest() # 32位md5算法进行加密签名
        return signature

    #交易类接口方法
    def signedRequest_Trade(self, path, params, reqMethod):
        print(params)
        param = ''
        keys = sorted(params.keys())
        for key in keys:
            param += key + '=' + str(params.get(key)) + '&'
        param = param.rstrip('&')
        print("param="+param)
        signature = self.signature(message=param)
        reqTime = str(time.time() * 1000).split('.')
        param += "&sign=" + signature
        param += "&req_time=" + reqTime[0]
        param += "&accesskey=" + self._public_key
        if (reqMethod == 'get'):
            print(reqMethod)
            url = BASE_API_TRADE + path + '?' + param;
            resp = requests.get(url, headers=headers, timeout=20)
            data = json.loads(resp.content)
            return data
        if(reqMethod == 'post'):
            #将加密信息与请求参数分开
            print(reqMethod)
            url = BASE_API_TRADE + path + '?sign=' + signature + '&req_time='+reqTime[0]+"&accesskey="+self._public_key
            print(params)
            return self.http_post_request(url=url, params=params)


    #行情类接口方法
    def signedRequest_Market(self, path, params):
        param = ''
        for key in sorted(params.keys()):
            param += key + '=' + str(params.get(key)) + '&'
        param = param.rstrip('&')
        url = BASE_API_PUBLIC + path +"?" + param
        resp = requests.get(url, headers=headers, timeout=20)
        data = json.loads(resp.content)
        return data

#################################交易类接口 start ####################
    # 获取个人ID
    def accounts(self):
        params = {
            'method': 'accounts'}
        print(self._public_key)
        resp = self.signedRequest_Trade(path='/account/accounts', params=params, reqMethod='get')
        return resp

    # 获取个人资产
    def balance(self, account_id):
        params = {
            'method': 'balance'}
        print(self._public_key)
        resp = self.signedRequest_Trade(path='/accounts/'+account_id+'/balance', params=params, reqMethod='get')
        return resp


    #委单
    def place(self,account_id,amount,types,symbol,price):
        params = {
            'account_id': account_id,
            'amount': amount,
            #'price' : price,
            'symbol': symbol,
            'type': types}
        if price:
            params["price"] = price
        resp = self.signedRequest_Trade(path='/order/orders/place',params=params, reqMethod='post')
        return resp

    #取消委单
    def cancel(self,id):
        params = {'method': 'submitcancel'}
        resp = self.signedRequest_Trade(path='/order/orders/'+id+'/submitcancel', params=params, reqMethod='post')
        return resp

    
    def batchcancel(self):
        order_ids = [1,2,3]
        print(str(order_ids))
        #'[1,2,3]'
        params = {'method':'batchcancel',"order_ids":str(order_ids)}
        resp = self.signedRequest_Trade(path='/order/orders/batchcancel', params=params, reqMethod='post')
        return resp

    #获取单个订单信息
    def getOrder(self,order_id):
        params = {'method': 'getOrder'}
        resp = self.signedRequest_Trade(path ='/order/orders/'+order_id,params=params, reqMethod='post')
        return resp

    # 获取单个订单信息
    def getOrderDetail(self, order_id):
        params = {'method':'matcharesultsSingle'}
        resp = self.signedRequest_Trade(path='/order/orders/' + order_id +'/matchresults', params=params, reqMethod='post')
        return resp

    #获取用户所有委单
    def getOrders(self,symbol, types, start_date, end_date,states,sizePage):
        params = {
            'end_date':end_date,
            'method':'getOrders',
            'size': sizePage,
            'start_date': start_date,
            'states': states,
            'symbol': symbol,
            'types': types
            }
        resp = self.signedRequest_Trade(path ='/order/matchresults',params=params, reqMethod='post')
        return resp

    # 获取用户所有委单
    def orders(self, symbol, types, start_date, end_date, states, sizePage):
        params = {
            'end_date': end_date,
            'method': 'orders',
            'size': sizePage,
            'start_date': start_date,
            'states': states,
            'symbol': symbol,
            'types': types
            }
        resp = self.signedRequest_Trade(path='/order/orders', params=params, reqMethod='get')
        return resp


    # 提现
    def withdrawCreate(self, address,amount, currency):
        params = {
            'address':address,
            'amount': amount,
            'currency':currency,
            'method': 'withdrawCreate'}
        resp = self.signedRequest_Trade(path='/dw/withdraw/api/create', params=params, reqMethod='post')
        return resp

    # 取消提现
    def withdrawCancel(self, withdrawId):
        params = {
            'method': 'withdrawCancel'}
        resp = self.signedRequest_Trade(path='/dw/withdraw-virtual/'+withdrawId+'/cancel', params=params, reqMethod='post')
        return resp

    # 冲提币查询
    def withdrawSelect(self,currency,typeSymbol,sizeNumber):
        params = {
            'method': 'withdrawCancel',
            'currency': currency,
            'type': typeSymbol,
            'size':sizeNumber}
        resp = self.signedRequest_Trade(path='/order/deposit_withdraw', params=params, reqMethod='get')
        return resp

#################################交易类接口 end####################

#################################行情类接口 start##################

    #
    def tickers(self):
        params = {}
        resp = self.signedRequest_Market(path='/market/tickers',params=params)
        return resp

    #
    def merged(self, currency):
        params = {'symbol': currency}
        resp = self.signedRequest_Market(path='/market/detail/merged', params=params)
        return resp

    #
    def depth(self,currency,type):
        params = {'symbol':currency,'type':type}
        resp = self.signedRequest_Market(path='/market/depth', params=params)
        return resp

    #
    def trade(self, currency):
        params = {'symbol': currency}
        resp = self.signedRequest_Market(path='/market/trade', params=params)
        return resp

    #
    def trades(self, currency,size):
        params = {'symbol': currency, 'size':size}
        resp = self.signedRequest_Market(path='/market/history/trade', params=params)
        return resp

    #
    def detail(self, currency):
        params = {'symbol': currency}
        resp = self.signedRequest_Market(path='/market/detail', params=params)
        return resp

    #
    def symbols(self):
        params = {}
        resp = self.signedRequest_Market(path='/common/symbols', params=params)
        return resp

    #
    def currencys(self):
        params = {}
        resp = self.signedRequest_Market(path='/common/currencys', params=params)
        return resp

    #
    def timestamp(self):
        params = {}
        resp = self.signedRequest_Market(path='/common/timestamp', params=params)
        return resp

    #k线
    def kline(self, symbol,period, size):
        params = {'symbol': symbol, 'period': period, 'size': size}
        resp = self.signedRequest_Market(path='/market/history/kline', params=params)
        return resp

#################################行情类接口 end##################

apikey = 'ak9bf3fd12d0044c6'
secretkey = '2a72d83b0ca1284a5ba5c6a6dafc2'
client = Client_BitGet(apikey,secretkey)

####order start######
#交易对类型，请填写系统所支持的交易对，例：IOST/BTC 写成：iost_btc
account_id='3903501889256960'
symbol = 'eth_btc'
types = 'buy-limit'
#单价
price = '0.009'
#要买或卖的数量
amount = '10'
respJson = client.place(account_id,amount,types,symbol,price)
print(respJson)
####order end######

# 批量撤销
# respJson = client.batchcancel()
# print(respJson)


####cancel start######
#交易对类型，请填写系统所支持的交易对，例：IOST/BTC 写成：iost_btc
#委单的ID
id = '4027320129393408'
# respJson = client.cancel(id)
# print(respJson)
####cancel end######



####getOrder start######
#委单ID
order_id = '4027321429393408'
# respJson = client.getOrder(order_id)
# print(respJson)
####getOrder end######

####getOrder start######
#委单ID
order_id = '40127917569637376'
# respJson = client.getOrderDetail(order_id)
# print(respJson)
####getOrder end######



####getOrders start######
types = 'buy-market'
#交易对类型，请填写系统所支持的交易对，例：IOST/BTC 写成：iost_btc
symbol = 'eth_btc'
#第几页，
start_date = '2018-06-01'
#每页有多少条数据，
end_date = '2018-07-17'
states = 'submitted'
sizePage = '2'
# fromId = '401608425894621184'
# direct = 'next'
# respJson = client.getOrders(symbol, types, start_date, end_date,states,sizePage)
# print(respJson)
####getOrders end######

####orders start######
types = 'buy-market'
#交易对类型，请填写系统所支持的交易对，例：IOST/BTC 写成：iost_btc
symbol = 'eth_btc'
#第几页，
start_date = '2018-06-01'
#每页有多少条数据，
end_date = '2018-07-17'
states = 'submitted'
sizePage = '2'
# respJson = client.orders(symbol, types, start_date, end_date,states,sizePage)
# print(respJson)
####orders end######

####withdraw start 提现######
#提现的数据
amount = '10'
#提现的币种
currency = 'btc'
#提现钱包地址
address = '1PaHiYCBFXuotKSSg7ZFGxB4n99CaDNYi'
# respJson = client.withdrawCreate( address,amount, currency)
# print(respJson)
####withdraw end######


####withdrawCancel start 提现######
#提现的数据
withdrawId = '2140'
# respJson = client.withdrawCancel(withdrawId)
# print(respJson)
####withdraw end######withdrawSelect(self,currency,typeSymbol,sizeNumber)


####withdrawSelect start ######
#提现的数据
currency = 'btc'
typeSymbol = 'withdraw'
sizeNumber = '10'
# respJson = client.withdrawSelect(currency,typeSymbol,sizeNumber)
# print(respJson)
####withdrawSelect end######

####accounts start ######
# respJson = client.accounts()
# print(respJson)
####accounts end######

####balance start ######
account_id = '39035174889256960'
# respJson = client.balance(account_id)
# print(respJson)
####balance end######



################行情类接口 start #############


# ticker(self,currency):

# tickers(self):
# respJson = client.tickers()
# print(respJson)
# merged(self, currency):
currency = 'eth_btc'
# respJson = client.merged(currency)
# print(respJson)
# depth(self,currency,type):
currency = 'eth_btc'
type = 'step0'
# respJson = client.depth(currency,type)
# print(respJson)
# trade(self, currency):
currency = 'eth_btc'
# respJson = client.trade(currency )
# print(respJson)
# trades(self, currency,size):
currency = 'eth_btc'
size = '50'
# respJson = client.trades(currency ,size)
# print(respJson)
# detail(self, currency):
currency = 'eth_btc'
# respJson = client.detail(currency )
# print(respJson)
# symbols(self):
# respJson = client.symbols(  )
# print(respJson)
# currencys(self):
# respJson = client.currencys(  )
# print(respJson)
# timestamp(self):
# respJson = client.timestamp(  )
# print(respJson)
# kline(self, symbol,period, size):
symbol='eth_btc'
period='1min'
size='2'
# respJson = client.kline(symbol,period,size)
# print(respJson)
