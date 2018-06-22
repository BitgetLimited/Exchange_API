# -*- coding:utf-8 -*-


import requests
import json
import time
import hashlib
import hmac
try:
    from urllib import urlencode
except:
    from urllib.parse import urlencode

#行情类接口
BASE_API_PUBLIC = 'http://api.upex.com/api-server/data/v2'
#交易类接口
BASE_API_PRIVATE = 'http://api.upex.com/api-server/api/v2'

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

    #sign签名
    def signature(self,message):
        sha_secretkey = hashlib.sha1(self._private_key.encode('utf-8')).hexdigest()
        signature = hmac.new(sha_secretkey.encode('utf-8'),message.encode('utf-8'),digestmod='MD5').hexdigest() # 32位md5算法进行加密签名
        return signature

    #交易类接口方法
    def signedRequest_Trade(self, path, params):
        param = ''
        for key in sorted(params.keys()):
            param += key + '=' + str(params.get(key)) + '&'
        param = param.rstrip('&')
        signature = self.signature(message=param)
        print("signature="+signature)
        reqTime = str(time.time() * 1000).split('.')
        param += "&sign=" + signature
        param += "&reqTime=" + reqTime[0]
        url = BASE_API_PRIVATE + path +"?" + param
        resp = requests.get(url, headers=headers, timeout=20)
        data = json.loads(resp.content)
        return data

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
    #委单
    def order(self,currency,tradeType,price,amount):
        '''
            获取多个委托买单或卖单，每次请求返回10条记录
            side: 可选 buy 1 /sell 0
            pageIndex:记录页数
        '''
        currency = currency.lower()
        if 'usd' in currency:
            currency = currency.replace('usd','usdt')
        params = {'method': 'order', 'accesskey': self._public_key, 'price': price, 'amount': amount, 'tradeType': tradeType,'currency':currency}
        resp = self.signedRequest_Trade(path='/order',params=params)
        return resp

    #取消委单
    def cancel(self,id,currency):
        currency = currency.lower()
        if 'usd' in currency:
            symbol = currency.replace('usd', 'usdt')
        params = {'method': 'cancel', 'accesskey': self._public_key, 'id': id, 'currency': currency}
        resp = self.signedRequest_Trade(path='/cancel', params=params)
        return resp

    #获取单个订单信息
    def getOrder(self,order_id,currency):
        currency = currency.lower()
        if 'usd' in currency:
            currency = currency.replace('usd','usdt')
        params = {'method': 'getOrder', 'accesskey': self._public_key, 'id':order_id,'currency':currency}
        resp = self.signedRequest_Trade(path ='/getOrder',params=params)
        return resp

    #获取用户所有委单
    def getOrders(self,tradeType, currency, pageIndex, pageSize):
        currency = currency.lower()
        if 'usd' in currency:
            currency = currency.replace('usd','usdt')
        params = {'method': 'getOrders', 'accesskey': self._public_key, 'tradeType':tradeType,'currency':currency,'pageIndex':pageIndex,'pageSize':pageSize}
        resp = self.signedRequest_Trade(path ='/getOrders',params=params)
        return resp

    #获取用户信息
    def getAccountInfo(self):
        params = {'method': 'getAccountInfo', 'accesskey': self._public_key}
        resp = self.signedRequest_Trade(path ='/getAccountInfo',params=params)
        return resp

    # 获取用户某种币的钱包地址
    def getUserAddress(self,currency):
        params = {'method': 'getUserAddress', 'accesskey': self._public_key,'currency':currency}
        resp = self.signedRequest_Trade(path='/getUserAddress', params=params)
        return resp

    # 获取数字资产提现记录
    def getWithdrawRecord(self, currency, pageIndex, pageSize):
        currency = currency.lower()
        if 'usd' in currency:
            currency = currency.replace('usd', 'usdt')
        params = {'method': 'getWithdrawRecord', 'accesskey': self._public_key, 'currency': currency, 'pageIndex': pageIndex, 'pageSize': pageSize}
        resp = self.signedRequest_Trade(path='/getWithdrawRecord', params=params)
        return resp

    # 获取数字资产充值记录
    def getChargeRecord(self, currency, pageIndex, pageSize):
        currency = currency.lower()
        if 'usd' in currency:
            currency = currency.replace('usd', 'usdt')
        params = {'method': 'getChargeRecord', 'accesskey': self._public_key, 'currency': currency, 'pageIndex': pageIndex, 'pageSize': pageSize}
        resp = self.signedRequest_Trade(path='/getChargeRecord', params=params)
        return resp

    # 提现
    def withdraw(self, amount,currency, fees,receiveAddress):
        currency = currency.lower()
        if 'usd' in currency:
            currency = currency.replace('usd', 'usdt')
        params = {'method': 'withdraw', 'accesskey': self._public_key, 'amount':amount, 'currency': currency, 'fees':fees,'receiveAddress':receiveAddress}
        resp = self.signedRequest_Trade(path='/withdraw', params=params)
        return resp

#################################交易类接口 end####################

#################################行情类接口 start##################

    #行情
    def ticker(self,currency):
        currency = currency.lower()
        if 'usd' in currency:
            currency = currency.replace('usd','usdt')
        params = {'currency':currency}
        resp = self.signedRequest_Market(path='/ticker', params=params)
        return resp

    #市场深度
    def depth(self,currency,size,merge):
        currency = currency.lower()
        if 'usd' in currency:
            currency = currency.replace('usd','usdt')
        params = {'currency':currency,'size':size,'merge':merge}
        resp = self.signedRequest_Market(path='/depth', params=params)
        return resp

    #历史成交
    def trades(self, currency):
        currency = currency.lower()
        if 'usd' in currency:
            currency = currency.replace('usd', 'usdt')
        params = {'currency': currency}
        resp = self.signedRequest_Market(path='/trades', params=params)
        return resp

    #k线
    def kline(self, currency,type, size):
        currency = currency.lower()
        if 'usd' in currency:
            currency = currency.replace('usd', 'usdt')
        params = {'currency': currency, 'type': type, 'size': size}
        resp = self.signedRequest_Market(path='/kline', params=params)
        return resp

#################################行情类接口 end##################

apikey = ''
secretkey = ''
client = Client_BitGet(apikey,secretkey)

####order start######
#交易对类型，请填写系统所支持的交易对，例：IOST/BTC 写成：iost_btc
currency = 'iost_btc'
#交易类型，0(buy)/1(sell)
tradeType = '0'
#单价
price = '1'
#要买或卖的数量
amount = '1'
#respJson = client.order(currency,tradeType,price,amount)
#print(respJson)
####order end######


####cancel start######
#交易对类型，请填写系统所支持的交易对，例：IOST/BTC 写成：iost_btc
currency = 'usdt_btc'
#委单的ID
id = '391400717711093760'
#respJson = client.cancel(id,currency)
#print(respJson)
####cancel end######



####getOrder start######
#交易对类型，请填写系统所支持的交易对，例：IOST/BTC 写成：iost_btc
currency = 'usdt_btc'
#委单ID
order_id = '391400717711093760'
#respJson = client.getOrder(order_id,currency)
#print(respJson)
####getOrder end######



####getOrders start######
tradeType = '0'
#交易对类型，请填写系统所支持的交易对，例：IOST/BTC 写成：iost_btc
currency = 'btc_usdt'
#第几页，
pageIndex = '1'
#每页有多少条数据，
pageSize = '40'
#respJson = client.getOrders(tradeType, currency, pageIndex, pageSize)
#print(respJson)
####getOrders end######



####getAccountInfo start######
#respJson = client.getAccountInfo()
#print(respJson)
####getAccountInfo end######




####getUserAddress start######
#系统所支持的币种
currency = 'btc'
#respJson = client.getUserAddress(currency)
#print(respJson)
####getUserAddress end######



####getWithdrawRecord start 获取数字资产提现记录######
#系统所支持的币种
currency = 'btc'
#当前页码
pageIndex = '1'
#当前页最多有多少条数据
pageSize = '40'
#respJson = client.getWithdrawRecord(currency,pageIndex,pageSize)
#print(respJson)
####getWithdrawRecord end######



####getChargeRecord start 获取数字资产充值记录######
#系统所支持的币种
currency = 'btc'
#当前页码
pageIndex = '1'
#当前页最多有多少条数据
pageSize = '40'
#respJson = client.getChargeRecord(currency,pageIndex,pageSize)
#print(respJson)
####getChargeRecord end######



####withdraw start 提现######
#提现的数据
amount = '1'
#提现的币种
currency = 'btc'
#提现的手续费
fees = '1'
#提现钱包地址
receiveAddress = '1PaHiYCBFXuotKSSg7ZFGxB4n99CaDNYi'
#respJson = client.withdraw(amount,currency,fees,receiveAddress)
#print(respJson)
####withdraw end######



################行情类接口 start #############

####ticker start ######
#系统所支持的交易对
currency = 'iost_btc'
#respJson = client.ticker(currency)
#print(respJson)
####ticker end######


####depth start ######
#系统所支持的交易对
currency = 'iost_btc'
#档位，1-50
size = '20'

merge = '0.01'
#respJson = client.depth(currency,size,merge)
#print(respJson)
####depth end######


####trades start ######
#系统所支持的交易对
currency = 'iost_btc'
#respJson = client.trades(currency)
#print(respJson)
####trades end######


####kline start ######
#系统所支持的交易对
currency = 'iost_btc'
type = '0'
size = '1'
respJson = client.kline(currency,type,size)
print(respJson)
####kline end######


